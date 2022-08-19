# API Specifications

## Summary Current Data API

### Get Total Sold Item within Top Ten Provinces
- Purpose: Untuk mendapatkan total barang terjual pada 10 buah provinsi dengan total penjualan tertinggi.
- Endpoint: `/total-sold-item-top-ten-province?source-data={source-data}`
- HTTP Method: `GET`
- Path Variable Enum:
  - source-data: brand-commerce | shopee
- Response Body (Success):
```
{
  
}
```

### Get Total Sold Item Each Brand
- Purpose: Untuk mendapatkan total barang terjual pada setiap merek.
- Endpoint: `/total-sold-item-each-brand?source-data={source-data}`
- HTTP Method: `GET`
- Path Variable Enum:
    - source-data: brand-commerce | shopee
- Response Body (Success):
```
{
  
}
```

### Get Total Sold Item within Each Month and Each Brand
- Purpose: Untuk mendapatkan total barang terjual untuk setiap bulan dan setiap merek pada suatu tahun.
- Endpoint: `/total-sold-item-each-month-each-brand?source-data={source-data}&year={year}`
- HTTP Method: `GET`
- Path Variable Enum:
    - source-data: brand-commerce | shopee
    - year: 2020 | 2021 | 2022
- Response Body (Success):
```
{
  
}
```

### Get Total Sold Item within Each Year and Each Brand
- Purpose: Untuk mendapatkan total barang terjual untuk setiap tahun dan setiap merek.
- Endpoint: `/total-sold-item-each-year-each-brand?source-data={source-data}`
- HTTP Method: `GET`
- Path Variable Enum:
    - source-data: brand-commerce | shopee
- Response Body (Success):
```
{
  
}
```

## Prediction Result API

### Get Ratio of Total Potential User and Not Potential User
- Purpose: Untuk mendapatkan perbandingan jumlah pembeli potensial dan tidak potensial
- Endpoint: `/get-predict-result-1`
- HTTP Method: `GET`
- Response Body (Success):
```
{
  
}
```

### Get   
- Purpose: 
- Endpoint: `/get-predict-result-2`
- HTTP Method: `GET`
- Response Body (Success):
```
{
  
}
```

### Get
- Purpose:
- Endpoint: `/get-predict-result-3`
- HTTP Method: `GET`
- Response Body (Success):
```
{
  
}
```

### Get
- Purpose:
- Endpoint: `/get-predict-result-4`
- HTTP Method: `GET`
- Response Body (Success):
```
{
  
}
```

### Get
- Purpose:
- Endpoint: `/update-new-data-and-predict/{month}`
- HTTP Method: `GET`
- Path Variable Enum:
  - month: 2 | 3
- Response Body (Success):
```
{
  
}
```