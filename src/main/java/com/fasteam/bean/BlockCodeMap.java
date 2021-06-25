package com.fasteam.bean;

import java.util.*;

/**
 * Description:  传递的codeMap编码表，分片文件上传需要先接收编码表
 * 将序号编码得到的base64字符串
 * <p>
 * Copyright: © 2021 Foxconn. All rights reserved.
 * Company: Foxconn
 *
 * @author FL
 * @version 1.0
 * @timestamp 2021/6/24
 */
public class BlockCodeMap {
    private String userId;
    private String md5;
    private List roadMap;

    public void setRoadMap(List<String> roadMap) {
        if (roadMap == null) {
            //roadMap不可重复设置
            //roadMap是顺序不可靠的，需要解码后重新排序
            roadMap.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    Integer v1 = Integer.parseInt(new String(Base64.getDecoder().decode(o1)));
                    Integer v2 = Integer.parseInt(new String(Base64.getDecoder().decode(o2)));
                    return v1 > v2 ? 1 : 0;
                }
            });
            this.roadMap = roadMap;
        }
    }

    public List getRoadMap() {
        return roadMap;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
