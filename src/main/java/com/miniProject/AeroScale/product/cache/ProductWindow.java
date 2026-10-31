package com.miniProject.AeroScale.product.cache;

import com.miniProject.AeroScale.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public record ProductWindow(List<ProductResponse> productResponsesList, int totalPages) {

    public Page<ProductResponse> slice(Pageable pageable) {
        int n = this.productResponsesList.size();
        int from = Math.min(pageable.getPageNumber() * pageable.getPageSize(), n);
        int to = Math.min((pageable.getPageNumber() + 1) * pageable.getPageSize(), n);

        return new PageImpl<>(this.productResponsesList.subList(from, to), pageable, totalPages);
    }
}
