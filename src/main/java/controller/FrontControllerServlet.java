package main.java.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import main.java.util.Util;

public class FrontControllerServlet extends HttpServlet{
    List<Class<?>> listClasses;

    public void init(){
        Util util= new Util();

        setListClasses(util.getControllerClasses(getInitParameter("packages")));
    }
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
        if (req.getRequestURL().toString().startsWith("/controller/list")) {
            for (Class<?> elem : getListClasses()) {
                out.println(elem.getName());
            }
        }
        for (Class<?> elem : getListClasses()) {
                out.println(elem.getName());
            }
            out.println("size"+ getListClasses().size());
            out.println(getInitParameter("packages"));
        out.println("Welcome to FrameWorkMvc via url"+req.getContextPath());
    }

    public List<Class<?>> getListClasses() {
        return listClasses;
    }

    public void setListClasses(List<Class<?>> listClasses) {
        this.listClasses = listClasses;
    }
}
