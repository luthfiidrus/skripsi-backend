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
import com.example.demo.model.dto.TotalSoldItemEachProvinceEachBrandDTO;
import com.example.demo.model.dto.TotalSoldItemEachMonthEachBrandDTO;
import com.example.demo.service.query.BrandCommerceQuery;
import com.example.demo.service.query.GetPredictQuery;
import com.example.demo.service.query.ShopeeQuery;
import org.javatuples.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QueryServiceImpl implements QueryService{

    private HashMap<Integer, String> shopeeBrandCode;

    public static HashMap<Integer, String> month;

    private final JdbcTemplate jdbcTemplate;

    public QueryServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        fillShopeeBrandCode();
        fillMonth();
    }

    public void fillShopeeBrandCode() {
        this.shopeeBrandCode = new HashMap<>();
        this.shopeeBrandCode.put(59763733, "Wardah");
        this.shopeeBrandCode.put(575053680, "Biodef");
        this.shopeeBrandCode.put(637555425, "Tavi");
        this.shopeeBrandCode.put(63983008, "Emina");
        this.shopeeBrandCode.put(326487418, "Kahf");
        this.shopeeBrandCode.put(524963178, "Labore");
        this.shopeeBrandCode.put(63984475, "Make Over");
        this.shopeeBrandCode.put(401724234, "OMG");
        this.shopeeBrandCode.put(652866307, "Instaperfect");
        this.shopeeBrandCode.put(625116419, "Crystallure");
    }

    public void fillMonth() {
        this.month = new HashMap<>();
        this.month.put(1, "Januari");
        this.month.put(2, "Februari");
        this.month.put(3, "Maret");
        this.month.put(4, "April");
        this.month.put(5, "Mei");
        this.month.put(6, "Juni");
        this.month.put(7, "Juli");
        this.month.put(8, "Agustus");
        this.month.put(9, "September");
        this.month.put(10, "Oktober");
        this.month.put(11, "November");
        this.month.put(12, "Desember");
    }

    @Override
    public Pair<Map<String, Map<String, BigDecimal>>, Set<String>> getTotalSoldItemEachProvinceEachBrand(String sourceData) {
        String sql = switch (sourceData) {
            case "brand-commerce" -> BrandCommerceQuery.getTotalSoldItemEachCityEachBrandQuery;
            case "shopee" -> ShopeeQuery.getTotalSoldItemEachCityEachBrandQuery;
            default -> "";
        };
        Map<String, Map<String, BigDecimal>> map = new HashMap<>();
        Set<String> tempBrands = new HashSet<>();
        jdbcTemplate.query(sql, (rs, rowNum) -> {
            if (!map.containsKey(rs.getString("province"))) {
                map.put(rs.getString("province"), new HashMap<String, BigDecimal>());
            }
            if (Objects.isNull(rs.getBigDecimal("quantity"))) {
                map.get(rs.getString("province")).put(rs.getString("brand"), BigDecimal.valueOf(0));
            }
            else {
                map.get(rs.getString("province")).put(rs.getString("brand"), rs.getBigDecimal("quantity"));
            }
            tempBrands.add(rs.getString("brand"));
            return new TotalSoldItemEachProvinceEachBrandDTO(
                    rs.getString("brand"),
                    rs.getString("province"),
                    rs.getBigDecimal("quantity")
            );
        });
        Set<String> finalBrands = tempBrands;
        if (sourceData.equals("shopee")) {
            Set<String> provinces = map.keySet();
            for (String province: provinces) {
                List<String> eachBrandInProvince = map.get(province).keySet().stream().toList();
                for (int i = 0; i < eachBrandInProvince.size(); i++) {
                    BigDecimal quantity = map.get(province).remove(eachBrandInProvince.get(i));
                    map.get(province).put(this.shopeeBrandCode.get(Integer.parseInt(eachBrandInProvince.get(i))), quantity);
                }
            }
            finalBrands = finalBrands.stream().map(brandCode -> this.shopeeBrandCode.get(Integer.parseInt(brandCode))).collect(Collectors.toSet());
        }
        return new Pair<>(map, finalBrands);
    }

    @Override
    public List<TotalSoldItemEachProvinceDTO> getTotalSoldItemTopTenProvince(String sourceData) {
        String sql = switch (sourceData) {
            case "brand-commerce" -> BrandCommerceQuery.getTotalSoldItemTopTenProvinceQuery;
            case "shopee" -> ShopeeQuery.getTotalSoldItemTopTenProvinceQuery;
            default -> "";
        };
        List<TotalSoldItemEachProvinceDTO> sqlResult = jdbcTemplate.query(sql, (rs, rowNum) -> TotalSoldItemEachProvinceDTO
                .builder()
                .provinsi(rs.getString("provinsi"))
                .total(rs.getInt("total"))
                .build());

        return sqlResult;
    }

    @Override
    public List<TotalSoldItemEachBrandDTO> getTotalSoldItemEachBrand(String sourceData) {
        String sql = switch (sourceData) {
            case "brand-commerce" -> BrandCommerceQuery.getTotalSoldItemEachBrandQuery;
            case "shopee" -> ShopeeQuery.getTotalSoldItemEachBrandQuery;
            default -> "";
        };
        List<TotalSoldItemEachBrandDTO> sqlResult = jdbcTemplate.query(sql, (rs, rowNum) -> TotalSoldItemEachBrandDTO
                .builder()
                .brand(rs.getString("brand"))
                .quantity(rs.getBigDecimal("quantity"))
                .build());

        if (sourceData.equals("shopee")) {
            for (TotalSoldItemEachBrandDTO item: sqlResult) {
                item.setBrand(this.shopeeBrandCode.get(Integer.parseInt(item.getBrand())));
            }
        }
        return sqlResult;
    }

    @Override
    public Pair<Map<Integer, Map<String, BigDecimal>>, Set<String>> getTotalSoldItemEachMonthEachBrand(
            String sourceData,
            int year) {
        String sql = String.format(switch (sourceData) {
            case "brand-commerce" -> BrandCommerceQuery.getTotalSoldItemEachMonthEachBrandQuery;
            case "shopee" -> ShopeeQuery.getTotalSoldItemEachMonthEachBrandQuery;
            default -> "";
        }, year);
        Map<Integer, Map<String, BigDecimal>> map = new HashMap<>();
        Set<String> tempBrands = new HashSet<>();
        jdbcTemplate.query(String.format(sql, year), (rs, rowNum) -> {
            if (!map.containsKey(rs.getInt("bulan"))) {
                map.put(rs.getInt("bulan"), new HashMap<String, BigDecimal>());
            }
            map.get(rs.getInt("bulan")).put(rs.getString("brand"), rs.getBigDecimal("quantity"));
            tempBrands.add(rs.getString("brand"));
            return TotalSoldItemEachMonthEachBrandDTO.builder()
                    .brand(rs.getString("brand"))
                    .bulan(rs.getInt("bulan"))
                    .quantity(rs.getBigDecimal("quantity"))
                    .build();
        });
        Set<String> finalBrands = tempBrands;
        List<Integer> bulans = map.keySet().stream().toList();
        for (int i = 0; i < bulans.size(); i++) {
            int bulan = bulans.get(i);
//            String bulanNew = this.month.get(Integer.parseInt(bulan));
            Map<String, BigDecimal> brandAndQuantity = map.remove(bulan);
            map.put(bulan, brandAndQuantity);
        }
        if (sourceData.equals("shopee")) {
            bulans = map.keySet().stream().toList();
            for (int i = 0; i < bulans.size(); i++) {
                int bulan = bulans.get(i);
                List<String> eachBrandInMonth = map.get(bulan).keySet().stream().toList();
                for (int j = 0; j < eachBrandInMonth.size(); j++) {
                    BigDecimal quantity = map.get(bulan).remove(eachBrandInMonth.get(j));
                    map.get(bulan).put(this.shopeeBrandCode.get(Integer.parseInt(eachBrandInMonth.get(j))), quantity);
                }
            }
            finalBrands = finalBrands.stream().map(brandCode -> this.shopeeBrandCode.get(Integer.parseInt(brandCode))).collect(Collectors.toSet());
        }
        return new Pair<>(map, finalBrands);
    }

    @Override
    public Pair<Map<String, Map<String, BigDecimal>>, Set<String>> getTotalSoldItemEachYearEachBrand(String sourceData) {
        String sql = switch (sourceData) {
            case "brand-commerce" -> BrandCommerceQuery.getTotalSoldItemEachYearEachBrandQuery;
            case "shopee" -> ShopeeQuery.getTotalSoldItemEachYearEachBrandQuery;
            default -> "";
        };
        Map<String, Map<String, BigDecimal>> map = new HashMap<>();
        Set<String> tempBrands = new HashSet<>();
        jdbcTemplate.query(sql, (rs, rowNum) -> {
            if (!map.containsKey(rs.getString("tahun"))) {
                map.put(rs.getString("tahun"), new HashMap<String, BigDecimal>());
            }
            map.get(rs.getString("tahun")).put(rs.getString("brand"), rs.getBigDecimal("quantity"));
            tempBrands.add(rs.getString("brand"));
            return TotalSoldItemEachMonthEachBrandDTO.builder()
                    .brand(rs.getString("brand"))
                    .bulan(rs.getInt("tahun"))
                    .quantity(rs.getBigDecimal("quantity"))
                    .build();
        });
        Set<String> finalBrands = tempBrands;
        if (sourceData.equals("shopee")) {
            List<String> tahuns = map.keySet().stream().toList();
            for (int i = 0; i < tahuns.size(); i++) {
                String tahun = tahuns.get(i);
                List<String> eachBrandInMonth = map.get(tahun).keySet().stream().toList();
                for (int j = 0; j < eachBrandInMonth.size(); j++) {
                    BigDecimal quantity = map.get(tahun).remove(eachBrandInMonth.get(j));
                    map.get(tahun).put(this.shopeeBrandCode.get(Integer.parseInt(eachBrandInMonth.get(j))), quantity);
                }
            }
            finalBrands = finalBrands.stream().map(brandCode -> this.shopeeBrandCode.get(Integer.parseInt(brandCode))).collect(Collectors.toSet());
        }
        return new Pair<>(map, finalBrands);
    }

    @Override
    public List<GetPredictResultOneDTO> getPredictResultOne() {
        String sql = GetPredictQuery.getPredictResultOne;
        List<GetPredictResultOneDTO> sqlResult = jdbcTemplate.query(sql, (rs, rowNum) -> GetPredictResultOneDTO
                .builder()
                .prediksi(rs.getString("keterangan"))
                .total(rs.getInt("total"))
                .build());

        return sqlResult;
    }

    @Override
    public List<GetPredictResultTwoDTO> getPredictResultTwo() {
        String sql = GetPredictQuery.getPredictResultTwo;
        List<GetPredictResultTwoDTO> sqlResult = jdbcTemplate.query(sql, (rs, rowNum) -> GetPredictResultTwoDTO
                .builder()
                .prediksiItem(rs.getString("prediksi_item"))
                .total(rs.getInt("total"))
                .build());

        return sqlResult;
    }

    @Override
    public List<GetPredictResultThreeDTO> getPredictResultThree() {
        String sql = GetPredictQuery.getPredictResultThree;
        List<GetPredictResultThreeDTO> sqlResult = jdbcTemplate.query(sql, (rs, rowNum) -> GetPredictResultThreeDTO
                .builder()
                .prediksi(rs.getString("keterangan"))
                .total(rs.getInt("total"))
                .build());

        return sqlResult;
    }

    @Override
    public List<GetPredictResultFourDTO> getPredictResultFour() {
        String sql = GetPredictQuery.getPredictResultFour;
        List<GetPredictResultFourDTO> sqlResult = jdbcTemplate.query(sql, (rs, rowNum) -> GetPredictResultFourDTO
                .builder()
                .provinsi(rs.getString("provinsi"))
                .total(rs.getInt("total"))
                .build());

        return sqlResult;
    }

    @Override
    public List<GetPredictResultFiveDTO> getPredictResultFive() {
        String sql = GetPredictQuery.getPredictResultFive;
        List<GetPredictResultFiveDTO> sqlResult = jdbcTemplate.query(sql, (rs, rowNum) -> GetPredictResultFiveDTO
                .builder()
                .revenue(rs.getDouble("revenue"))
                .total(rs.getInt("total"))
                .build());

        return sqlResult;
    }

    @Override
    public GetMonthYearPredictionDTO getMonthYearPrediction() {
        String sql = GetPredictQuery.getMonthYearPrediction;
        GetMonthYearPredictionDTO sqlResult = jdbcTemplate.query(sql, (rs, rowNum) -> GetMonthYearPredictionDTO
                .builder()
                .bulan(rs.getString("bulan"))
                .tahun(rs.getString("tahun"))
                .build()).get(0);

        return sqlResult;
    }

