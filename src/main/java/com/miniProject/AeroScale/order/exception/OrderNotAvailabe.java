package com.miniProject.AeroScale.order.exception;

import com.razorpay.Order;

public class OrderNotAvailabe extends RuntimeException{

    public OrderNotAvailabe(String msg) {super(msg);}
}
