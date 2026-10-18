package mg.itu.tsimbina.util;

import java.util.Map;

import jakarta.servlet.http.Part;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import mg.itu.tsimbina.annotation.Model;
import mg.itu.tsimbina.model.Fichier;

public class Binder {

    public static <T> T bind(
            Map<String, String[]> parameters,
            Part[] parts,
            String prefix,
            Class<T> clazz
    ) throws Exception {

        T objet = clazz.getDeclaredConstructor().newInstance();
        
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {

            String key = entry.getKey();

            String debut = prefix + ".";

            if (!key.startsWith(debut)) {
                continue;
            }

            // Exemple : client.nom -> nom
            String fieldName = key.substring(debut.length());

            String[] parties = fieldName.split("\\.");

            // Récupérer le champ
            Field field = clazz.getDeclaredField(parties[0]);

            String value = entry.getValue()[0];

            Object valeurConvertie;

            if (field.getType().isAnnotationPresent(Model.class)) {

                // Gestion d'un objet imbriqué
                valeurConvertie = bind(
                        parameters,
                        parts,
                        fieldName,
                        field.getType()
                );

            } else {

                valeurConvertie = convertir(value, field.getType());
            }

            // Construire le nom du setter
            String nomSetter = "set"
                    + Character.toUpperCase(parties[0].charAt(0))
                    + parties[0].substring(1);

            // Récupérer le setter
            Method setter = clazz.getMethod(
                    nomSetter,
                    field.getType()
            );

            // Appeler le setter
            setter.invoke(objet, valeurConvertie);
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