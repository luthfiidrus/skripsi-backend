package com.example.demo.service;

import com.example.demo.model.dto.OrderTransactionBrandCommerceDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderTransactionBrandCommerceServiceImpl implements OrderTransactionBrandCommerceService {

    private static final Logger logger = LoggerFactory.getLogger(OrderTransactionBrandCommerceServiceImpl.class);

//    @Autowired
//    private OrderTransactionBrandCommerceRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper mapper;

//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;

    private List<OrderTransactionBrandCommerceDTO> findOneMonth(int month) {
//        return repository.findOneMonth();
        String sql = String.format("select * from brand_commerce.order_transaction_bc_luthfi where extract(month from date_order) = %d", month);
        List<OrderTransactionBrandCommerceDTO> result = new ArrayList<>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            public void processRow(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    result.add(OrderTransactionBrandCommerceDTO.builder()
                            .brand(resultSet.getString("brand"))
                            .orderId(BigInteger.valueOf(resultSet.getInt("order_id")))
                            .orderStatus(resultSet.getString("order_status"))
                            .dateOrder(resultSet.getString("date_order"))
                            .customerId(resultSet.getString("customer_id"))
                            .paymentMethod(resultSet.getString("payment_method"))
                            .dateCompleted(resultSet.getString("date_completed"))
                            .datePaid(resultSet.getString("date_paid"))
                            .dateInvoice(resultSet.getString("date_invoice"))
                            .noInvoice(resultSet.getString("no_invoice"))
                            .orderCurrency(resultSet.getString("order_currency"))
                            .orderSubtotal(resultSet.getBigDecimal("order_subtotal"))
                            .cartDiscount(resultSet.getBigDecimal("cart_discount"))
                            .redeemedPoint(resultSet.getBigDecimal("redeemed_point"))
                            .orderShipping(resultSet.getBigDecimal("order_shipping"))
                            .orderTotal(resultSet.getBigDecimal("order_total"))
                            .couponUsage(resultSet.getString("coupon_usage"))
                            .orderStockReduced(resultSet.getString("order_stock_reduced"))
                            .xenditInvoice(resultSet.getString("xendit_invoice"))
                            .version(resultSet.getString("version"))
                            .writeDate(resultSet.getString("write_date"))
                            .build());
                }
            }
        });
        return result;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<String> sendOneMonth(int month) {
//        logger.info("Sending brand commerce data");
        logger.info("Start querying to db BC");
        List<OrderTransactionBrandCommerceDTO> oneMonthTransaction = this.findOneMonth(month);
        logger.info("End querying to db BC");
//        try {
//            for (OrderTransactionBrandCommerceDTO transaction: oneMonthTransaction) {
//                kafkaTemplate.send("com.xyz.bc.transfer", mapper.writeValueAsString(transaction));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        logger.info("EOF BC");
        return CompletableFuture.completedFuture("Brand commerce data sent");
    }
}
