package com.questionnaire.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet({"/questionnaire", "/result", "/api/results"})
public class QuestionnaireController extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final Gson gson = new Gson();
    
    // 用于存储所有问卷结果的静态列表
    private static final List<QuestionnaireResult> results = new ArrayList<>();

    // 问卷结果实体类
    public static class QuestionnaireResult {
        private String name;
        private String gender;
        private int age;
        private String department;
        private String[] hobbies;
        
        public QuestionnaireResult() {}
        
        public QuestionnaireResult(String name, String gender, int age, String department, String[] hobbies) {
            this.name = name;
            this.gender = gender;
            this.age = age;
            this.department = department;
            this.hobbies = hobbies;
        }

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        
        public String[] getHobbies() { return hobbies; }
        public void setHobbies(String[] hobbies) { this.hobbies = hobbies; }
    }
    
    // API响应类
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;
        
        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/questionnaire":
                showQuestionnaire(request, response);
                break;
            case "/result":
                showResults(request, response);
                break;
            case "/api/results":
                getResultsApi(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/questionnaire":
                processQuestionnaire(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    
    private void showQuestionnaire(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/questionnaire.html").forward(request, response);
    }
    
    private void processQuestionnaire(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        // 获取表单参数
        String name = request.getParameter("name");
        String gender = request.getParameter("gender");
        int age = Integer.parseInt(request.getParameter("age"));
        String department = request.getParameter("department");
        String[] hobbies = request.getParameterValues("hobbies");
        
        // 创建问卷结果对象
        QuestionnaireResult result = new QuestionnaireResult(name, gender, age, department, hobbies);
        
        // 将结果添加到列表中
        results.add(result);
        
        // 将结果保存到session中以便在结果页面显示
        HttpSession session = request.getSession();
        session.setAttribute("currentResult", result);
        
        // 重定向到结果显示页面
        response.sendRedirect("result");
    }
    
    private void showResults(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/result.html").forward(request, response);
    }
    
    private void getResultsApi(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        
        HttpSession session = request.getSession();
        QuestionnaireResult currentResult = (QuestionnaireResult) session.getAttribute("currentResult");
        
        // 处理爱好显示
        StringBuilder hobbiesDisplay = new StringBuilder();
        if (currentResult != null && currentResult.getHobbies() != null) {
            for (int i = 0; i < currentResult.getHobbies().length; i++) {
                if (i > 0) hobbiesDisplay.append(", ");
                hobbiesDisplay.append(currentResult.getHobbies()[i]);
            }
        }
        
        // 计算统计数据
        int totalParticipants = results.size();
        double averageAge = 0;
        if (totalParticipants > 0) {
            int totalAge = 0;
            for (QuestionnaireResult result : results) {
                totalAge += result.getAge();
            }
            averageAge = (double) totalAge / totalParticipants;
        }
        
        // 构造返回数据
        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("currentResult", currentResult);
        data.put("hobbiesDisplay", hobbiesDisplay.toString());
        data.put("totalParticipants", totalParticipants);
        data.put("averageAge", String.format("%.2f", averageAge));
        
        ApiResponse apiResponse = new ApiResponse(true, "获取结果成功", data);
        String jsonResponse = gson.toJson(apiResponse);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}