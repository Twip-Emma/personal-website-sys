package top.twip.api.enums;

/**
 * @author : 七画一只妖
 * @date : 2022-02-14 17:29
 */

public enum CodeEnum {
    INTERNAL_ERROR(500), // 服务器内部错误
    OK(200), // 成功
    NOT_FOUND(404), // 找不到请求的资源
    FORBIDDEN(403), // 请求被拒绝
    UNAUTHORIZED(401), // 未授权不通过
    BAD_REQUEST(400), // 服务器无法解析该请求

    // 自定义状态码
    NOT_ALL_OK(1204), // 成功了，但是不符合要求
    TOKEN_EXPIRED(1001), // 自定义：TOKEN过期
    USER_HANDLE_ERROR(1002), // 自定义：用户操作失误
    KEY_FORBIDDEN(1003);

    private final Integer code;

    CodeEnum(int code) {
        this.code = code;
    }

    public int value() {
        return code;
    }
}
