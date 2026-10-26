package mg.itu.tsimbina.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

public class MySession {
    Map<String, Object> attribut;
    HttpSession httpSession;

    public MySession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public MySession() {

    }

    public void refreshSession() {
        List<String> nomAttribut = Collections.list(httpSession.getAttributeNames());
        attribut = new HashMap<>();
        for (String nom : nomAttribut) {
            attribut.put(nom, httpSession.getAttribute(nom));
        }
    }

    public void persist() {
        List<String> nomAttribut = Collections.list(httpSession.getAttributeNames());
        for (String nom : nomAttribut) {
            httpSession.removeAttribute(nom);
        }
        if (attribut == null) {
            attribut = new HashMap<>();
        }
        for (String nom : attribut.keySet()) {
            httpSession.setAttribute(nom, attribut.get(nom));
        }
    }
    public boolean containsKey (String key){
        return attribut.containsKey(key);
    }

    public Object get(String name) {
        return this.attribut.get(name);
    }

    public void add(String name, Object attr) {
        this.attribut.put(name, attr);
    }

    public Map<String, Object> getAttribut() {
        return attribut;
    }

    public void setAttribut(Map<String, Object> attribut) {
        this.attribut = attribut;
    }

}
