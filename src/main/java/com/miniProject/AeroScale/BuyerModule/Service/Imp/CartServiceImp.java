package com.miniProject.AeroScale.BuyerModule.Service.Imp;

import com.miniProject.AeroScale.BuyerModule.DTO.Request.ItemDataForCart;
import com.miniProject.AeroScale.BuyerModule.Entity.Buyer;
import com.miniProject.AeroScale.BuyerModule.Entity.CartItem;
import com.miniProject.AeroScale.BuyerModule.Exception.InsufficientStockException;
import com.miniProject.AeroScale.BuyerModule.Exception.RequiredThingsNotFoundException;
import com.miniProject.AeroScale.BuyerModule.Repository.BuyerRepository;
import com.miniProject.AeroScale.BuyerModule.Repository.CartRespository;
import com.miniProject.AeroScale.BuyerModule.Service.BuyerService;
import com.miniProject.AeroScale.BuyerModule.Service.CartService;
import com.miniProject.AeroScale.product.entity.Product;
import com.miniProject.AeroScale.product.repository.ProductRepository;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.miniProject.AeroScale.AuthModule.Security.JwtAuthenticationFilter.AuthenticatedObject;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements CartService {

    private final CartRespository cartRespository;
    private final BuyerService buyerService;
    private final BuyerRepository buyerRepository;
    private final ProductRepository productRepository;


    @Override
    public void addItemToCart(ItemDataForCart itemDataForCart, AuthenticatedObject authenticatedObject) {
        Buyer buyer = buyerRepository.findById(authenticatedObject.id()).
                orElseThrow(() -> new RequiredThingsNotFoundException("To add items in cart buyer id is required"));

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

    private void assertStockAvailable(Product product, int reqQuantity) {
        if(product.getStockQuantity() == null || product.getStockQuantity() < reqQuantity) {
            throw new InsufficientStockException("Only " + (product.getStockQuantity() == null ? 0: product.getStockQuantity())
                    + " Units of " + product.getName() + " are available");
        }
    }
}
