package com.example.demo.service;

import java.util.concurrent.CompletableFuture;

public interface OrderTransactionBrandCommerceService {
    CompletableFuture<String> sendOneMonth(int month);
}
