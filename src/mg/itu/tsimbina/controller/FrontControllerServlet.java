package mg.itu.tsimbina.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.tsimbina.util.Util;
import mg.itu.tsimbina.dto.ControllerMethod;
import mg.itu.tsimbina.exception.NoMethodUrlException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.text.View;
import mg.itu.tsimbina.dto.UrlMethodDTO;
import mg.itu.tsimbina.dto.ViewPathDTO;
import mg.itu.tsimbina.exception.DupicatedUrl;

public class FrontControllerServlet extends HttpServlet {

    List<Class<?>> listClasses;
    Map<UrlMethodDTO, ControllerMethod> controllerMethods;
    Util util;
    ViewPathDTO viewPathDTO;
    public void init() {
        this.setListClasses((List) this.getServletContext().getAttribute("listClasses"));
        this.setUtil((Util) this.getServletContext().getAttribute("util"));
        this.setControllerMethods((Map) this.getServletContext().getAttribute("controllerMethods"));
        this.setViewPathDTO((ViewPathDTO) this.getServletContext().getAttribute("viewPathDTO"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processHandler(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processHandler(req, res);
    }

    public void processHandler(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        out.println("Welcome to FrameWorkMvc via url: " + req.getRequestURI());
        out.println();
        out.println();

        out.println("List des classes controller :");
        for (Class<?> elem : getListClasses()) {
            out.println(elem.getName());
        }
        out.println();
        out.println();

        try {
            UrlMethodDTO urlMethodDTO = new UrlMethodDTO(util.getPathAfterBaseURL(req), req.getMethod());
            ControllerMethod ControllerMethod = util.getControllerMethodByUrlMethod(urlMethodDTO, controllerMethods);
            out.println(urlMethodDTO + "" + ControllerMethod);
            ControllerMethod.executeMethod(req,res,this.getViewPathDTO());
        } catch (NoMethodUrlException e) {
            out.println(e.getMessage());
            out.println("List des controller method urls:");

            for (UrlMethodDTO elem : controllerMethods.keySet()) {
                out.println(elem + " " + controllerMethods.get(elem));
            }
        }

    }

    public List<Class<?>> getListClasses() {
        return this.listClasses;
    }

    public void setListClasses(List<Class<?>> var1) {
        this.listClasses = var1;
    }

    public Map<UrlMethodDTO, ControllerMethod> getControllerMethods() {
        return this.controllerMethods;
    }

    public void setControllerMethods(Map<UrlMethodDTO, ControllerMethod> var1) {
        this.controllerMethods = var1;
    }

    public Util getUtil() {
        return this.util;
    }

    public void setUtil(Util var1) {
        this.util = var1;
    }

    public ViewPathDTO getViewPathDTO() {
        return viewPathDTO;
    }

    public void setViewPathDTO(ViewPathDTO viewPathDTO) {
        this.viewPathDTO = viewPathDTO;
    }

}
