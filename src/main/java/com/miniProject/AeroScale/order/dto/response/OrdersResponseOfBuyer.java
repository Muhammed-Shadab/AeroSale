package com.miniProject.AeroScale.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersResponseOfBuyer {

    private List<OrderResponse> content;
    private boolean last;
    private long totalElements;
    private long totalPages;
    private long number;
    private int size;


}
