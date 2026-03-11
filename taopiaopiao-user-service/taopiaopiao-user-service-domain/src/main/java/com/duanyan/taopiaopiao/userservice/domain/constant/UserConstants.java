package com.duanyan.taopiaopiao.userservice.domain.constant;

/**
 * 用户服务常量
 *
 * @author duanyan
 * @since 1.0.0
 */
public class UserConstants {

    /**
     * 用户状态
     */
    public static final class Status {
        /** 正常 */
        public static final String ACTIVE = "active";
        /** 禁用 */
        public static final String DISABLED = "disabled";
        /** 锁定 */
        public static final String LOCKED = "locked";
    }

    /**
     * 错误码
     */
    public static final class ErrorCode {
        /** 用户名或密码错误 */
        public static final int INVALID_CREDENTIALS = 1001;
        /** 账号已被禁用 */
        public static final int ACCOUNT_DISABLED = 1002;
        /** 用户不存在 */
        public static final int USER_NOT_FOUND = 1003;
    }

    private UserConstants() {}
}
