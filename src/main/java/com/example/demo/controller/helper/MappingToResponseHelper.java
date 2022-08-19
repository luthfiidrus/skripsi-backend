package com.example.demo.controller.helper;

import com.example.demo.model.dto.GetMonthYearPredictionDTO;
import com.example.demo.model.dto.GetPredictResultFiveDTO;
import com.example.demo.model.dto.GetPredictResultFourDTO;
import com.example.demo.model.dto.GetPredictResultOneDTO;
import com.example.demo.model.dto.GetPredictResultThreeDTO;
import com.example.demo.model.dto.GetPredictResultTwoDTO;
import com.example.demo.model.dto.TotalSoldItemEachBrandDTO;
import com.example.demo.model.dto.TotalSoldItemEachProvinceDTO;
import com.example.demo.service.QueryServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MappingToResponseHelper {

    public static List<List<Object>> toTotalSoldItemEachProvinceEachBrand(
            Map<String, Map<String, BigDecimal>> map,
            Set<String> brands) {

        List<Object> header = new ArrayList<>();
        header.add("Province");
        header.addAll(brands);

        List<List<Object>> response = new ArrayList<>();
        response.add(header);
        for (Object province: map.keySet()) {
            List<Object> arrayDetail = new ArrayList<>();
            arrayDetail.add(province);
            for (int i = 1; i < header.size(); i++) {
                String brand = (String) header.get(i);
                try {
                    arrayDetail.add(map.get(province).get(brand));
                } catch (Exception e) {
                    arrayDetail.add(0);
                }
            }
            response.add(arrayDetail);
        }
        return response;
    }

    public static List<List<Object>> toTotalSoldItemTopTenProvince(List<TotalSoldItemEachProvinceDTO> array) {
        List<Object> header = new ArrayList<>();
        header.add("Provinsi");
        header.add("Total");

        List<List<Object>> response = new ArrayList<>();
        response.add(header);
        for (TotalSoldItemEachProvinceDTO dto: array) {
            List<Object> arrayDetail = new ArrayList<>();
            arrayDetail.add(dto.getProvinsi());
            arrayDetail.add(dto.getTotal());
            response.add(arrayDetail);
        }

        return response;
    }

    public static List<List<Object>> toTotalSoldItemEachBrand(List<TotalSoldItemEachBrandDTO> array) {
        List<Object> header = new ArrayList<>();
        header.add("Brand");
        header.add("Jumlah");

        List<List<Object>> response = new ArrayList<>();
        response.add(header);
        for (TotalSoldItemEachBrandDTO item: array) {
            List<Object> arrayDetail = new ArrayList<>();
            arrayDetail.add(item.getBrand());
            arrayDetail.add(item.getQuantity());
            response.add(arrayDetail);
        }
        return response;
    }

    public static List<List<Object>> toTotalSoldItemEachMonthEachBrand(
            Map<Integer, Map<String, BigDecimal>> map,
            Set<String> brands) {
        List<Object> header = new ArrayList<>();
        header.add("Bulan");
        header.addAll(brands);

        List<Integer> listBulan = new ArrayList<>(map.keySet().stream().toList());
        Collections.sort(listBulan);

        List<String> listNewBulan = new ArrayList<>();

        for (int i = 0; i < listBulan.size(); i++) {
            listNewBulan.add(QueryServiceImpl.month.get(listBulan.get(i)));
        }

        List<List<Object>> response = new ArrayList<>();
        response.add(header);
        for (int i = 0; i < listBulan.size(); i++) {
            List<Object> arrayDetail = new ArrayList<>();
            arrayDetail.add(listNewBulan.get(i));
            for (int j = 1; j < header.size(); j++) {
                String brand = (String) header.get(j);
                arrayDetail.add(map.get(listBulan.get(i)).get(brand));
            }
            response.add(arrayDetail);
        }
        return response;
    }

    public static List<List<Object>> toTotalSoldItemEachYearEachBrand(
            Map<String, Map<String, BigDecimal>> map,
            Set<String> brands) {
        List<Object> header = new ArrayList<>();
        header.add("Tahun");
        header.addAll(brands);

        List<List<Object>> response = new ArrayList<>();
        response.add(header);
        for (Object tahun: map.keySet().stream().sorted().collect(Collectors.toList())) {
            List<Object> arrayDetail = new ArrayList<>();
            arrayDetail.add(tahun);
            for (int i = 1; i < header.size(); i++) {
                String brand = (String) header.get(i);
                arrayDetail.add(map.get(tahun).get(brand));
            }
            response.add(arrayDetail);
        }
        return response;
    }

    public static List<List<Object>> toGetPredictResultOne(List<GetPredictResultOneDTO> array) {
        List<Object> header = new ArrayList<>();
        header.add("Prediksi");
        header.add("Total");

        List<List<Object>> response = new ArrayList<>();
        response.add(header);
        for (GetPredictResultOneDTO predict: array) {
            List<Object> arrayDetail = new ArrayList<>();
            arrayDetail.add(predict.getPrediksi());
            arrayDetail.add(predict.getTotal());
            response.add(arrayDetail);
        }

        return response;
    }

    public static List<List<Object>> toGetPredictResultTwo(List<GetPredictResultTwoDTO> array) {
        List<Object> header = new ArrayList<>();
        header.add("Prediksi Item");
        header.add("Total");

        List<List<Object>> response = new ArrayList<>();
        response.add(header);
        for (GetPredictResultTwoDTO predict: array) {
            List<Object> arrayDetail = new ArrayList<>();
            arrayDetail.add(predict.getPrediksiItem());
            arrayDetail.add(predict.getTotal());
            response.add(arrayDetail);
        }

        return response;
    }

    public static List<List<Object>> toGetPredictResultThree(List<GetPredictResultThreeDTO> array) {
        List<Object> header = new ArrayList<>();
        header.add("Prediksi");
        header.add("Total");

        List<List<Object>> response = new ArrayList<>();
        response.add(header);
        for (GetPredictResultThreeDTO predict: array) {
            List<Object> arrayDetail = new ArrayList<>();
            arrayDetail.add(predict.getPrediksi());
            arrayDetail.add(predict.getTotal());
            response.add(arrayDetail);
        }

        return response;
    }

    public static List<List<Object>> toGetPredictResultFour(List<GetPredictResultFourDTO> array) {
        List<Object> header = new ArrayList<>();
        header.add("Provinsi");
        header.add("Total");

        List<List<Object>> response = new ArrayList<>();
        response.add(header);
        for (GetPredictResultFourDTO predict: array) {
            List<Object> arrayDetail = new ArrayList<>();
            arrayDetail.add(predict.getProvinsi());
            arrayDetail.add(predict.getTotal());
            response.add(arrayDetail);
        }

        return response;
    }

    public static List<List<Object>> toGetPredictResultFive(List<GetPredictResultFiveDTO> array) {
        List<Object> header = new ArrayList<>();
        header.add("Revenue");
        header.add("Total");

        List<List<Object>> response = new ArrayList<>();
        response.add(header);
        for (GetPredictResultFiveDTO predict: array) {
            List<Object> arrayDetail = new ArrayList<>();
            arrayDetail.add(predict.getRevenue());
            arrayDetail.add(predict.getTotal());
            response.add(arrayDetail);
        }

        return response;
    }

    public static String toGetMonthYearPrediction(GetMonthYearPredictionDTO data) {
        int bulan = Integer.parseInt(data.getBulan()) % 12;
        return QueryServiceImpl.month.get(bulan+1) + " " + data.getTahun();
    }
}
