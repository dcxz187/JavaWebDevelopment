package com.forum.dao;

import com.forum.model.Reply;
import com.forum.model.Thread;
import com.forum.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 内存数据存储
 */
public class DataStore {
    // 使用线程安全的集合存储数据
    private static final List<User> users = new CopyOnWriteArrayList<>();
    private static final List<Thread> threads = new CopyOnWriteArrayList<>();
    private static final List<Reply> replies = new CopyOnWriteArrayList<>();
    
    // 使用原子整数生成ID
    private static final AtomicInteger threadIdGenerator = new AtomicInteger(1);
    private static final AtomicInteger replyIdGenerator = new AtomicInteger(1);
    
    // 初始化一些测试数据
    static {
        // 添加测试用户
        users.add(new User("admin", "admin123"));
        users.add(new User("user1", "password1"));
        users.add(new User("user2", "password2"));
        
        // 添加测试帖子
        Thread thread1 = new Thread(threadIdGenerator.getAndIncrement(), "欢迎来到论坛", "这是论坛的第一个帖子，欢迎大家讨论！", "admin");
        Thread thread2 = new Thread(threadIdGenerator.getAndIncrement(), "Java学习交流", "大家在学习Java过程中有什么问题可以在这里讨论", "user1");
        threads.add(thread1);
        threads.add(thread2);
        
        // 添加测试回复
        Reply reply1 = new Reply(replyIdGenerator.getAndIncrement(), "感谢分享，学习了！", "user2", thread1.getId());
        Reply reply2 = new Reply(replyIdGenerator.getAndIncrement(), "这个帖子很有用", "user1", thread1.getId());
        replies.add(reply1);
        replies.add(reply2);
        
        // 将回复添加到对应的帖子中
        thread1.addReply(reply1);
        thread1.addReply(reply2);
    }
    
    // 用户相关操作
    public static List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    public static User getUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
    
    public static void addUser(User user) {
        users.add(user);
    }
    
    public static boolean isUsernameExists(String username) {
        return users.stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }
    
    // 帖子相关操作
    public static List<Thread> getAllThreads() {
        return new ArrayList<>(threads);
    }
    
    public static Thread getThreadById(int id) {
        return threads.stream()
                .filter(thread -> thread.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public static void addThread(Thread thread) {
        threads.add(thread);
    }
    
    public static int generateThreadId() {
        return threadIdGenerator.getAndIncrement();
    }
    
    // 回复相关操作
    public static List<Reply> getRepliesByThreadId(int threadId) {
        return replies.stream()
                .filter(reply -> reply.getThreadId() == threadId)
                .collect(ArrayList::new, (list, item) -> list.add(item), (list1, list2) -> list1.addAll(list2));
    }
    
    public static void addReply(Reply reply) {
        replies.add(reply);
        
        // 同时将回复添加到对应的帖子中
        Thread thread = getThreadById(reply.getThreadId());
        if (thread != null) {
            thread.addReply(reply);
        }
    }
    
    public static int generateReplyId() {
        return replyIdGenerator.getAndIncrement();
    }
}