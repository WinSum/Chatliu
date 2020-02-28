package com.winsum.chatliu.advice;

import com.winsum.chatliu.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 捕捉异常
 */
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable ex , Model model){

        if (ex instanceof CustomizeException){
            model.addAttribute("message", ex.getMessage());
        }else{
            model.addAttribute("message","服务冒烟了!!!");
        }
        return new ModelAndView("error");
    }
}
