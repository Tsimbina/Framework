package mg.itu.tsimbina.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

public class MySession {
    Map<String, Object> attribut;

    public MySession() {

    }

    public void getSession(HttpSession session) {
        List<String> nomAttribut = Collections.list(session.getAttributeNames());
        attribut = new HashMap<>();
        for (String nom : nomAttribut) {
            attribut.put(nom, session.getAttribute(nom));
        }
    }

    public void persist(HttpSession session) {
        List<String> nomAttribut = Collections.list(session.getAttributeNames());
        for (String nom : nomAttribut) {
            session.removeAttribute(nom);
        }
        if (attribut == null) {
            attribut = new HashMap<>();
        }
        for (String nom : attribut.keySet()) {
            session.setAttribute(nom, attribut.get(nom));
        }
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
