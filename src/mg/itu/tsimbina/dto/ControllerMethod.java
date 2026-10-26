package mg.itu.tsimbina.dto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import mg.itu.tsimbina.annotation.Auth;
import mg.itu.tsimbina.annotation.WebApi;
import mg.itu.tsimbina.exception.NonAuthorizeException;
import mg.itu.tsimbina.model.MySession;
import mg.itu.tsimbina.util.Binder;
import mg.itu.tsimbina.util.Util;
import mg.itu.tsimbina.view.ModelAndView;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Parameter;

public class ControllerMethod {

    private Class<?> controllerClass;

    public ControllerMethod(Class<?> controllerClass, Object controllerInstance, Method method) {
        this.controllerClass = controllerClass;
        this.controllerInstance = controllerInstance;
        this.method = method;
    }

    private Object controllerInstance;

    public Object getControllerInstance() {
        return controllerInstance;
    }

    public void setControllerInstance(Object controllerInstance) {
        this.controllerInstance = controllerInstance;
    }

    public ControllerMethod(Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    private Method method;

    @Override
    public String toString() {
        return " controller: " + controllerClass.getName() + "-> " + getMethod().getName() + "()";
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void executeMethod(HttpServletRequest request, HttpServletResponse response,
            ServletContext servletContext) {
        ViewPathDTO viewPathDTO = (ViewPathDTO) servletContext.getAttribute("viewPathDTO");
        if (controllerInstance == null) {
            try {
                controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create controller instance: " + e.getMessage(), e);
            }
        }
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

        Map<String, MySession> mapSession = (Map<String, MySession>) servletContext.getAttribute("MapSession");

        String sessionId = request.getSession().getId();
        String authKey = (String) servletContext.getAttribute("authKey");

        // Initialisation de la session personnalisée
        MySession ms = mapSession.get(sessionId);

        if (ms == null) {
            ms = new MySession(request.getSession());
            mapSession.put(sessionId, ms);
        }

        ms.refreshSession();
        if (method.isAnnotationPresent(Auth.class)) {
            if (mapSession.containsKey(sessionId)) {
                if (!mapSession.get(sessionId).containsKey(authKey)) {
                    throw new NonAuthorizeException(authKey);
                }
            } else {
                throw new NonAuthorizeException(authKey);
            }

        }

        try {
            // Class<?>[] parameterTypes = method.getParameterTypes();
            Parameter[] param = method.getParameters();
            Object[] parameters = new Object[param.length];
            Map<String, String[]> parametersMap = request.getParameterMap();

            List<String> keys = new ArrayList<>(parametersMap.keySet());
            Collections.sort(keys);
            for (int i = 0; i < param.length; i++) {
                if (param[i].getType().equals(ApplicationContext.class)) {
                    parameters[i] = ctx;
                } else if (param[i].getType().equals(MySession.class)) {
                    parameters[i] = ms;

                }

                else {
                    Class<?> type = param[i].getType();
                    String valeur = request.getParameter(param[i].getName());
                    Part[] parts = request.getParts().toArray(new Part[0]);
                    parameters[i] = Binder.bind(parametersMap, parts, param[i].getName(), type);

                }
            }

            if (method.isAnnotationPresent(WebApi.class)) {

                Object o = method.invoke(controllerInstance, parameters);
                ms.persist();

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(new ObjectMapper().writeValueAsString(o));

                // mapper.writeValue(response.getWriter(), method.invoke(controllerInstance,
                // parameters));
            } else {
                response.setContentType("text/html;charset=UTF-8");

                Object o = method.invoke(controllerInstance, parameters);
                ms.persist();

                // if (o.getClass().isInstance(Strin)) {
                //
                // }
                ModelAndView mv = ModelAndView.toModelAndView(o);
                String viewPath = viewPathDTO.formatView(mv);
                for (Map.Entry<String, Object> en : mv.getAttributes().entrySet()) {
                    request.setAttribute(en.getKey(), en.getValue());
                }
                RequestDispatcher dispat = request.getRequestDispatcher(viewPath);

                System.out.println(
                        "dispatch to executed");
                dispat.forward(request, response);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke method: " + e.getMessage(), e);
        }
    }
}
