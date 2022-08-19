package com.example.demo.controller;

import com.example.demo.controller.helper.MappingToResponseHelper;
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
import com.example.demo.model.response.UploadFileResponse;
import com.example.demo.service.FileStorageServiceImpl;
import com.example.demo.service.OrderTransactionBrandCommerceService;
import com.example.demo.service.OrderTransactionShopeeService;
import com.example.demo.service.QueryService;
import com.example.demo.service.UpdateDatabaseService;
import lombok.AllArgsConstructor;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/backend")
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private final QueryService queryService;

    private final OrderTransactionBrandCommerceService brandCommerceService;

    private final OrderTransactionShopeeService shopeeService;

    private final FileStorageServiceImpl fileStorageService;

    private final UpdateDatabaseService updateDatabaseService;

//    @RequestMapping(value = "/total-sold-item-each-province-each-brand", method = RequestMethod.GET,
//            produces = {"application/json"})
//    public ResponseEntity getTotalSoldItemEachProvinceEachBrand(
//            @RequestParam(name = "source-data", required = true) String sourceData) {
//        Pair<Map<String, Map<String, BigDecimal>>, Set<String>> result = queryService
//                .getTotalSoldItemEachProvinceEachBrand(sourceData);
//        Map<String, Map<String, BigDecimal>> map = result.getValue0();
//        Set<String> brands = result.getValue1();
//
//        if (map.size() == 0) {
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.ok(MappingToResponseHelper.toTotalSoldItemEachProvinceEachBrand(map, brands));
//    }

    @RequestMapping(value = "/total-sold-item-top-ten-province", method = RequestMethod.GET,
            produces = {"application/json"})
    public ResponseEntity getTotalSoldItemTopTenProvince(
            @RequestParam(name = "source-data", required = true) String sourceData) {
        List<TotalSoldItemEachProvinceDTO> result = queryService.getTotalSoldItemTopTenProvince(sourceData);

        if (result.size() == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(MappingToResponseHelper.toTotalSoldItemTopTenProvince(result));
    }

    @RequestMapping(value = "/total-sold-item-each-brand", method = RequestMethod.GET,
            produces = {"application/json"})
    public ResponseEntity getTotalSoldItemEachBrand(
            @RequestParam(name = "source-data", required = true) String sourceData) {
        List<TotalSoldItemEachBrandDTO> result = queryService.getTotalSoldItemEachBrand(sourceData);

        if (result.size() == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(MappingToResponseHelper.toTotalSoldItemEachBrand(result));
    }

    @RequestMapping(value = "/total-sold-item-each-month-each-brand", method = RequestMethod.GET,
            produces = {"application/json"})
    public ResponseEntity getTotalSoldItemEachMonthEachBrand(
            @RequestParam(name = "source-data", required = true) String sourceData,
            @RequestParam(name = "year", required = true) int year) {
        Pair<Map<Integer, Map<String, BigDecimal>>, Set<String>> result = queryService
                .getTotalSoldItemEachMonthEachBrand(sourceData, year);
        Map<Integer, Map<String, BigDecimal>> map = result.getValue0();
        Set<String> brands = result.getValue1();

        if (map.size() == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(MappingToResponseHelper.toTotalSoldItemEachMonthEachBrand(map, brands));
    }

    @RequestMapping(value = "/total-sold-item-each-year-each-brand", method = RequestMethod.GET,
            produces = {"application/json"})
    public ResponseEntity getTotalSoldItemEachYearEachBrand(
            @RequestParam(name = "source-data", required = true) String sourceData) {
        Pair<Map<String, Map<String, BigDecimal>>, Set<String>> result = queryService
                .getTotalSoldItemEachYearEachBrand(sourceData);
        Map<String, Map<String, BigDecimal>> map = result.getValue0();
        Set<String> brands = result.getValue1();

        if (map.size() == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(MappingToResponseHelper.toTotalSoldItemEachYearEachBrand(map, brands));
    }

//    @RequestMapping(value = "/kafka-send/{month}", method = RequestMethod.GET,
//            produces = {"application/json"})
//    public ResponseEntity sendDataToSpark(@PathVariable int month) throws Exception {
//        CompletableFuture<String> exec1 = brandCommerceService.sendOneMonth(month);
//        CompletableFuture<String> exec2 = shopeeService.sendOneMonth(month);
//        logger.info("CompletableFutures called.");
//        CompletableFuture.allOf(exec1, exec2).join();
//
//        logger.info(exec1.get());
//        logger.info(exec2.get());
//        return ResponseEntity.ok(200);
//    }

    @RequestMapping(value = "/update-new-data-and-predict/{month}", method = RequestMethod.GET,
            produces = {"application/json"})
    public ResponseEntity sendDataToSparkViaDB(@PathVariable int month) throws Exception {
//        updateDatabaseService.updateBrandCommerce(month);
//        updateDatabaseService.updateShopee(month);
        updateDatabaseService.sendData(month);
        return ResponseEntity.ok(200);
    }

    @RequestMapping(value = "/get-predict-result-1", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity getPredictResultOne() {
        List<GetPredictResultOneDTO> result = queryService.getPredictResultOne();

        if (result.size() == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(MappingToResponseHelper.toGetPredictResultOne(result));
    }

//    @RequestMapping(value = "/get-predict-result-1-table", method = RequestMethod.GET, produces = {"application/json"})
//    public ResponseEntity getPredictResultOneTable(@RequestParam int page, @RequestParam int limit) {
//        Pageable pageable = PageRequest.of(page, limit);
//        GetPredictResultOneTableDTO result = queryService.getPredictResultOneTable(pageable);
//
//        return ResponseEntity.ok(result);
//    }

    @RequestMapping(value = "/get-predict-result-2", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity getPredictResultTwo() {
        List<GetPredictResultTwoDTO> result = queryService.getPredictResultTwo();

        if (result.size() == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(MappingToResponseHelper.toGetPredictResultTwo(result));
    }

//    @RequestMapping(value = "/get-predict-result-2-table", method = RequestMethod.GET, produces = {"application/json"})
//    public ResponseEntity getPredictResultTwoTable(@RequestParam int page, @RequestParam int limit) {
//        Pageable pageable = PageRequest.of(page, limit);
//        GetPredictResultTwoTableDTO result = queryService.getPredictResultTwoTable(pageable);
//
//        return ResponseEntity.ok(result);
//    }

    @RequestMapping(value = "/get-predict-result-3", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity getPredictResultThree() {
        List<GetPredictResultThreeDTO> result = queryService.getPredictResultThree();

        if (result.size() == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(MappingToResponseHelper.toGetPredictResultThree(result));
    }

//    @RequestMapping(value = "/get-predict-result-3-table", method = RequestMethod.GET, produces = {"application/json"})
//    public ResponseEntity getPredictResultThreeTable(@RequestParam int page, @RequestParam int limit) {
//        Pageable pageable = PageRequest.of(page, limit);
//        GetPredictResultThreeTableDTO result = queryService.getPredictResultThreeTable(pageable);
//
//        return ResponseEntity.ok(result);
//    }

    @RequestMapping(value = "/get-predict-result-4", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity getPredictResultFour() {
        List<GetPredictResultFourDTO> result = queryService.getPredictResultFour();

        if (result.size() == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(MappingToResponseHelper.toGetPredictResultFour(result));
    }

//    @RequestMapping(value = "/get-predict-result-5", method = RequestMethod.GET, produces = {"application/json"})
//    public ResponseEntity getPredictResultFive() {
//        List<GetPredictResultFiveDTO> result = queryService.getPredictResultFive();
//
//        if (result.size() == 0) {
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.ok(MappingToResponseHelper.toGetPredictResultFive(result));
//    }

    @RequestMapping(value = "/get-month-year-prediction", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity getMonthYearPrediction() {
        GetMonthYearPredictionDTO result = queryService.getMonthYearPrediction();

        return ResponseEntity.ok(MappingToResponseHelper.toGetMonthYearPrediction(result));
    }

    @RequestMapping(value = "/upload-file", method = RequestMethod.POST)
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download-file/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @RequestMapping(value = "/upload-multiple-files", method = RequestMethod.POST)
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/download-file/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
