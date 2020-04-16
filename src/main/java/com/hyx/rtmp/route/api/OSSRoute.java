package com.hyx.rtmp.route.api;

import com.hyx.rtmp.common.constant.HttpConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author heyx
 * @date 4/16/2020 5:17 PM
 */
@RestController
@Api(tags = "视频管理")
public class OSSRoute {

    /**
     * 开启一个线程进行播放
     */
    @GetMapping("/receive")
    @ApiOperation(value = "根据id播放视频", notes = "根据id播放视频", httpMethod = HttpConst.FINAL_METHOD_GET, produces = "application/json")
    public void receive(
            @RequestParam(name = "object") String object,
            @RequestParam(name = "size") String size,
            @RequestParam(name = "etag") String etag,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "fileId") String fileId,
            @RequestParam(name = "appkey") String appkey,
            @RequestParam(name = "sourceId") String sourceId,
            @RequestParam(name = "fileBucket") String fileBucket
                        ){
        System.out.println("object:" + object);
        System.out.println("size:" +  size);
        System.out.println("etag:" +  etag);
        System.out.println("name:" +  name);
        System.out.println("fileId:" +  fileId);
        System.out.println("appkey:" +  appkey);
        System.out.println("sourceId:" +  sourceId);
        System.out.println("fileBucket:" +  fileBucket);
    }

}
