package com.gosuncn.shop.controller;

import com.gosuncn.shop.entities.User;
import com.gosuncn.shop.util.FileHelper;
import com.gosuncn.shop.util.SendEmailUtil;
import com.gosuncn.shop.util.UpHelper;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: chenxihua
 * @Date: 2018-12-18:11:07
 * 这个类，用来上传图片，以及发送验证码到邮箱
 */

@Slf4j
@Controller
@RequestMapping("/upload")
public class UploadController {


    /**
     * 图片及文件上传
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/goodsPic")
    public Map<String, Object> uploadPictures(MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();

        // 以下是救了我命的输出数据，就是看了这数据，我才做出这个简单的功能
//        log.info("---> 1: "+file.getOriginalFilename());
//        log.info("---> 2: "+file.getName());
//        log.info("---> 3: "+file.getContentType());
//        log.info("---> 4: "+file.getBytes());
//        log.info("---> 5: "+file.getSize());
//        log.info("---> 6: "+file.getInputStream());
//        log.info("---> 7: "+file.getResource());
//        log.info("---> 8: "+file.getInputStream().getClass());
        try {
            // 文件后缀名
            byte[] bytes = file.getBytes();
            if (bytes.length>=10485760){
                map.put("code", 101);
                map.put("msg", "上传附件错误，最大支持10M文件");
                return map;
            }
            long length = bytes.length;
            // 获取文件输入流
            InputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            String[] splits = file.getOriginalFilename().split("\\.");
            String suffixName = splits[1];

            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
            objectMetadata.setContentLength(length);


            String stringUrl = UpHelper.loadStringUrl(byteArrayInputStream, suffixName, objectMetadata);

            map.put("code", 0);
            map.put("msg", "上传图片成功");
            map.put("data", stringUrl);
            return map;
        }catch (Exception e){
            e.printStackTrace();
            map.put("code", 500);
            map.put("msg", "上传图片成功");
            map.put("data", null);
        }
        return map;
    }


    /**
     * 发送邮箱校验码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/verifyCode")
    public Map<String, Object> sendVerifyCode(HttpSession session, @RequestParam("eVal")String emailVal){
        Map<String, Object> map = new HashMap<>();

        Integer num = (int)((Math.random()*9+1)*100000);
        String numCode = String.valueOf(num);

        User currentUser = (User)session.getAttribute("currentUser");
        String reEmail = currentUser.getEmail();

        if (reEmail.equalsIgnoreCase(emailVal)){
            try {
                boolean emailCode = SendEmailUtil.sendEmailCode(emailVal, numCode);
                map.put("code", 0);
                map.put("success", true);
                map.put("data", numCode);
                map.put("msg", "发送验证码成功，请及时输入验证码");
            } catch (MessagingException e) {
                e.printStackTrace();
                map.put("code", 500);
                map.put("success", false);
                map.put("msg", "输入邮箱错误，与注册邮箱不符合");
            }
        }else{
            map.put("code", 1);
            map.put("success", false);
            map.put("msg", "输入邮箱错误，与注册邮箱不符合");
        }
        return map;
    }




}
