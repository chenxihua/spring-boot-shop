package com.gosuncn.shop;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * @author: chenxihua
 * @Date: 2018-12-28:15:07
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BucketsTest {

    @Test
    public void testBuckets01(){
        try {
            // bucket 的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            COSClient cosClient = new COSClient(new BasicCOSCredentials("AKID0mOmvqf5LL6mmciYfZEe07zbx4wYsDH5", "h9NN8d4HEvMZWDf4EE2f4FBrCI9qJXWL"), new ClientConfig(new Region("ap-guangzhou")));
            List<Bucket> bucketList = cosClient.listBuckets();
            for (Bucket bucket : bucketList) {
                log.info("---> : " + bucket.getName());
                log.info("---> : " + bucket.getLocation());
                log.info("---> : " + bucket.getCreationDate());
                log.info("---> : " + bucket.getOwner());
            }
        } catch (CosClientException cle) {
            // 自定义异常处理比如打印异常等
            log.error("del object failed.", cle);
            // ...
        }

    }

    @Test
    public void testBuckets02(){
        try {
            // bucket 的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            COSClient cosClient = new COSClient(new BasicCOSCredentials("AKID0mOmvqf5LL6mmciYfZEe07zbx4wYsDH5", "h9NN8d4HEvMZWDf4EE2f4FBrCI9qJXWL"), new ClientConfig(new Region("ap-guangzhou")));
            // 桶名：
            String bucketName = "springbootshop-1256969743";
            // 获取 bucket 下成员
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            listObjectsRequest.setBucketName(bucketName);
            listObjectsRequest.setPrefix("");
            listObjectsRequest.setDelimiter("/");
            listObjectsRequest.setMarker("");
            listObjectsRequest.setMaxKeys(100);

            // 获取文件列表
            ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);
            // 获取下次 list 的 marker
            String nextMarker = objectListing.getNextMarker();
            // 判断是否已经 list 完, 如果 list 结束, 则 isTruncated 为 false, 否则为 true
            boolean truncated = objectListing.isTruncated();
            List<COSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary objectSummary : objectSummaries) {
                log.info("---> 获取文件路径： " + objectSummary.getKey());
                log.info("-- > 获取文件长度： " + objectSummary.getSize());
                log.info("-- > 获取文件ETag： " + objectSummary.getETag());
                log.info("-- > 获取最后修改时间： " + objectSummary.getLastModified());
                log.info("-- > 获取文件存储类型： " + objectSummary.getStorageClass());
            }

        } catch (CosClientException cle) {
            // 自定义异常处理比如打印异常等
            log.error("del object failed.", cle);
            // ...
        }
    }


    @Test
    public void testBuckets03(){
        try {
            // bucket 的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            COSClient cosClient = new COSClient(new BasicCOSCredentials("AKID0mOmvqf5LL6mmciYfZEe07zbx4wYsDH5", "h9NN8d4HEvMZWDf4EE2f4FBrCI9qJXWL"), new ClientConfig(new Region("ap-guangzhou")));
            // 桶名：
            String bucketName = "springbootshop-1256969743";
            String key = "shop/1545978125174.png";
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
            Date dataExc = new Date(System.currentTimeMillis()+30L*60L*1000L);
            request.setExpiration(dataExc);
            URL downUrl = cosClient.generatePresignedUrl(request);
            String downStrUrl = downUrl.toString();
            log.info("---> : " + downStrUrl);

        } catch (CosClientException cle) {
            // 自定义异常处理比如打印异常等
            log.error("del object failed.", cle);
            // ...
        }

    }


    @Test
    public void testStringSplit(){
        String strUrl = "15645641.png";
        String[] splits = strUrl.split("\\.");
        log.info("---> : " + splits[1]);
    }
}
