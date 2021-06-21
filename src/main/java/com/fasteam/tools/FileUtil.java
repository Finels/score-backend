package com.fasteam.tools;

import com.alibaba.fastjson.JSONObject;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2020/5/4.
 */
@Component
public class FileUtil {
    @Value("${fastdfs.nginx.url}")
    private String nginxUrl;
    @PostConstruct
    public void init() throws IOException, MyException {
        ClientGlobal.initByProperties("fastdfs-client.properties");
    }

    public String upload(MultipartFile file) throws IOException, MyException {
        byte[] fileBuff = file.getBytes();
        String tempFileName = file.getOriginalFilename();
        String fileExtName = tempFileName.substring(tempFileName.lastIndexOf("."));
        //建立连接
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getTrackerServer();
        StorageClient1 client = new StorageClient1(trackerServer);
        NameValuePair[] metaList = new NameValuePair[3];
        metaList[0] = new NameValuePair("fileName", tempFileName);
        metaList[1] = new NameValuePair("fileExtName", fileExtName);
        metaList[2] = new NameValuePair("fileLength", String.valueOf(file.getSize()));
        String fileId = client.upload_file1(fileBuff, fileExtName, metaList);
        return nginxUrl + fileId;
    }

    public void delete(String fileId) throws IOException, MyException {
        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getTrackerServer();
        StorageClient1 client = new StorageClient1(trackerServer);
        client.delete_file1(fileId);
    }

}
