package mg.itu.tsimbina.view;

import java.util.HashMap;
import java.util.Map;
public class ModelAndView {
    private String viewName;
    private Map<String, Object> attributes;

    public ModelAndView(String viewName) {
        this.viewName = viewName;   
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void addObject(String key, Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }

    public static ModelAndView toModelAndView(Object object){
        if (object==null) {
            return null;
        }
        if (object instanceof String) {
            return new ModelAndView((String) object);
        } else if (object instanceof ModelAndView) {
            return (ModelAndView) object;
        } else {
            throw new IllegalArgumentException("Object doit être un String ou un ModelAndView");
        }
    }



}

