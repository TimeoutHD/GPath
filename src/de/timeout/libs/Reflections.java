package de.timeout.libs;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Reflections {
		
	private static final Field modifiers = getField(Field.class, "modifiers");
	
	private Reflections() {}

	public static Field getField(Class<?> clazz, String name) {
			try {
				Field field = clazz.getDeclaredField(name);
			    field.setAccessible(true);
			      
			    if (Modifier.isFinal(field.getModifiers()))modifiers.set(field, field.getModifiers() & ~Modifier.FINAL);
			    return field;
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				Logger.getGlobal().log(Level.WARNING, "Could not find Field " + name + " in Class " + clazz.getName(), e);
			}
	return null;
	}
	
	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
		try {
			Constructor<?> constructor = clazz.getConstructor(params);
			if(Modifier.isPrivate(constructor.getModifiers())) modifiers.set(constructor, constructor.getModifiers() & ~Modifier.PRIVATE);
			return constructor;
		} catch (NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			Logger.getGlobal().log(Level.SEVERE, "Constructor with parameters does not exist", e);
		}
		return null;
	}
	
	public static Field getField(Object obj, String name) {
		try {
			return obj.getClass().getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			Logger.getGlobal().log(Level.WARNING, "Could not find Field " + name + "in Class " + obj.getClass().getName(), e);
		}
		return null;
	}
	
	public static Object getValue(Field field, Object obj) {
		try {
			field.setAccessible(true);
			return field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			Logger.getGlobal().log(Level.SEVERE, "Could not get Value from Field " + field.getName() + " in " + obj, e);
		}
		return null;
	}
	
	public static Class<?> getSubClass(Class<?> overclass, String classname) {
		Class<?>[] underclasses = overclass.getClasses();
		for(Class<?> underclass : underclasses) {
			if(underclass.getName().equalsIgnoreCase(overclass.getName() + "$" + classname))return underclass;
		}
		return null;
	}
	

	public static Class<?> getClass(String classpath) {
		try {
			return Class.forName(classpath);
		} catch (ClassNotFoundException e) {
			Logger.getGlobal().log(Level.SEVERE, "Class " + classpath + " not found", e);
		}
		return null;
	}
	
	public static void setField(Field field, Object obj, Object value) {
		try {
			field.setAccessible(true);
			field.set(obj, value);
			field.setAccessible(false);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			Logger.getGlobal().log(Level.SEVERE, "Could not set Value " + value.getClass().getName() + " in Field " + field.getName() + " in Class " + obj.getClass().getName(), e);
		}
	}
	
	public static <T> Field getField(Class<?> target, String name, Class<T> fieldtype) {
		for(Field field : target.getDeclaredFields()) {
			if((name == null || field.getName().equals(name)) && fieldtype.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				return field;
			}
		}
		return null;
	}
}