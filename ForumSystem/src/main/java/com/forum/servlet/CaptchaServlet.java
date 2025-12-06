package com.forum.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.util.Random;

import javax.imageio.ImageIO;

@WebServlet("/api/captcha")
public class CaptchaServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    
    // 验证码字符集
    private static final String CHAR_SET = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
    // 验证码长度
    private static final int CODE_LENGTH = 4;
    // 图片宽度
    private static final int WIDTH = 120;
    // 图片高度
    private static final int HEIGHT = 40;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应内容类型为JPEG图像
        response.setContentType("image/jpeg");
        
        // 禁止浏览器缓存验证码图片
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        
        // 创建验证码文本
        String captchaText = generateCaptchaText();
        
        // 将验证码存储在Session中用于验证
        HttpSession session = request.getSession();
        session.setAttribute("captcha", captchaText);
        
        // 创建图像
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        
        // 设置背景色
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        
        // 绘制边框
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        
        // 设置字体
        Font font = new Font("Arial", Font.BOLD, 24);
        graphics.setFont(font);
        
        // 绘制验证码文本
        graphics.setColor(Color.BLACK);
        int x = (WIDTH - (font.getSize() * CODE_LENGTH)) / 2;
        int y = HEIGHT / 2 + font.getSize() / 3;
        graphics.drawString(captchaText, x, y);
        
        // 添加干扰线
        Random random = new Random();
        graphics.setColor(Color.GRAY);
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            graphics.drawLine(x1, y1, x2, y2);
        }
        
        // 添加噪点
        graphics.setColor(Color.GRAY);
        for (int i = 0; i < 30; i++) {
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            graphics.drawOval(x1, y1, 1, 1);
        }
        
        // 释放资源
        graphics.dispose();
        
        // 将图像写入响应输出流
        ImageIO.write(bufferedImage, "jpeg", response.getOutputStream());
    }
    
    /**
     * 生成随机验证码文本
     * @return 验证码文本
     */
    private String generateCaptchaText() {
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHAR_SET.length());
            captcha.append(CHAR_SET.charAt(index));
        }
        return captcha.toString();
    }
}