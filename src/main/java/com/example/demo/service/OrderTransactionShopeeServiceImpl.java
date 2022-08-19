package com.example.demo.service;

import com.example.demo.model.dto.OrderTransactionShopeeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderTransactionShopeeServiceImpl implements OrderTransactionShopeeService {

    private static final Logger logger = LoggerFactory.getLogger(OrderTransactionShopeeServiceImpl.class);

//    @Autowired
//    private OrderTransactionShopeeRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper mapper;

//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;

    private List<OrderTransactionShopeeDTO> findOneMonth(int month) {
//        return repository.findOneMonth();
        String sql = String.format("select * from shopee.order_transaction_shopee_luthfi where extract(month from create_time) = %d", month);
        List<OrderTransactionShopeeDTO> result = new ArrayList<>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            public void processRow(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    result.add(OrderTransactionShopeeDTO.builder()
                            .ordersn(resultSet.getString("ordersn"))
                            .orderStatus(resultSet.getString("order_status"))
                            .shopid(resultSet.getInt("shopid"))
                            .createTime(resultSet.getString("create_time"))
                            .payTime(resultSet.getString("pay_time"))
                            .updateTime(resultSet.getString("update_time"))
                            .buyerUsername(resultSet.getString("buyer_username"))
                            .recipientName(resultSet.getString("recipient_name"))
                            .recipientPhone(resultSet.getString("recipient_phone"))
                            .recipientAddress(resultSet.getString("recipient_address"))
                            .recipientDistrict(resultSet.getString("recipient_district"))
                            .recipientCity(resultSet.getString("recipient_city"))
                            .recipientProvince(resultSet.getString("recipient_province"))
                            .timestamp(resultSet.getString("timestamp"))
                            .estimatedShippingFee(resultSet.getBigDecimal("estimated_shipping_fee"))
                            .build());
                }
            }
        });
        return result;
    }

    @Override
    @Async("threadPoolTaskExecutor")
    public CompletableFuture<String> sendOneMonth(int month) {
//        logger.info("Sending shopee data");
        logger.info("Start querying to db Shopee");
        List<OrderTransactionShopeeDTO> oneMonthTransaction = this.findOneMonth(month);
        logger.info("End querying to db Shopee");
//        try {
//            for (OrderTransactionShopeeDTO transaction: oneMonthTransaction) {
//                kafkaTemplate.send("com.xyz.shopee.transfer", mapper.writeValueAsString(transaction));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        logger.info("EOF Shopee");
        return CompletableFuture.completedFuture("Shopee data sent");
    }
}
