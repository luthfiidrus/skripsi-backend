package com.example.demo.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponse<T> {

    private int status;

    private T data;

}
