package com.xhs.qa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * Created on 18/3/12 15:18
 *
 * @author sunyumei
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {
    private Boolean success;
    private T data;
    private String err_msg;

    public static <T> CommonResponse<T> success(T t) {
        return new CommonResponse<>(true, t, null);
    }
    public static <T> CommonResponse<T> fail(String errorMessage) {
        return new CommonResponse<>(false, null, errorMessage);
    }
}
