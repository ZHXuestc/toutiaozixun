package com.nowcoder.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by nowcoder on 2016/7/3.
 */
public class ToutiaoUtil {
    //Logger 对象用来记录特定系统或应用程序组件的日志消息,用来记录ToutiaoUtils类的日志信息
    private static final Logger logger = LoggerFactory.getLogger(ToutiaoUtil.class);

    public static String TOUTIAO_DOMAIN = "http://127.0.0.1:8080/";
    //文件上传路径
    public static String IMAGE_DIR = "E:/upload/";
    public static String[] IMAGE_FILE_EXTD = new String[] {"png", "bmp", "jpg", "jpeg"};
    //判断文件是否可以上传
    public static boolean isFileAllowed(String fileExt){
        //如果传递过来的扩展名和数组中的匹配则返回true
            for(String ext : IMAGE_FILE_EXTD){
                if(ext.equals(fileExt)){
                    return true;
                }
            }
        return false;
    }

    //验证服务器返回前端有没有成功,0代表成功1代表失败
    //调用该方法可以产生json串在页面中显示
    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        //转成json串
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        //转成json串
        return json.toJSONString();
    }

    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        //转成json串
        return json.toJSONString();
    }

    //使用MD5进行加密的方法
    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }
}
