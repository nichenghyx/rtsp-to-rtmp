package com.hyx.rtmp.route.api;

import com.hyx.rtmp.common.constant.HttpConst;
import com.hyx.rtmp.common.utils.ResponseUtils;
import com.hyx.rtmp.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author heyx
 * @date 4/16/2020 2:06 PM
 */
@RestController
@RequestMapping("/api/video")
@Api(tags = "视频管理")
public class VideoRoute {

    @Autowired
    VideoService videoService;

    /**
     * 开启一个线程进行播放
     */
    @GetMapping("/start")
    @ApiOperation(value = "根据id播放视频", notes = "根据id播放视频", httpMethod = HttpConst.FINAL_METHOD_GET, produces = "application/json")
    @ApiResponse(message = "", code = HttpConst.SUCCESS, response = Object.class)
    public ResponseUtils start(@RequestParam(name = "rtsp") String rtsp, @RequestParam(name = "rtmp") String rtmp){
        return videoService.start(rtsp, rtmp);
    }

    /**
     * 视频延时，保持转换
     * @return
     */
    @GetMapping("/last")
    @ApiOperation(value = "保持视频延时", notes = "保持视频延时", httpMethod = HttpConst.FINAL_METHOD_GET, produces = "application/json")
    @ApiResponse(message = "", code = HttpConst.SUCCESS, response = Object.class)
    public ResponseUtils last(@RequestParam(name = "rtsp") String rtsp, @RequestParam(name = "rtmp") String rtmp) {
        videoService.last(rtsp, rtmp);
        return ResponseUtils.getSuccessResponseJo();
    }

    /**
     * 强行停止转换某个视频
     */
    @GetMapping("/stop")
    @ApiOperation(value = "停止某个视频", notes = "停止某个视频", httpMethod = HttpConst.FINAL_METHOD_GET, produces = "application/json")
    @ApiResponse(message = "", code = HttpConst.SUCCESS, response = Object.class)
    public void stop(@RequestParam(name = "rtsp") String rtsp) {
        if (StringUtils.isNotBlank(rtsp)) {
            VideoService.LIVE_VIDEO_MAP.remove(rtsp);
        }
    }

}
