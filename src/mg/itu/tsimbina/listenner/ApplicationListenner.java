package mg.itu.tsimbina.listenner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import mg.itu.tsimbina.annotation.UrlMapping;
import mg.itu.tsimbina.dto.ControllerMethod;
import mg.itu.tsimbina.dto.UrlMethodDTO;
import mg.itu.tsimbina.exception.DupicatedUrl;
import mg.itu.tsimbina.model.MySession;
import mg.itu.tsimbina.util.Util;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.util.ArrayList;
import mg.itu.tsimbina.dto.ViewPathDTO;

public class ApplicationListenner implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) throws DupicatedUrl {
        System.out.println("Application started");

        Util util = new Util();
        ServletContext context = sce.getServletContext();

        Map<UrlMethodDTO, ControllerMethod> controllerMethods = new HashMap<>();
        List<Class<?>> listClasses = new ArrayList<>();
        try {
            util.getClasses(context.getInitParameter("packages"), controllerMethods,listClasses);
            context.setAttribute("controllerMethods", controllerMethods);
            context.setAttribute("listClasses", listClasses);
            context.setAttribute("util", util);
            context.setAttribute("authKey", context.getInitParameter("authKey"));
            context.setAttribute("viewPathDTO", new ViewPathDTO(context.getInitParameter("viewPrefix"), context.getInitParameter("viewSuffix")));
            Map<String,MySession> mapSession= new HashMap<>();
            context.setAttribute("MapSession",mapSession );
        } catch (DupicatedUrl e) {
            System.out.println(e.getMessage());
            throw e;
        } catch(Exception e){
            e.printStackTrace();
        }
        

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application stopped");
    }

}
