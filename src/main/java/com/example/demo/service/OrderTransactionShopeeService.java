package com.example.demo.service;

import java.util.concurrent.CompletableFuture;

public interface OrderTransactionShopeeService {
    CompletableFuture<String> sendOneMonth(int month);
}
