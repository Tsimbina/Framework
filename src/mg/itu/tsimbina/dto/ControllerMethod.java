package mg.itu.tsimbina.dto;

import java.lang.reflect.Method;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import mg.itu.tsimbina.view.ModelAndView;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
public class ControllerMethod{

    private Class<?> controllerClass;
    private Object controllerInstance;

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

    public void executeMethod(HttpServletRequest request, HttpServletResponse response, ViewPathDTO viewPathDTO) {
        if (controllerInstance == null) {
            try {
                controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create controller instance: " + e.getMessage(), e);
            }
        }
        try {
            response.setContentType("text/html;charset=UTF-8");

            Object o = method.invoke(controllerInstance);
            ModelAndView mv = ModelAndView.toModelAndView(o);
            String viewPath = viewPathDTO.formatView(mv);
            for (Map.Entry<String, Object> en : mv.getAttributes().entrySet()) {
                request.setAttribute(en.getKey(), en.getValue());
            }
            RequestDispatcher dispat = request.getRequestDispatcher(viewPath);
            System.out.println("dispatch to executed");
            dispat.forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke method: " + e.getMessage(), e);
        }
    }
}
