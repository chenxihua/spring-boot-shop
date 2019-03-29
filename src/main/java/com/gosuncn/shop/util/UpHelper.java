package com.gosuncn.shop.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @author: chenxihua
 * @Date: 2018-12-29:8:55
 */
@Slf4j
public class UpHelper {


    /**
     * 上传文件或者图片
     * @param inputStream
     * @param strSuffix
     * @param objectMetadata
     * @return
     */
    public static String loadStringUrl(InputStream inputStream, String strSuffix, ObjectMetadata objectMetadata){
        // 桶名：
        String bucketName = "springbootshop-1256969743";
        String upName = System.currentTimeMillis() + "." + strSuffix;
        String key = "/shop/"+ upName;
        String photoUrl = null;
        try {
            // bucket 的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            COSClient cosClient = new COSClient(new BasicCOSCredentials("AKID0mOmvqf5LL6mmciYfZEe07zbx4wYsDH5", "h9NN8d4HEvMZWDf4EE2f4FBrCI9qJXWL"), new ClientConfig(new Region("ap-guangzhou")));

            PutObjectResult objectResult = cosClient.putObject(bucketName, key, inputStream, objectMetadata);

            photoUrl = "https://springbootshop-1256969743.cos.ap-guangzhou.myqcloud.com/shop/" + upName;

            // 关闭客户端(关闭后台线程)
            cosClient.shutdown();

        } catch (CosClientException cle) {
            // 自定义异常处理比如打印异常等
            log.error("上传文件失败，原因是：", cle);
        }
        return photoUrl;
    }

    /**
     * 下载附件
     * @param strUrl
     * @return
     */
    public static void downloadUrl(String strUrl){
        // https://springbootshop-1256969743.cos.ap-guangzhou.myqcloud.com/shop/1551856140899.txt
        String[] splits = strUrl.split("/");
        String fileName = splits[splits.length-1];

        String bucketName = "springbootshop-1256969743";
        String key = "/shop/"+fileName;
        File newLoad = null;

        try {
            String sourceName = "C:\\chenxihua\\";
            File file = new File(sourceName);
            if (!file.exists()){
                file.mkdirs();
            }
            String[] list = file.list();
            if (list.length>0){
                for (int i = 0; i <list.length ; i++) {
                    if (fileName.equals(list[i])){
                        File fileCopy = null;
                        fileCopy = new File(sourceName+fileName);
                        if (fileCopy.isFile()){
                            fileCopy.delete();
                            log.info("删除完成，相同了...");
                        }
                    }
                    newLoad = new File(sourceName+fileName);
                    try {
                        newLoad.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error("1：文件创建失败，"+e);
                    }
                }
            }else{
                newLoad = new File(sourceName+fileName);
                try {
                    newLoad.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("2：文件创建失败，"+e);
                }
            }

            // bucket 的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            COSClient cosClient = new COSClient(new BasicCOSCredentials("AKID0mOmvqf5LL6mmciYfZEe07zbx4wYsDH5", "h9NN8d4HEvMZWDf4EE2f4FBrCI9qJXWL"), new ClientConfig(new Region("ap-guangzhou")));

            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, newLoad);

            // 关闭流
            cosClient.shutdown();

        }catch (CosClientException e){
            e.printStackTrace();
            log.error("下载附件失败，原因是："+e);
        }
    }

}
