package mg.itu.tsimbina.util;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.tsimbina.annotation.Model;

import java.lang.reflect.Field;

public class Binder {

    public static <T> T bind(
            Map<String, String[]> parameters,
            String prefix,
            Class<T> clazz
    ) throws Exception {

        T objet = clazz.getDeclaredConstructor().newInstance();

        //Map<String, String[]> parameters = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {

            String key = entry.getKey();

            // Vérifie que le paramètre commence par "client."
            String debut = prefix + ".";

            if (!key.startsWith(debut)) {
                continue;
            }
            // Exemple :
            // client.nom -> nom
            String fieldName = key.substring(debut.length());
            String[] parts = fieldName.split("\\.");
            Field field = clazz.getDeclaredField(parts[0]);

            field.setAccessible(true);
            String value = entry.getValue()[0];
            Object valeurConvertie= null;
            if (field.getType().isAnnotationPresent(Model.class)) {
                 valeurConvertie = bind(parameters, fieldName, field.getType());
            } else {
                 valeurConvertie = convertir(value, field.getType());
            }
            field.set(objet, valeurConvertie);
        }

        return objet;
    }

    private static Object convertir(String value, Class<?> type) {

        if (type == String.class) {
            return value;
        }

        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        }

        if (type == long.class || type == Long.class) {
            return Long.parseLong(value);
        }

        if (type == double.class || type == Double.class) {
            return Double.parseDouble(value);
        }

        if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(value);
        }

        throw new IllegalArgumentException(
                "Type non supporté : " + type
        );
    }
}
