package com.example.demo.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GetPredictResultOneTableDTO {

    private int page;

    private int per_page;

    private long total;

    private long total_pages;

    private List<Row> data;

    @AllArgsConstructor
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Row {

        private String customer_id;

        private String keterangan;
    }
}