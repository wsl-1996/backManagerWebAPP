package com.skqtec.timertask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.skqtec.entity.TrackEntity;
import com.skqtec.repository.TrackRepository;
import com.skqtec.tools.KdniaoTrackQueryAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class updateExpressage {
    @Autowired
    private TrackRepository trackRepository;
    //@Scheduled(cron = "0/1 0 * * * ? ")
    @Scheduled(cron = "0 * * * * ?")
    public void run(){
        try{
            KdniaoTrackQueryAPI api = new KdniaoTrackQueryAPI();
            List<TrackEntity>list=trackRepository.query("0");
            for(TrackEntity track:list) {
                //String expCode=KdniaoTrackQueryAPI.getCompanyCode(track.getTrackName());
                String expNo=track.getTrackNumber();
                String result = api.getOrderTracesByJson(track.getTrackCode(), expNo);
                JSONObject jsonObject=JSON.parseObject(result);
                JSONArray traces=(JSONArray)jsonObject.get("Traces");
                track.setTrack(traces.toString());
                trackRepository.saveOrUpdate(track);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
