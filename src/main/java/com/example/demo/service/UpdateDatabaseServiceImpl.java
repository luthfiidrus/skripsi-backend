package com.example.demo.service;

import com.example.demo.model.dto.OrderTransactionBrandCommerceDTO;
import com.example.demo.model.dto.OrderTransactionShopeeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UpdateDatabaseServiceImpl implements UpdateDatabaseService{

    private static final Logger logger = LoggerFactory.getLogger(UpdateDatabaseServiceImpl.class);

    private final JdbcTemplate jdbcTemplate;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//    private final DateTimeFormatter formatterWithMilisecond = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

    private final DateTimeFormatter formatterWithMilisecond = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral(' ')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .toFormatter();

    public UpdateDatabaseServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    @Override
    private void updateBrandCommerce(int month) {
        logger.info("Get data BC from db luthfi");
        String selectSql = String.format("select * from brand_commerce.order_transaction_bc_luthfi where extract(month from date_order) = %d and extract(year from date_order) = 2022", month);
        List<OrderTransactionBrandCommerceDTO> transactionBrandCommerceDTOS = jdbcTemplate.query(selectSql, BeanPropertyRowMapper.newInstance(OrderTransactionBrandCommerceDTO.class));
        logger.info("Done get data BC from db luthfi");

        logger.info("Start insert data BC to db naufal");
        String insertSql = "INSERT INTO brand_commerce.order_transaction_bc_naufal (brand, order_id, order_status, date_order, customer_id, payment_method, date_completed, date_paid, date_invoice, no_invoice, order_currency, order_subtotal, cart_discount, redeemed_point, order_shipping, order_total, coupon_usage, order_stock_reduced, xendit_invoice, version, write_date) \n" +
                "  VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> batchArgsList = new ArrayList<>();

        for (OrderTransactionBrandCommerceDTO dto: transactionBrandCommerceDTOS) {
            Object[] objectArray = {
                    dto.getBrand(),
                    dto.getOrderId(),
                    dto.getOrderStatus(),
                    LocalDateTime.parse(dto.getDateOrder(), formatter),
//                    dto.getDateOrder(),
                    dto.getCustomerId(),
                    dto.getPaymentMethod(),
                    dto.getDateCompleted() == null ? null : LocalDateTime.parse(dto.getDateCompleted(), formatter),
//                    dto.getDateCompleted(),
                    dto.getDatePaid() == null ? null : LocalDateTime.parse(dto.getDatePaid(), formatter),
//                    dto.getDatePaid(),
                    dto.getDateInvoice() == null ? null : LocalDateTime.parse(dto.getDateInvoice(), formatter),
//                    dto.getDateInvoice(),
                    dto.getNoInvoice(),
                    dto.getOrderCurrency(),
                    dto.getOrderSubtotal(),
                    dto.getCartDiscount(),
                    dto.getRedeemedPoint(),
                    dto.getOrderShipping(),
                    dto.getOrderTotal(),
                    dto.getCouponUsage(),
                    dto.getOrderStockReduced(),
                    dto.getXenditInvoice(),
                    dto.getVersion(),
                    dto.getWriteDate() == null ? null : LocalDateTime.parse(dto.getWriteDate(), formatterWithMilisecond)
//                    dto.getWriteDate()
            };
            batchArgsList.add(objectArray);
        }

        jdbcTemplate.batchUpdate(insertSql, batchArgsList);
        logger.info("Done insert data BC to db naufal");
    }

//    @Override
    private void updateShopee(int month) {
        logger.info("Get data Shopee from db luthfi");
        String selectSql = String.format("select * from shopee.order_transaction_shopee_luthfi where extract(month from create_time) = %d and extract(year from create_time) = 2022", month);
        List<OrderTransactionShopeeDTO> transactionShopeeDTOS = jdbcTemplate.query(selectSql, BeanPropertyRowMapper.newInstance(OrderTransactionShopeeDTO.class));
        logger.info("Done get data Shopee from db luthfi");

        logger.info("Start insert data Shopee to db naufal");
        String insertSql = "INSERT INTO shopee.order_transaction_shopee_naufal (ordersn, order_status, shopid, create_time, pay_time, update_time, buyer_username, recipient_name, recipient_phone, recipient_address, recipient_district, recipient_city, recipient_province, timestamp, estimated_shipping_fee) \n" +
                "  VALUES  (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> batchArgsList = new ArrayList<>();

        for (OrderTransactionShopeeDTO dto: transactionShopeeDTOS) {
            Object[] objectArray = {
                    dto.getOrdersn(),
                    dto.getOrderStatus(),
                    dto.getShopid(),
                    dto.getCreateTime() == null ? null : LocalDateTime.parse(dto.getCreateTime(), formatter),
//                    dto.getCreateTime(),
                    dto.getPayTime() == null ? null : LocalDateTime.parse(dto.getPayTime(), formatter),
//                    dto.getPayTime(),
                    dto.getUpdateTime() == null ? null : LocalDateTime.parse(dto.getUpdateTime(), formatter),
//                    dto.getUpdateTime(),
                    dto.getBuyerUsername(),
                    dto.getRecipientName(),
                    dto.getRecipientPhone(),
                    dto.getRecipientAddress(),
                    dto.getRecipientDistrict(),
                    dto.getRecipientCity(),
                    dto.getRecipientProvince(),
                    dto.getTimestamp() == null ? null : LocalDateTime.parse(dto.getTimestamp(), formatterWithMilisecond),
//                    dto.getTimestamp(),
                    dto.getEstimatedShippingFee()
            };
            batchArgsList.add(objectArray);
        }

        jdbcTemplate.batchUpdate(insertSql, batchArgsList);
        logger.info("Done insert data Shopee to db naufal");
    }

    @Override
    public void sendData(int month) {
        this.updateBrandCommerce(month);
        this.updateShopee(month);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(this::triggerFlask);
    }

    private String triggerFlask() {
        try {
            logger.info("Initializing API client");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://34.82.223.138:16202/"))
//                    .uri(URI.create("http://postman-echo.com/bytes/5/mb?type=json"))
                    .GET()
                    .build();

            logger.info("Start sending through API");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            logger.info(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Triggering API done";
    }
}