//    @Override
//    public GetPredictResultOneTableDTO getPredictResultOneTable(Pageable pageable) {
//        String sql = GetPredictQuery.getPredictResultOneTable;
//        List<GetPredictResultOneTableDTO.Row> sqlResult = jdbcTemplate.query(
//                String.format(sql, pageable.getOffset()-pageable.getPageSize(), pageable.getPageSize()), (rs, rowNum) -> GetPredictResultOneTableDTO.Row
//                        .builder()
//                        .customer_id(rs.getString("customer_id"))
//                        .keterangan(rs.getString("keterangan"))
//                        .build());
//
//        String sqlCount = GetPredictQuery.getPredictResultOneCount;
//        Long count = jdbcTemplate.query(sqlCount, (rs, rowNum) -> rs.getLong(1)).get(0);
//
//        return GetPredictResultOneTableDTO
//                .builder()
//                .page(pageable.getPageNumber())
//                .per_page(pageable.getPageSize())
//                .total(count)
//                .total_pages((long) Math.ceil(count.doubleValue()/pageable.getPageSize()))
//                .data(sqlResult)
//                .build();
//    }
//
//    @Override
//    public GetPredictResultTwoTableDTO getPredictResultTwoTable(Pageable pageable) {
//        String sql = GetPredictQuery.getPredictResultTwoTable;
//        List<GetPredictResultTwoTableDTO.Row> sqlResult = jdbcTemplate.query(
//                String.format(sql, pageable.getOffset()-pageable.getPageSize(), pageable.getPageSize()), (rs, rowNum) -> GetPredictResultTwoTableDTO.Row
//                        .builder()
//                        .customer_id(rs.getString("customer_id"))
//                        .prediksi_item(rs.getString("prediksi_item"))
//                        .build());
//
//        String sqlCount = GetPredictQuery.getPredictResultTwoCount;
//        Long count = jdbcTemplate.query(sqlCount, (rs, rowNum) -> rs.getLong(1)).get(0);
//
//        return GetPredictResultTwoTableDTO
//                .builder()
//                .page(pageable.getPageNumber())
//                .per_page(pageable.getPageSize())
//                .total(count)
//                .total_pages((long) Math.ceil(count.doubleValue()/pageable.getPageSize()))
//                .data(sqlResult)
//                .build();
//    }
//
//    @Override
//    public GetPredictResultThreeTableDTO getPredictResultThreeTable(Pageable pageable) {
//        String sql = GetPredictQuery.getPredictResultThreeTable;
//        List<GetPredictResultThreeTableDTO.Row> sqlResult = jdbcTemplate.query(
//                String.format(sql, pageable.getOffset()-pageable.getPageSize(), pageable.getPageSize()), (rs, rowNum) -> GetPredictResultThreeTableDTO.Row
//                        .builder()
//                        .customer_id(rs.getString("customer_id"))
//                        .keterangan(rs.getString("keterangan"))
//                        .build());
//
//        String sqlCount = GetPredictQuery.getPredictResultThreeCount;
//        Long count = jdbcTemplate.query(sqlCount, (rs, rowNum) -> rs.getLong(1)).get(0);
//
//        return GetPredictResultThreeTableDTO
//                .builder()
//                .page(pageable.getPageNumber())
//                .per_page(pageable.getPageSize())
//                .total(count)
//                .total_pages((long) Math.ceil(count.doubleValue()/pageable.getPageSize()))
//                .data(sqlResult)
//                .build();
//    }

}
