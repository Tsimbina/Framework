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
import mg.itu.tsimbina.dto.UrlMethodDTO;
import mg.itu.tsimbina.exception.DupicatedUrl;

public class FrontControllerServlet extends HttpServlet {

    List<Class<?>> listClasses;
    Map<UrlMethodDTO, ControllerMethodUrlDTO> controllerMethods;
    Util util;

    public void init() {
        this.util = new Util();
        controllerMethods = new HashMap<>();
        try {
            setListClasses(util.getClasses(getInitParameter("packages"), controllerMethods));

        } catch (DupicatedUrl e) {
            log(e.getMessage());
            throw e;
        }
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
            ControllerMethodUrlDTO controllerMethodUrlDTO = util.getControllerMethodUrlDTOByUrlMethod(urlMethodDTO, controllerMethods);
            controllerMethodUrlDTO.executeMethod();
            out.println(urlMethodDTO + "" + controllerMethodUrlDTO);
        } catch (NoMethodUrlException e) {
            out.println(e.getMessage());
            out.println("List des controller method urls:");

            for (UrlMethodDTO elem : controllerMethods.keySet()) {
                out.println(elem + " " + controllerMethods.get(elem));
            }
        }

    }

    public List<Class<?>> getListClasses() {
        return listClasses;
    }

    public void setListClasses(List<Class<?>> listClasses) {
        this.listClasses = listClasses;
    }

}
