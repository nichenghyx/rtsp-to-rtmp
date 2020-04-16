package com.hyx.rtmp.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dwzc.yongdinghe.common.constant.HttpConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 请求返回对象
 *
 * @author: pzy
 * @create: 2020-02-27 15:41
 */
@Data
@Builder
public class ResponseUtils {

    @ApiModelProperty("状态码")
    private Integer status;
    @ApiModelProperty("数据")
    private Object data;
    @ApiModelProperty("消息提醒")
    private String msg;
    @ApiModelProperty("数量")
    private long total;

    public static ResponseUtils getSuccessResponseJo() {
        return ResponseUtils.builder()
                .status(HttpConst.SUCCESS)
                .build();
    }

    public static ResponseUtils getSuccessResponseJoMsg(String msg) {
        return ResponseUtils.builder()
                .msg(msg)
                .status(HttpConst.SUCCESS)
                .build();
    }

    public static ResponseUtils getSuccessResponseJoData(Object data) {
        if (data instanceof IPage){
            return getSuccessResponseJoDataAndTotal(((IPage) data).getRecords(), ((IPage) data).getTotal());
        }else if(data instanceof Page) {
            return getSuccessResponseJoDataAndTotal(((Page) data).getContent(), ((Page) data).getTotalElements());
        }

        return ResponseUtils.builder()
                .data(data)
                .status(HttpConst.SUCCESS)
                .build();
    }

    public static ResponseUtils getSuccessResponseJoDataAndTotal(Object data, long total) {
        return ResponseUtils.builder()
                .data(data)
                .total(total)
                .status(HttpConst.SUCCESS)
                .build();
    }

    public static ResponseUtils getFailureResponseJoMsg(String msg, Integer status) {
        return ResponseUtils.builder()
                .msg(msg)
                .status(status)
                .build();
    }

    public static ResponseUtils getIntervalServerResponseJo() {
        return ResponseUtils.builder()
                .msg(HttpConst.ERROR_500)
                .status(HttpConst.INTERNAL_SERVER_ERROR)
                .build();
    }
    public static ResponseUtils getIntervalServerResponseJoMsg(String msg) {
        return ResponseUtils.builder()
                .msg(msg)
                .status(HttpConst.INTERNAL_SERVER_ERROR)
                .build();
    }

    /**
     * 文件相应设置
     *
     * @param filePath 文件的完整路径，包含文件名称
     */
    public static void response(HttpServletResponse response, String filePath) {
        OutputStream os = null;
        try {
            response.reset();
            os = response.getOutputStream();
            File file = new File(filePath);
            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[2048];
            int length = 0;
            while ((length = inputStream.read(bytes)) > 0) {
                os.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 响应文件流
     */
    public static void response(HttpServletResponse response, InputStream inputStream) {
        OutputStream os = null;
        try {
            response.reset();
            os = response.getOutputStream();
            byte[] bytes = new byte[2048];
            int length = 0;
            while ((length = inputStream.read(bytes)) > 0) {
                os.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setHeader(HttpServletResponse response, String fileName){
        response.setHeader("Content-disposition", "attachment;filename=\"" + fileName + "\"");
        response.setContentType("application/octet-stream;charset=UTF-8");// 设置类型
        response.setHeader("Pragma", "No-cache");// 设置头
        response.setHeader("Cache-Control", "no-cache");// 设置头
        response.setDateHeader("Expires", 0);// 设置日期头
    }

    public static void response(HttpServletRequest request, HttpServletResponse response, String filePath, String fileName) {
        OutputStream os = null;
        try {
            response.reset();
            os = response.getOutputStream();
            String headerFileName = "";
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                headerFileName = URLEncoder.encode(fileName, "utf-8");
            } else {
                headerFileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
            }
            setHeader(response, headerFileName);
            File file = new File(filePath);
            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[2048];
            int length = 0;
            while ((length = inputStream.read(bytes)) > 0) {
                os.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 响应文件流
     */
    public static void response(HttpServletRequest request, HttpServletResponse response, InputStream inputStream, String fileName) {
        OutputStream os = null;
        try {
            response.reset();
            os = response.getOutputStream();
            String headerFileName = "";
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                headerFileName = URLEncoder.encode(fileName, "utf-8");
            } else {
                headerFileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
            }
            setHeader(response , headerFileName);
            byte[] bytes = new byte[2048];
            int length = 0;
            while ((length = inputStream.read(bytes)) > 0) {
                os.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
