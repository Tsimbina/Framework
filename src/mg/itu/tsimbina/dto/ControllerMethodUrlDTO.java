package mg.itu.tsimbina.dto;

import java.lang.reflect.Method;

public class ControllerMethodUrlDTO {

    private Class<?> controllerClass;
    private Object controllerInstance;

    public ControllerMethodUrlDTO(Class<?> controllerClass, Method method) {
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

    public void executeMethod() {
        if (controllerInstance == null) {
            try {
                controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

            } catch (Exception e) {
                throw new RuntimeException("Failed to create controller instance: " + e.getMessage(), e);
            }
        }
        try {
            method.invoke(controllerInstance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke method: " + e.getMessage(), e);
        }
    }
}
