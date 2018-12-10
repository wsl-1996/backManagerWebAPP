package com.skqtec.tools;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public class Base64Tool {

    public static String jdkBase64Encode(String src) {
        //加密：
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(src.getBytes());
        System.out.println("encode : " + encode);
        return encode;
    }

    public static String jdkBase64Decode(String src){
        //解密
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            String decode = new String(decoder.decodeBuffer(src));
            System.out.println("decode : " + decode);
            return decode;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
