package com.skqtec.tools;

public class Base64ToolsTest {
    @org.junit.Test
    public void testJdkBase64Encode() {
        System.out.println(Base64Tool.jdkBase64Encode("hese1232"));
    }

    @org.junit.Test
    public void testJdkBase64Decode() {
        System.out.println(Base64Tool.jdkBase64Decode("aGVzZTEyMzI="));
    }
}
