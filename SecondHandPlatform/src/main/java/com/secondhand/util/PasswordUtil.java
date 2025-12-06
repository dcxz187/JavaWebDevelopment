package com.secondhand.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 密码工具类，用于密码加密和验证
 */
public class PasswordUtil {
    
    /**
     * 对密码进行加密
     * @param password 明文密码
     * @return 加密后的密码
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    /**
     * 验证密码是否正确
     * @param password 明文密码
     * @param hashedPassword 加密后的密码
     * @return 验证结果
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}