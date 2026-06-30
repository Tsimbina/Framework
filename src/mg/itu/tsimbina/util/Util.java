package mg.itu.tsimbina.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.tsimbina.annotation.Controller;
import mg.itu.tsimbina.annotation.UrlMapping;
import mg.itu.tsimbina.dto.ControllerMethodUrlDTO;
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

    public List<Class<?>> getClasses(String packagesName, Class<?> annotationClass) {
        List<Class<?>> listClasses = getClasses(packagesName);
        List<Class<?>> listAnnotatedClasses = new ArrayList<>();

        for (Class<?> elem : listClasses) {
            if (elem.isAnnotationPresent((Class<java.lang.annotation.Annotation>) annotationClass)) {
                listAnnotatedClasses.add(elem);
            }
        }
        return listAnnotatedClasses;
    }

    public List<Class<?>> getClasses(String packagesName, Map<UrlMethodDTO, ControllerMethodUrlDTO> controlerMethods) throws DupicatedUrl {
        List<Class<?>> listClasses = getClasses(packagesName);
        List<Class<?>> listAnnotatedClasses = new ArrayList<>();

        for (Class<?> elem : listClasses) {
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
                        controlerMethods.put(urlMethodDTO, new ControllerMethodUrlDTO(elem, method));
                    }

                }
            }
        }
        return listAnnotatedClasses;
    }
    public String getPathAfterBaseURL(HttpServletRequest req) {
        String baseURL = req.getContextPath();
        String requestURL = req.getRequestURI();
        return requestURL.substring(baseURL.length());
    }

    public ControllerMethodUrlDTO getControllerMethodUrlDTOByUrlMethod(UrlMethodDTO urlMethodDTO, Map<UrlMethodDTO, ControllerMethodUrlDTO> controllerMethods) {
        ControllerMethodUrlDTO ret = controllerMethods.get(urlMethodDTO);
        if (ret != null) {
            return ret;
        } else {
            throw new NoMethodUrlException("No method url found for url: " + urlMethodDTO);

        }
    }
    

   
}
