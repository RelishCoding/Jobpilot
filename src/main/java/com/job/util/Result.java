package com.job.util;

import com.job.constant.MessageConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {
    private int code = HttpStatus.OK.value();
    private String msg = MessageConstants.REQUEST_SUCCESS;
    private Object data;

    public static Result success() {
        return new Result();
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.data = data;
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.code = HttpStatus.BAD_REQUEST.value();
        result.msg = msg;
        return result;
    }

    public static Result fail(int code, String msg) {
        Result result = new Result();
        result.code = code;
        result.msg = msg;
        return result;
    }
}
