package main.java.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class FrontControllerServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
        processHandler(req, res);
    }
    @Override
    protected void doPost(HttpServletRequest req,HttpServletResponse res) throws  ServletException, IOException{
        processHandler(req, res);
    }
    public void processHandler(HttpServletRequest req,HttpServletResponse res)throws  ServletException, IOException{
        res.setContentType("text/plain");
        PrintWriter out=res.getWriter();
        out.println("Welcome to FrameWorkMvc via url"+req.getContextPath());
    }
}
