package mg.itu.tsimbina.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import mg.itu.tsimbina.util.Util;
import mg.itu.tsimbina.dto.ControllerMethod;
import mg.itu.tsimbina.exception.NoMethodUrlException;
import mg.itu.tsimbina.model.MySession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.text.View;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import mg.itu.tsimbina.dto.UrlMethodDTO;
import mg.itu.tsimbina.dto.ViewPathDTO;
import mg.itu.tsimbina.exception.DupicatedUrl;

@MultipartConfig 
public class FrontControllerServlet extends HttpServlet {



    private ApplicationContext ctx;



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processHandler(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processHandler(req, res);
    }

    public void processHandler(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            Util util=(Util) this.getServletContext().getAttribute("util");
            util.executeMethod(req, res, getServletContext());
        } catch (NoMethodUrlException e) {
            System.out.println(e.getMessage());
        }

    }



    public ApplicationContext getCtx() {
        return ctx;
    }

    public void setCtx(ApplicationContext ctx) {
        this.ctx = ctx;
    }

}
