package com.winsum.chatliu.dto;

import com.winsum.chatliu.exception.CustomizeErrorCode;
import com.winsum.chatliu.exception.CustomizeException;
import lombok.Data;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Data
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;


    public static ResultDTO errorOf(Integer code, String message){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO errorOf(CustomizeException e){
        return errorOf(e.getCode(),e.getMessage());
    }


    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO okOf(){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    //非静态时：
 /*   public ResultDTO okOf(T t){
        ResultDTO<T> resultDTO = new ResultDTO<T>();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }*/

 //泛型函数
    public static <T> ResultDTO okOf(T t){
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }

}
