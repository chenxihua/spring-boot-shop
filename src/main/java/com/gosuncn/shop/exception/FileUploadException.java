package com.gosuncn.shop.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @Author: chenxihua
 * @Date: 2019/3/14:17:41
 * @Version 1.0
 **/

@Slf4j
@ControllerAdvice
public class FileUploadException {

    @ExceptionHandler(MultipartException.class)
    public String handleError1(MultipartException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        log.info("---》 ： 文件上传失败， 来到这里");
        return null;
//        return "redirect:/uploadStatus";
    }

}
