package protocol;

import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public abstract class RequestBody implements Serializable {
    public static <T> T parseJson(Class<T> type, String json ){
        return new Gson().fromJson(json,type);
    }
    public static <T> T  parse(Class<T> type, Map<String, Object> properties) {
        T bean = null;
        try {
            bean = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Class<?> clazz = bean.getClass();
        while(clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                    if (properties.containsKey(field.getName())) {
                        field.setAccessible(true);
                        try {
                            field.set(bean, properties.get(field.getName()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return bean;
    }
}
