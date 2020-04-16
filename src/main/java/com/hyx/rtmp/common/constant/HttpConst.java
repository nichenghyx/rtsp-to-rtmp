package com.hyx.rtmp.common.constant;

public interface HttpConst {

    /**
     * 请求成功。一般用于GET与POST请求
     */
    Integer SUCCESS = 200;
    /**
     * 客户端请求的语法错误，服务器无法理解
     */
    Integer BAD_REQUEST = 400;
    /**
     * 请求要求用户的身份认证
     */
    Integer UNAUTHORIZED = 401;
    /**
     * 服务器理解请求客户端的请求，但是拒绝执行此请求
     */
    Integer FORBIDDEN = 403;
    /**
     * 服务器无法根据客户端的请求找到资源
     */
    Integer NOT_FOUND = 404;

    Integer METHOD_NOT_ALLOWED = 405;

    Integer INTERNAL_SERVER_ERROR = 500;

    String FINAL_MSG_E = "运行异常";

    String ERROR_DELETE = "正在使用，无法删除";

    String ERROR_404 = "信息不存在";

    String ERROR_404_FILE = "文件不存在";

    String ERROR_400 = "请求错误";
    /**
     * 参数无法解析
     */
    String ERROR_400_PARAMS = "参数无法解析";
    /**
     * 服务器内部错误
     */
    String ERROR_500 = "服务器繁忙~";

    /**
     * 文件上传失败
     */
    String ERROR_500_FILE_UPLOAD = "文件上传失败";

    /**
     * POST
     */
    String FINAL_METHOD_POST = "POST";
    /**
     * GET
     */
    String FINAL_METHOD_GET = "GET";
    /**
     * PUT
     */
    String FINAL_METHOD_PUT = "PUT";
    /**
     * DELETE
     */
    String FINAL_METHOD_DELETE = "DELETE";

}
