package mg.itu.tsimbina.listenner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import mg.itu.tsimbina.annotation.UrlMapping;
import mg.itu.tsimbina.dto.ControllerMethodUrlDTO;
import mg.itu.tsimbina.dto.UrlMethodDTO;
import mg.itu.tsimbina.exception.DupicatedUrl;
import mg.itu.tsimbina.util.Util;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.util.ArrayList;
public class ApplicationListenner implements ServletContextListener {

    

    @Override
    public void contextInitialized(ServletContextEvent sce) throws DupicatedUrl {
        System.out.println("Application started");
        
        Util util=new Util();
         ServletContext context = sce.getServletContext();
         
         

        Map<UrlMethodDTO,ControllerMethodUrlDTO> controllerMethods = new HashMap<>();
        List<Class<?>> listClasses =new ArrayList<>();
        try {
            listClasses=util.getClasses(context.getInitParameter("packages"), controllerMethods);
            context.setAttribute("controllerMethods", controllerMethods);
            context.setAttribute("listClasses", listClasses);
            context.setAttribute("util", util);
        } catch (DupicatedUrl e) {
            System.out.println(e.getMessage());
            throw e;
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application stopped");
    }


}
