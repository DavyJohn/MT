package com.zzh.mt.utils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static com.zzh.mt.utils.Contants.secretKey;

/**
 * Created by 腾翔信息 on 2017/6/21.
 */

public class MdTools {
    public static String sign_digest(LinkedHashMap<String,String> map){
        Set<String> keyset = map.keySet();
        List<String> keylist = new ArrayList<String>(keyset);
        Collections.sort(keylist);
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<keylist.size();i++){
            String key = keylist.get(i);
            String value = map.get(key);
            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }
        sb.append("secret").append("=").append(secretKey);
        String sign_string = sb.toString();
        return  getHash(sign_string,"MD5");
    }
    private static String getHash(String source, String hashType) {
        StringBuilder sb = new StringBuilder();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance(hashType);
            md5.update(source.getBytes("UTF-8"));
            for (byte b : md5.digest()) {
                sb.append(String.format("%02X", b)); // 10进制转16进制，X 表示以十六进制形式输出，02 表示不足两位前面补0输出
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
