package com.miniProject.AeroScale.SellerModule.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusBreakdown {
    private String status;
    private long count;
}
