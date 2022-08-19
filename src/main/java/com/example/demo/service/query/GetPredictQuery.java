package com.example.demo.service.query;

public interface GetPredictQuery {
    String getPredictResultOne = "select potensial_status as keterangan, count(potensial_status) as total from public.hasil_task_1 group by potensial_status";
//    String getPredictResultOneTable = "select customer_id, keterangan from public.hasil_task_1 offset %d limit %d";
//    String getPredictResultOneCount = "select count(*) from public.hasil_task_1";
    String getPredictResultTwo = "select item_prediksi as prediksi_item, count(item_prediksi) as total from public.hasil_task_2 group by item_prediksi order by count(item_prediksi) desc limit 10";
//    String getPredictResultTwoTable = "select customer_id, prediksi_item from public.hasil_task_2 offset %d limit %d";
//    String getPredictResultTwoCount = "select count(*) from public.hasil_task_2";
    String getPredictResultThree = "select next_time_buy as keterangan, count(next_time_buy) as total from public.hasil_task_3 group by next_time_buy, next_time_buy_code order by next_time_buy_code";
//    String getPredictResultThreeTable = "select customer_id, keterangan from public.hasil_task_3 offset %d limit %d";
//    String getPredictResultThreeCount = "select count(*) from public.hasil_task_3";
    String getPredictResultFour = "select provinsi, count(potensial_status) as total from public.hasil_task_1  group by provinsi order by count(potensial_status) desc limit 10";
    String getPredictResultFive = "select revenue, count(potensial_status) as total from public.hasil_task_1 where potensial_status = \"potensial_status\" group by revenue order by revenue";
    String getMonthYearPrediction = "select extract(month from max(date_order)) as bulan, extract(year from max(date_order)) as tahun from brand_commerce.order_transaction_bc_naufal";
}
