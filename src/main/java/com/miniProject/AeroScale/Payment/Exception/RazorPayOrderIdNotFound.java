package com.miniProject.AeroScale.Payment.Exception;

import com.miniProject.AeroScale.Payment.DTO.Response.RazorPayOrderResponse;

public class RazorPayOrderIdNotFound extends RuntimeException{

    public RazorPayOrderIdNotFound(String msg) {super(msg);}
}
