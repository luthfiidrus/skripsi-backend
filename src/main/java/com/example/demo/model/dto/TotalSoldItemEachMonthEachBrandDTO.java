package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalSoldItemEachMonthEachBrandDTO {

    private String brand;

    private Integer bulan;

    private BigDecimal quantity;
}
