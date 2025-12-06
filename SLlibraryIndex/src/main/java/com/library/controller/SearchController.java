package com.library.controller;

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
import com.google.gson.Gson;

@WebServlet("/api/search")
public class SearchController extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Gson gson = new Gson();
    private final BookService bookService = new BookService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");

        try {
            List<Book> results = bookService.searchBooks(keyword);
            SearchResponse searchResponse = new SearchResponse(true, "搜索成功", results);
            String jsonResponse = gson.toJson(searchResponse);

            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
        } catch (Exception e) {
            SearchResponse errorResponse = new SearchResponse(false, "搜索失败: " + e.getMessage(), null);
            String jsonResponse = gson.toJson(errorResponse);

            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // 图书实体类
    public static class Book {
        private String title;
        private String author;
        private String year;

        public Book(String title, String author, String year) {
            this.title = title;
            this.author = author;
            this.year = year;
        }

        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getYear() { return year; }

        public void setTitle(String title) { this.title = title; }
        public void setAuthor(String author) { this.author = author; }
        public void setYear(String year) { this.year = year; }
    }

    // 图书服务类
    public static class BookService {
        private static final List<Book> bookDatabase = new ArrayList<>();

        static {
            bookDatabase.add(new Book("Java编程思想", "Bruce Eckel", "2007"));
            bookDatabase.add(new Book("深入理解Java虚拟机", "周志明", "2019"));
            bookDatabase.add(new Book("算法导论", "Thomas H.Cormen", "2009"));
            bookDatabase.add(new Book("计算机网络", "谢希仁", "2021"));
            bookDatabase.add(new Book("数据结构与算法分析", "Mark Allen Weiss", "2020"));
        }

        public List<Book> searchBooks(String keyword) {
            List<Book> results = new ArrayList<>();
            if (keyword == null || keyword.trim().isEmpty()) {
                return results;
            }

            keyword = keyword.toLowerCase();
            for (Book book : bookDatabase) {
                if (book.getTitle().toLowerCase().contains(keyword) ||
                    book.getAuthor().toLowerCase().contains(keyword)) {
                    results.add(book);
                }
            }
            return results;
        }
    }

    public static class SearchResponse {
        private boolean success;
        private String message;
        private List<Book> results;

        public SearchResponse(boolean success, String message, List<Book> results) {
            this.success = success;
            this.message = message;
            this.results = results;
        }
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public List<Book> getResults() { return results; }
        public void setResults(List<Book> results) { this.results = results; }
    }
}