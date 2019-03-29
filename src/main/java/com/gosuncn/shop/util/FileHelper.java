package com.gosuncn.shop.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: chenxihua
 * @Date: 2018-12-24:16:34
 *
 * 这个类用来帮助图片上传
 */
@Slf4j
public class FileHelper {

    private static String bucketName = "springbootshop-1256969743";
    private static String region = "ap-guangzhou";
    // 使用第一个
    private static COSCredentials cosCredentials = null;
    private static TransferManager transferManager = null;
    private static COSClient cosClient = null;

    static{
        // 1 初始化用户身份信息(secretId, secretKey)
        String SecretId = "AKID0mOmvqf5LL6mmciYfZEe07zbx4wYsDH5";
        String SecretKey = "h9NN8d4HEvMZWDf4EE2f4FBrCI9qJXWL";
        cosCredentials = new BasicCOSCredentials(SecretId, SecretKey);
        // 2 设置bucket的区域
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 3 生成cos客户端
        cosClient = new COSClient(cosCredentials, clientConfig);


        // 开启线程
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        // 指定要上传到 COS 上的路径
        transferManager = new TransferManager(cosClient, threadPool);
    }


    /**
     * @param localFile
     * @return
     */
    public static URL uploads(File localFile){

        URL url = null;
        String key = "/shop/"+System.currentTimeMillis()+".png";
        try {
            // 本地文件上传到COS
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            //
            Upload upload = transferManager.upload(putObjectRequest);
            upload.waitForCompletion();
            log.info("上传成功");
            //获取上传成功之后文件的下载地址
            url = cosClient.generatePresignedUrl(bucketName, key, new Date(System.currentTimeMillis()+5*60*10000));
            return url;
        } catch (Throwable e) {
            log.info("上传失败。。。");
            e.printStackTrace();
        } finally {
            // 关闭
//            transferManager.shutdownNow();
            cosClient.shutdown();
        }
        return url;
    }

}
