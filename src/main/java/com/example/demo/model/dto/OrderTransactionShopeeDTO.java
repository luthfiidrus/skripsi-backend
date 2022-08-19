package com.example.demo.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderTransactionShopeeDTO implements Serializable {

    private String ordersn;

    private String orderStatus;

    private Integer shopid;

    private String createTime;

    private String payTime;

    private String updateTime;

    private String buyerUsername;

    private String recipientName;

    private String recipientPhone;

    private String recipientAddress;

    private String recipientDistrict;

    private String recipientCity;

    private String recipientProvince;

    private String timestamp;

    private BigDecimal estimatedShippingFee;

}
