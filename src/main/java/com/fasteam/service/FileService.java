package com.fasteam.service;

import com.fasteam.bean.BlockCodeMap;
import com.fasteam.bean.Namespace;
import com.fasteam.tools.AuthUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Description: 1.接收编码表。 2.分片接收文件并保存在本地。 3.根据编码表顺序循环合并文件
 * Copyright: © 2021 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2021/6/21
 */
@Service
public class FileService {

    private ThreadLocal<Namespace> currentNamespace = new ThreadLocal<>();

    @Autowired
    private NamespaceManager namespaceManager;
    @Autowired
    private CacheService cacheService;

    /**
     * 上传文件分块之前先开辟临时用户空间
     */
    public void beforeUploadBlock(String userId) {
        Namespace namespace = namespaceManager.getOrApplyNamespace(userId);
        currentNamespace.set(namespace);
    }

    public String acceptBlockCode(String sessionId, BlockCodeMap codeMap) {
        //缓存编码表，key是sessionId，一定不能重复
        cacheService.putBlockCode(sessionId, codeMap);
        return sessionId;
    }

    /**
     * 接收文件单个块的方法
     */
    public void fileBlockAccept(String sessionId, String userId, MultipartFile multipartFile) throws IOException {
        Namespace namespace = namespaceManager.getNamespace(userId);
        InputStream inputStream = multipartFile.getInputStream();
        File tempBlockFile = new File(namespace.getRootRoom());
        FileOutputStream out = new FileOutputStream(tempBlockFile);
        IOUtils.copy(inputStream, out);
    }
}
