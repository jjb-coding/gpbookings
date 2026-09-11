package util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

public class ReflectionUtil {
    public static ArrayList<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
    	ArrayList<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            
            File directory = new File(resource.getFile());
            if (directory.exists()) {
                File[] files = directory.listFiles();
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".class")) {
                        String className = packageName + '.' + 
                            file.getName().substring(0, file.getName().length() - 6);
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
        
        return classes;
    }
    
    public static ArrayList<Class<?>> getNamedClasses(String packageName) throws ClassNotFoundException, IOException {
    	ArrayList<Class<?>> in = getClasses(packageName);
    	ArrayList<Class<?>> out = new ArrayList<>();
    	for (int i = 0; i < in.size(); i++)
    		if (!in.get(i).isAnonymousClass())
    			out.add(in.get(i));
    	return out;
    }
}
