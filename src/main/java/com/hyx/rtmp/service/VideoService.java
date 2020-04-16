package com.hyx.rtmp.service;

import com.hyx.rtmp.common.utils.DateUtils;
import com.hyx.rtmp.common.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
public class VideoService {

    @Value("${video.timeOut}")
    private Integer timeOut = 5;

    @Value("${video.checkPeriod}")
    private Integer checkPeriod = 5;

    @Value("${video.server}")
    private String server = "127.0.0.1:1935";


    // private static final String rtspPath = "rtsp://admin:Buu#2019@192.168.8.210:554/h264/ch1/main/av_stream";
    // private static final String rtmpPath = "rtmp://192.168.8.202:1935/live/room";


    public static ConcurrentHashMap<String, Date> LIVE_VIDEO_MAP = new ConcurrentHashMap<String, Date>();
    private static ScheduledExecutorService stopVideoExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ConcurrentHashMap<String, Thread> THREAD_MAP = new ConcurrentHashMap<String, Thread>();

    /**
     * 启动视频转换
     * @return rtmpUrl
     */
    public ResponseUtils start(String rtspUrl, String rtmpUrl) {
        if (null == LIVE_VIDEO_MAP.get(rtspUrl)) {
            if(null != THREAD_MAP.get(rtspUrl)){
                THREAD_MAP.get(rtspUrl).stop();
            }
            Thread t = new VideoThread(rtspUrl, rtmpUrl);
            t.start();
            THREAD_MAP.put(rtspUrl, t);
        } 
        LIVE_VIDEO_MAP.put(rtspUrl, new Date());
        return ResponseUtils.getSuccessResponseJo();
    }


    /**
     * 保持当前观看的视频
     */
    public void last(String rtspUrl, String rtmpUrl) {
        if (null != LIVE_VIDEO_MAP.get(rtspUrl)) {
            LIVE_VIDEO_MAP.put(rtspUrl, new Date());
        } else {
            start(rtspUrl, rtmpUrl);
        }
    }

    /**
     * 启动线程类
     */
    public void startTimer() {
        System.out.println(checkPeriod + "mins later start the timer and the time is: " + new Date());
        stopVideoExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (String key : LIVE_VIDEO_MAP.keySet()) {
                    Date last = LIVE_VIDEO_MAP.get(key);
                    if (DateUtils.secondBetween(last, new Date()) > timeOut) {
                        LIVE_VIDEO_MAP.remove(key);
                    }
                }
            }
        }, 0, checkPeriod, TimeUnit.SECONDS);
    }

}

class VideoThread extends Thread {

    private String rtspUrl;
    private String rtmpUrl;

    public VideoThread(String rtspUrl, String rtmpUrl) {
        this.rtspUrl = rtspUrl;
        this.rtmpUrl = rtmpUrl;
    }

    @Override
    public void run() {
        try {
            new VideoPakcetConvert().from(rtspUrl).to(rtmpUrl).go(rtspUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}