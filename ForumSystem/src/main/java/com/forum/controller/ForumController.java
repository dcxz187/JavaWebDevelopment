package com.forum.controller;

import com.forum.model.Reply;
import com.forum.model.Thread;
import com.forum.model.User;
import com.forum.service.ReplyService;
import com.forum.service.ThreadService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ForumController {

    @Autowired
    private ThreadService threadService;
    
    @Autowired
    private ReplyService replyService;

    @GetMapping("/threads")
    @ResponseBody
    public Map<String, Object> getThreads(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            
            if (user == null) {
                result.put("success", false);
                result.put("message", "用户未登录");
                return result;
            }
            
            // 获取所有帖子
            List<Thread> threads = threadService.findAll();
            
            result.put("success", true);
            result.put("data", threads);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取帖子列表时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/thread")
    @ResponseBody
    public Map<String, Object> getThread(@RequestParam int id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            
            if (user == null) {
                result.put("success", false);
                result.put("message", "用户未登录");
                return result;
            }
            
            // 获取帖子信息
            Thread thread = threadService.findById(id);
            if (thread == null) {
                result.put("success", false);
                result.put("message", "帖子不存在");
                return result;
            }
            
            // 获取帖子的回复列表
            List<Reply> replies = replyService.findByThreadId(id);
            
            // 构造返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("thread", thread);
            data.put("replies", replies);
            
            result.put("success", true);
            result.put("data", data);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取帖子详情时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/createThread")
    @ResponseBody
    public Map<String, Object> createThread(@RequestParam String title,
                                          @RequestParam String content,
                                          HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            
            if (user == null) {
                result.put("success", false);
                result.put("message", "用户未登录");
                return result;
            }
            
            // 检查参数
            if (title == null || title.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "标题不能为空");
                return result;
            }
            
            if (content == null || content.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "内容不能为空");
                return result;
            }
            
            // 创建新帖子
            Thread thread = new Thread();
            thread.setId(threadService.generateId());
            thread.setTitle(title.trim());
            thread.setContent(content.trim());
            thread.setAuthor(user.getUsername());
            
            // 保存帖子
            threadService.save(thread);
            
            result.put("success", true);
            result.put("message", "帖子创建成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建帖子时发生错误: " + e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/addReply")
    @ResponseBody
    public Map<String, Object> addReply(@RequestParam int threadId,
                                      @RequestParam String content,
                                      HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            User user = (User) session.getAttribute("user");
            
            if (user == null) {
                result.put("success", false);
                result.put("message", "用户未登录");
                return result;
            }
            
            // 检查参数
            if (content == null || content.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "回复内容不能为空");
                return result;
            }
            
            try {
                // 创建新回复
                Reply reply = new Reply();
                reply.setId(replyService.generateId());
                reply.setContent(content.trim());
                reply.setAuthor(user.getUsername());
                reply.setThreadId(threadId);
                
                // 保存回复
                replyService.save(reply);
                
                result.put("success", true);
                result.put("message", "回复发表成功");
            } catch (NumberFormatException e) {
                result.put("success", false);
                result.put("message", "无效的帖子ID");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发表回复时发生错误: " + e.getMessage());
        }
        
        return result;
    }
}