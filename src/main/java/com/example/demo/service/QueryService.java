package com.example.demo.service;

import com.example.demo.model.dto.GetMonthYearPredictionDTO;
import com.example.demo.model.dto.GetPredictResultFiveDTO;
import com.example.demo.model.dto.GetPredictResultFourDTO;
import com.example.demo.model.dto.GetPredictResultOneDTO;
import com.example.demo.model.dto.GetPredictResultOneTableDTO;
import com.example.demo.model.dto.GetPredictResultThreeDTO;
import com.example.demo.model.dto.GetPredictResultThreeTableDTO;
import com.example.demo.model.dto.GetPredictResultTwoDTO;
import com.example.demo.model.dto.GetPredictResultTwoTableDTO;
import com.example.demo.model.dto.TotalSoldItemEachBrandDTO;
import com.example.demo.model.dto.TotalSoldItemEachProvinceDTO;
import org.javatuples.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QueryService {
    Pair<Map<String, Map<String, BigDecimal>>, Set<String>> getTotalSoldItemEachProvinceEachBrand(String sourceData);
    List<TotalSoldItemEachProvinceDTO> getTotalSoldItemTopTenProvince(String sourceData);
    List<TotalSoldItemEachBrandDTO> getTotalSoldItemEachBrand(String sourceData);
    Pair<Map<Integer, Map<String, BigDecimal>>, Set<String>> getTotalSoldItemEachMonthEachBrand(String sourceData, int year);
    Pair<Map<String, Map<String, BigDecimal>>, Set<String>> getTotalSoldItemEachYearEachBrand(String sourceData);
    List<GetPredictResultOneDTO> getPredictResultOne();
    List<GetPredictResultTwoDTO> getPredictResultTwo();
    List<GetPredictResultThreeDTO> getPredictResultThree();
    List<GetPredictResultFourDTO> getPredictResultFour();
    List<GetPredictResultFiveDTO> getPredictResultFive();
//    GetPredictResultOneTableDTO getPredictResultOneTable(Pageable pageable);
//    GetPredictResultTwoTableDTO getPredictResultTwoTable(Pageable pageable);
//    GetPredictResultThreeTableDTO getPredictResultThreeTable(Pageable pageable);
    GetMonthYearPredictionDTO getMonthYearPrediction();
}
