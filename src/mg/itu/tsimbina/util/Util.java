package mg.itu.tsimbina.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Parameter;
import mg.itu.tsimbina.annotation.Controller;
import mg.itu.tsimbina.annotation.UrlMapping;
import mg.itu.tsimbina.dto.ControllerMethod;
import mg.itu.tsimbina.dto.UrlMethodDTO;
import mg.itu.tsimbina.exception.DupicatedUrl;
import mg.itu.tsimbina.exception.NoMethodUrlException;

public class Util {

    public List<String> splitPackage(String packageName) {
        return List.of(packageName.split(";"));
    }

    public void loadClasses(File directory, String packageName, List<Class<?>> listClass) throws Exception {
        if (!directory.exists()) {
            return;
        }
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                String sousPackageName = packageName + "." + file.getName();
                loadClasses(file, sousPackageName, listClass);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                listClass.add(Class.forName(className));
            }
        }
    }

    public void loadClassesIn(String packageName, List<Class<?>> listClasses) throws Exception {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        File directory = new File(loader.getResource(path).getFile());
        loadClasses(directory, packageName, listClasses);
    }

    public List<Class<?>> getClasses(String packagesName) {
        List<Class<?>> listClasses = new ArrayList<>();
        try {
            for (String elem : splitPackage(packagesName)) {
                loadClassesIn(elem, listClasses);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listClasses;
    }

    public List<Class<?>> loadClasses(String packagesName, Class<?> annotationClass) {
        List<Class<?>> listClasses = getClasses(packagesName);
        List<Class<?>> listAnnotatedClasses = new ArrayList<>();

        for (Class<?> elem : listClasses) {
            if (elem.isAnnotationPresent((Class<java.lang.annotation.Annotation>) annotationClass)) {
                listAnnotatedClasses.add(elem);
            }
        }
        return listAnnotatedClasses;
    }

    public void getClasses(String packagesName, Map<UrlMethodDTO, ControllerMethod> controlerMethods,
            List<Class<?>> listClasses) throws DupicatedUrl, Exception {
        listClasses = getClasses(packagesName);
        List<Class<?>> listAnnotatedClasses = new ArrayList<>();
        //Object o = null;
        for (Class<?> elem : listClasses) {
            //o = elem.getDeclaredConstructor().newInstance();
            if (elem.isAnnotationPresent(Controller.class)) {
                listAnnotatedClasses.add(elem);
                for (java.lang.reflect.Method method : elem.getMethods()) {
                    if (method.isAnnotationPresent(UrlMapping.class)) {
                        UrlMapping mapping = method.getAnnotation(UrlMapping.class);
                        String url = mapping.url();
                        String methodName = mapping.methodName();
                        UrlMethodDTO urlMethodDTO = new UrlMethodDTO(url, methodName);
                        if (controlerMethods.containsKey(urlMethodDTO)) {
                            throw new DupicatedUrl(urlMethodDTO);
                        }
                        controlerMethods.put(urlMethodDTO, new ControllerMethod(elem,  method));
                    }

                }
            }
        }
    }

    public void executeMethod(HttpServletRequest req, HttpServletResponse res, ServletContext servletContext) {
        Map<UrlMethodDTO, ControllerMethod> controllerMethods = (Map<UrlMethodDTO, ControllerMethod>) servletContext.getAttribute("controllerMethods");
        UrlMethodDTO urlMethodDTO = new UrlMethodDTO(this.getPathAfterBaseURL(req), req.getMethod());
        ControllerMethod ControllerMethod = this.getControllerMethodByUrlMethod(urlMethodDTO, controllerMethods);
        ControllerMethod.executeMethod(req, res, servletContext);
    }

    public String getPathAfterBaseURL(HttpServletRequest req) {
        String baseURL = req.getContextPath();
        String requestURL = req.getRequestURI();
        return requestURL.substring(baseURL.length());
    }

    public ControllerMethod getControllerMethodByUrlMethod(UrlMethodDTO urlMethodDTO,
            Map<UrlMethodDTO, ControllerMethod> controllerMethods) {
        ControllerMethod ret = controllerMethods.get(urlMethodDTO);
        if (ret != null) {
            return ret;
        } else {
            throw new NoMethodUrlException("No method url found for url: " + urlMethodDTO);

        }
    }

    public List<String> splitParamName(String paramName) {
        return List.of(paramName.split("."));
    }

    public void buildParamObject(Object paramReturn, Parameter param, HttpServletRequest request) {

    }

}
