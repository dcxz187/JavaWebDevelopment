package com.secondhand.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.ZoneId;

/**
 * 数据库连接池管理类
 */
public class DatabaseConnection {
    private static HikariDataSource dataSource;
    
    static {
        try {
            System.out.println("开始初始化数据库连接池...");
            
            // 加载MySQL驱动
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("MySQL驱动加载成功");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL驱动未找到: " + e.getMessage());
                throw new RuntimeException("MySQL驱动未找到", e);
            }
            
            HikariConfig config = new HikariConfig();
            
            // 获取系统默认时区
            String systemTimeZone = ZoneId.systemDefault().toString();
            System.out.println("系统时区: " + systemTimeZone);
            
            // 从环境变量或默认值获取数据库配置，添加时区参数
            String jdbcUrl = System.getenv("DB_URL") != null ? 
                System.getenv("DB_URL") : 
                "jdbc:mysql://localhost:3306/secondhand_platform?useSSL=false&serverTimezone=" + systemTimeZone + "&allowPublicKeyRetrieval=true";
                
            String username = System.getenv("DB_USERNAME") != null ? 
                System.getenv("DB_USERNAME") : "root";
                
            String password = System.getenv("DB_PASSWORD") != null ? 
                System.getenv("DB_PASSWORD") : "123456";
            
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            
            System.out.println("数据库配置: URL=" + config.getJdbcUrl() + ", Username=" + config.getUsername());
            
            dataSource = new HikariDataSource(config);
            System.out.println("数据库连接池初始化成功! 数据源: " + dataSource);
        } catch (Throwable e) {
            System.err.println("数据库连接池初始化失败: " + e.getMessage());
            System.err.println("完整的异常堆栈:");
            e.printStackTrace();
            
            // 即使初始化失败，也要确保程序能继续运行（但数据库功能会失效）
            dataSource = null;
        }
    }
    
    /**
     * 获取数据库连接
     * @return 数据库连接
     * @throws SQLException SQL异常
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("数据库连接池未正确初始化，请检查数据库配置和连接参数");
        }
        return dataSource.getConnection();
    }
    
    /**
     * 获取数据源
     * @return 数据源
     */
    public static DataSource getDataSource() {
        return dataSource;
    }
    
    /**
     * 检查数据源是否正确初始化
     * @return true表示初始化成功，false表示初始化失败
     */
    public static boolean isDataSourceInitialized() {
        return dataSource != null;
    }
}