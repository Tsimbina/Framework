package mg.itu.tsimbina.dto;

import java.lang.reflect.Method;

public class ControllerMethodUrlDTO {

    private Class<?> controllerClass;
    public ControllerMethodUrlDTO(Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;
    }

    private Method method;

    @Override
    public String toString(){
        return " controller: "+getControllerClass().getName()+"-> "+getMethod().getName()+"()";
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
}
