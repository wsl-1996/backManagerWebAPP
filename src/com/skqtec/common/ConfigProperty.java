package com.skqtec.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigProperty {

    @Value("${host}")
    private String host;

    @Value("${tempPathFile}")
    private String tempPathFile;

    @Value("${imgUrlBase}")
    private String imgUrlBase;

    public String getHost(){
        return host;
    }

    public String getTempPathFile(){
        return tempPathFile;
    }

    public String getImgUrlBase(){
        return imgUrlBase;
    }
}
