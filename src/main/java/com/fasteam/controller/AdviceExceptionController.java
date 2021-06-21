package com.fasteam.controller;

import com.fasteam.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Description:  com.fasteam.controller
 * Copyright: © 2020 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2020/5/7
 */
@ControllerAdvice
@Slf4j
public class AdviceExceptionController {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public RestResponse customException(Exception e) {
        log.error("Error", e);
        return RestResponse.error(e.getMessage());
    }
}
