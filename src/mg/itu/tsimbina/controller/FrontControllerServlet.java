package mg.itu.tsimbina.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.tsimbina.util.Util;
import mg.itu.tsimbina.dto.ControllerMethodUrlDTO;
import mg.itu.tsimbina.exception.NoMethodUrlException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontControllerServlet extends HttpServlet {

    List<Class<?>> listClasses;
    Map<String, ControllerMethodUrlDTO> controllerMethods;

    public void init() {
        Util util = new Util();
        controllerMethods = new HashMap<>();
        setListClasses(util.getClasses(getInitParameter("packages"), controllerMethods));
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
            ControllerMethodUrlDTO controllerMethodUrlDTO = getControllerMethodUrlDTOByUrl(getPathAfterBaseURL(req));

            out.println(controllerMethodUrlDTO);
        } catch (NoMethodUrlException e) {
            out.println(e.getMessage());
            out.println("List des controller method urls:");

            for (String elem : controllerMethods.keySet()) {
                out.println("Url:" + elem + " " + controllerMethods.get(elem));
            }
        }

    }

    public ControllerMethodUrlDTO getControllerMethodUrlDTOByUrl(String url) {
        ControllerMethodUrlDTO ret = controllerMethods.get(url);
        if (ret != null) {
            return ret;
        } else {
            throw new NoMethodUrlException("No method url found for url: " + url);

        }
    }

    public String getPathAfterBaseURL(HttpServletRequest req) {
        String baseURL = req.getContextPath();
        String requestURL = req.getRequestURI();
        return requestURL.substring(baseURL.length());
    }

    public List<Class<?>> getListClasses() {
        return listClasses;
    }

    public void setListClasses(List<Class<?>> listClasses) {
        this.listClasses = listClasses;
    }

}
