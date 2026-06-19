package main.java.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import main.java.annotation.Controller;

public class Util {
    public  List<String> splitPackage(String packageName) {
        return List.of(packageName.split(";"));
    }
    public void loadClasses(File directory, String packageName,List<Class<?>> listClass) throws Exception{
        if (!directory.exists()) {
            return;
        }
        for(File file:directory.listFiles()){
            if (file.isDirectory()) {
                String sousPackageName = packageName + "." + file.getName();
                loadClasses(file, sousPackageName, listClass);
            } else if (file.getName().endsWith(".class")) {
                String className= packageName+"."+file.getName().replace(".class", "");
                listClass.add(Class.forName(className));
            }
        }
    }
    
    public void loadClassesIn(String packageName,List<Class<?>> listClasses) throws Exception{
        ClassLoader loader= Thread.currentThread().getContextClassLoader();
        String path=packageName.replace('.', '/');
        File directory= new File(loader.getResource(path).getFile());
        loadClasses(directory, packageName, listClasses);
    } 

    public List<Class<?>> getClasses(String packagesName){
        List<Class<?>> listClasses= new  ArrayList<>();
        try {
            for (String elem : splitPackage(packagesName)) {
                loadClassesIn(elem, listClasses);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return  listClasses;
    } 
    public List<Class<?>> getClasses(String packagesName, Class<?> annotationClass){
        List<Class<?>> listClasses= getClasses(packagesName);
        List<Class<?>> listAnnotatedClasses= new  ArrayList<>();

        for (Class<?> elem : listClasses) {
            if (elem.isAnnotationPresent((Class<java.lang.annotation.Annotation>) annotationClass)) {
                listAnnotatedClasses.add(elem);
            }
        }
        return listAnnotatedClasses;
    }

    public List<Class<?>> getControllerClasses(String packagesName){
        return getClasses(packagesName, Controller.class);
    }
}
