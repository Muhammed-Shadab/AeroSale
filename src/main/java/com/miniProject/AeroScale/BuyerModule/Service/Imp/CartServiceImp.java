package com.miniProject.AeroScale.BuyerModule.Service.Imp;

import com.miniProject.AeroScale.BuyerModule.DTO.Request.ItemDataForCart;
import com.miniProject.AeroScale.BuyerModule.DTO.Response.CartResponse;
import com.miniProject.AeroScale.BuyerModule.DTO.Response.UpdateCartItemRequest;
import com.miniProject.AeroScale.BuyerModule.Entity.Buyer;
import com.miniProject.AeroScale.BuyerModule.Entity.CartItem;
import com.miniProject.AeroScale.BuyerModule.Exception.CartItemNotFoundException;
import com.miniProject.AeroScale.BuyerModule.Exception.InsufficientStockException;
import com.miniProject.AeroScale.BuyerModule.Exception.RequiredThingsNotFoundException;
import com.miniProject.AeroScale.BuyerModule.Repository.BuyerRepository;
import com.miniProject.AeroScale.BuyerModule.Repository.CartRespository;
import com.miniProject.AeroScale.BuyerModule.Service.BuyerService;
import com.miniProject.AeroScale.BuyerModule.Service.CartService;
import com.miniProject.AeroScale.product.entity.Product;
import com.miniProject.AeroScale.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements CartService {

    private final CartRespository cartRespository;
    private final BuyerRepository buyerRepository;
    private final ProductRepository productRepository;


    @Override
    @Transactional
    public void addItemToCart(ItemDataForCart itemDataForCart, UUID id) {
        Buyer buyer = buyerRepository.findById(id).
                orElseThrow(() -> new RequiredThingsNotFoundException("Buyer NotFound!!"));

        Product product = productRepository.findById(itemDataForCart.getProductId())
                .orElseThrow(() -> new RequiredThingsNotFoundException("Item not Found"));

        CartItem existing = cartRespository.findByBuyerAndProductId(buyer.getId(), product.getId()).orElse(null);

        assertStockAvailable(product, itemDataForCart.getCount());
        if(existing == null) {
            CartItem item = CartItem.builder()
                    .buyer(buyer)
                    .product(product)
                    .itemCount(itemDataForCart.getCount())
                    .build();

            cartRespository.save(item);
        }else {
            existing.setItemCount(itemDataForCart.getCount());
            cartRespository.save(existing);
        }
    }

    @Override
    @Transactional
    public void removeItem(UUID itemId, UUID buyerId) {
        CartItem item = cartRespository.findByIdAndBuyerId(itemId, buyerId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart Item not Found " + itemId));
        cartRespository.delete(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartResponse> getAllCarts(UUID id) {
        List<CartItem> cartItemList = cartRespository.findAllByBuyerId(id);

        List<CartResponse> cartResponsesList = new ArrayList<>();
        for(CartItem item: cartItemList) {
            cartResponsesList.add(cartItemToCartResponseConverter(item));
        }
        return cartResponsesList;
    }

    @Override
    @Transactional
    public void UpdateCartDetails(UpdateCartItemRequest updateCartItemRequest, UUID buyerId) {
        CartItem item = cartRespository.findByIdAndBuyerId(updateCartItemRequest.getCartid(), buyerId)
                .orElseThrow(() -> new CartItemNotFoundException("cart Item NotFound!!"));

        Product product = item.getProduct();
        assertStockAvailable(product, updateCartItemRequest.getItemCount());

        item.setItemCount(updateCartItemRequest.getItemCount());
        cartRespository.save(item);
    }

    @Override
    @Transactional
    public void clearCart(UUID id) {
        cartRespository.deleteAllByBuyerId(id);
    }

    private CartResponse cartItemToCartResponseConverter(CartItem item) {
        if(item == null) return null;
        Product product =  item.getProduct();
        return CartResponse.builder()
                .id(item.getId())
                .itemCount(item.getItemCount())
                .itemName(product.getName())
                .pricePerItem(product.getPrice())
                .CurrentStockOfProduct(product.getStockQuantity())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    private void assertStockAvailable(Product product, int reqQuantity) {
        if(product.getStockQuantity() == null || product.getStockQuantity() < reqQuantity) {
            throw new InsufficientStockException("Only " + (product.getStockQuantity() == null ? 0: product.getStockQuantity())
                    + " Units of " + product.getName() + " are available");
        }
    }
}
