package com.example.demo.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderTransactionBrandCommerceDTO implements Serializable {

    private String brand;

    private BigInteger orderId;

    private String orderStatus;

    private String dateOrder;

    private String customerId;

    private String paymentMethod;

    private String dateCompleted;

    private String datePaid;

    private String dateInvoice;

    private String noInvoice;

    private String orderCurrency;

    private BigDecimal orderSubtotal;

    private BigDecimal cartDiscount;

    private BigDecimal redeemedPoint;

    private BigDecimal orderShipping;

    private BigDecimal orderTotal;

    private String couponUsage;

    private String orderStockReduced;

    private String xenditInvoice;

    private String version;

    private String writeDate;

}
