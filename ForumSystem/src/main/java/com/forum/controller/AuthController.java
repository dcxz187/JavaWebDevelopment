package com.forum.controller;

import com.forum.model.User;
import com.forum.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestParam String username, 
                                   @RequestParam String password, 
                                   @RequestParam String captcha, 
                                   HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 验证验证码
            HttpSession session = request.getSession();
            String sessionCaptcha = (String) session.getAttribute("captcha");
            
            // 移除session中的验证码，确保一次有效
            session.removeAttribute("captcha");
            
            // 检查验证码
            if (captcha == null || sessionCaptcha == null || !captcha.equalsIgnoreCase(sessionCaptcha)) {
                result.put("success", false);
                result.put("message", "验证码错误");
                return result;
            }
            
            // 验证用户名和密码
            User user = userService.findByUsername(username);
            
            if (user == null || !user.getPassword().equals(password)) {
                result.put("success", false);
                result.put("message", "用户名或密码错误");
                return result;
            }
            
            // 登录成功，将用户信息存储到session中
            session.setAttribute("user", user);

            result.put("success", true);
            result.put("message", "登录成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "登录时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/register")
    @ResponseBody
    public Map<String, Object> register(@RequestParam String username,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword,
                                      @RequestParam String captcha,
                                      HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 检查参数是否完整
            if (username == null || password == null || confirmPassword == null || captcha == null) {
                result.put("success", false);
                result.put("message", "请填写所有必填字段");
                return result;
            }

            // 检查参数是否为空字符串
            if (username.trim().isEmpty() || password.trim().isEmpty() || confirmPassword.trim().isEmpty() || captcha.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "请填写所有必填字段");
                return result;
            }

            // 验证验证码
            HttpSession session = request.getSession();
            String sessionCaptcha = (String) session.getAttribute("captcha");

            // 移除session中的验证码，确保一次有效
            session.removeAttribute("captcha");

            // 检查验证码
            if (!captcha.equalsIgnoreCase(sessionCaptcha)) {
                result.put("success", false);
                result.put("message", "验证码错误");
                return result;
            }

            // 检查两次输入的密码是否一致
            if (!password.equals(confirmPassword)) {
                result.put("success", false);
                result.put("message", "两次输入的密码不一致");
                return result;
            }

            // 检查用户名是否已存在
            if (userService.existsByUsername(username)) {
                result.put("success", false);
                result.put("message", "用户名已存在");
                return result;
            }

            // 创建新用户并保存
            User user = new User(username, password);
            userService.save(user);

            result.put("success", true);
            result.put("message", "注册成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "注册时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/logout")
    @ResponseBody
    public Map<String, Object> logout(HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 销毁session
            if (session != null) {
                session.invalidate();
            }

            result.put("success", true);
            result.put("message", "退出登录成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "退出登录时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/user")
    @ResponseBody
    public Map<String, Object> getUser(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            
            if (user == null) {
                result.put("success", false);
                result.put("message", "用户未登录");
                return result;
            }
            
            // 返回用户信息
            Map<String, String> userData = new HashMap<>();
            userData.put("username", user.getUsername());
            
            result.put("success", true);
            result.put("data", userData);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取用户信息时发生错误: " + e.getMessage());
        }
        
        return result;
    }
}