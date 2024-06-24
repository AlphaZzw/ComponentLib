package com.neteasecloud.componentlib.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 反射工具类，可缓存反射。
 * For example:
 * Canvas canvas = new Canvas(bitmap);
 * try {
 *  ReflectUtils.from(canvas).method("setNightModeUseOf", new Class[]{int.class}).invoke(canvas, 2);
 * } catch (Exception ignored) {
 * }
 */
public class ReflectUtils {

    private static IReflect sReflect = new CacheReflect();

    public static IReflectClass from(Object object) throws ClassNotFoundException {
        return sReflect.from(object);
    }

    public static IReflectClass from(Class<?> clazz) throws ClassNotFoundException {
        return sReflect.from(clazz);
    }

    public static IReflectClass from(ClassLoader classLoader, String className) throws ClassNotFoundException {
        return sReflect.from(classLoader, className);
    }

    public static IReflectClass from(String className) throws ClassNotFoundException {
        return sReflect.from(className);
    }

    private static class CacheReflect implements IReflect {
        private Map<ClassLoader, Map<String, IReflectClass>> mCacheClass = new HashMap<>();

        @Override
        public IReflectClass from(Object object) throws ClassNotFoundException {
            return from(object.getClass());
        }

        @Override
        public IReflectClass from(Class<?> clazz) throws ClassNotFoundException {
            return from(clazz.getClassLoader(), clazz.getName());
        }

        @Override
        public IReflectClass from(ClassLoader classLoader, String className) throws ClassNotFoundException {

            Map<String, IReflectClass> map = mCacheClass.get(classLoader);
            if (map == null) {
                map = new HashMap<>();
                mCacheClass.put(classLoader, map);
            }

            IReflectClass reflectClass = map.get(className);
            if (reflectClass == null) {
                Class<?> clazz = classLoader.loadClass(className);
                reflectClass = new DefaultReflectClass(clazz);
                map.put(className, reflectClass);
            }
            return reflectClass;
        }

        @Override
        public IReflectClass from(String className) throws ClassNotFoundException {
            return from(getClass().getClassLoader(), className);
        }
    }

    private static class DefaultReflectClass implements IReflectClass {
        private Class<?> mClass;
        private Map<String, IReflectConstructor> mConstructors = new HashMap<>();
        private Map<String, IReflectMethod> mMethods = new HashMap<>();
        private Map<String, IReflectField> mFields = new HashMap<>();

        DefaultReflectClass(Class<?> clazz) {
            mClass = clazz;
        }

        @Override
        public Class<?> clazz() {
            return mClass;
        }

        @Override
        public IReflectConstructor constructor(Class... parameterTypes) throws NoSuchMethodException {
            StringBuilder builder = new StringBuilder();
            if (parameterTypes != null && parameterTypes.length > 0) {
                for (Class parameterType : parameterTypes) {
                    builder.append(parameterType.getName());
                }
            }
            String key = builder.toString();
            IReflectConstructor reflectConstructor = mConstructors.get(key);
            if (reflectConstructor == null) {
                Constructor<?> constructor = mClass.getConstructor(parameterTypes);
                reflectConstructor = new DefaultReflectConstructor(constructor);
                mConstructors.put(key, reflectConstructor);
            }
            return reflectConstructor;
        }

        @Override
        public IReflectMethod method(String methodName, Class... parameterTypes) throws NoSuchMethodException {
            StringBuilder builder = new StringBuilder(methodName);
            if (parameterTypes != null && parameterTypes.length > 0) {
                for (Class parameterType : parameterTypes) {
                    builder.append(parameterType.getName());
                }
            }
            String key = builder.toString();
            IReflectMethod reflectMethod = mMethods.get(key);
            if (reflectMethod == null) {
                Method method = null;
                for (Class<?> clazz = mClass; clazz != null; clazz = clazz.getSuperclass()) {
                    try {
                        method = clazz.getDeclaredMethod(methodName, parameterTypes);
                        break;
                    } catch (Exception ignored) {
                    }
                }
                if (method == null) {
                    throw new NoSuchMethodException(methodName + " " + Arrays.toString(parameterTypes));
                }
                reflectMethod = new DefaultReflectMethod(method);
                mMethods.put(key, reflectMethod);
            }
            return reflectMethod;
        }

        @Override
        public IReflectField field(String fieldName) throws NoSuchFieldException {
            IReflectField reflectField = mFields.get(fieldName);
            if (reflectField == null) {
                Field field = null;
                for (Class<?> clazz = mClass; clazz != null; clazz = clazz.getSuperclass()) {
                    try {
                        field = clazz.getDeclaredField(fieldName);
                        break;
                    } catch (Exception ignored) {
                    }
                }
                if (field == null) {
                    throw new NoSuchFieldException(fieldName);
                }
                reflectField = new DefaultReflectField(field);
                mFields.put(fieldName, reflectField);
            }
            return reflectField;
        }
    }

    private static class DefaultReflectConstructor implements IReflectConstructor {

        private Constructor<?> mConstructor;

        DefaultReflectConstructor(Constructor<?> constructor) {
            mConstructor = constructor;
            mConstructor.setAccessible(true);
        }

        @Override
        public Constructor constructor() {
            return mConstructor;
        }

        @Override
        public Object newInstance(Object... args) throws
                IllegalArgumentException,
                IllegalAccessException,
                InvocationTargetException,
                InstantiationException {
            return mConstructor.newInstance(args);
        }
    }

    private static class DefaultReflectMethod implements IReflectMethod {

        private Method mMethod;

        DefaultReflectMethod(Method method) {
            mMethod = method;
            mMethod.setAccessible(true);
        }

        @Override
        public Method method() {
            return mMethod;
        }

        @Override
        public Object invoke(Object receiver, Object... args) throws
                InvocationTargetException,
                IllegalArgumentException,
                IllegalAccessException {
            return mMethod.invoke(receiver, args);
        }
    }

    private static class DefaultReflectField implements IReflectField {
        private Field mField;

        DefaultReflectField(Field field) {
            mField = field;
            mField.setAccessible(true);
        }

        @Override
        public Field field() {
            return mField;
        }

        @Override
        public Object get(Object o) throws IllegalAccessException, IllegalArgumentException {
            return mField.get(o);
        }

        @Override
        public boolean getBoolean(Object o) throws IllegalAccessException, IllegalArgumentException {
            return mField.getBoolean(o);
        }

        @Override
        public byte getByte(Object o) throws IllegalAccessException, IllegalArgumentException {
            return mField.getByte(o);
        }

        @Override
        public char getChar(Object o) throws IllegalAccessException, IllegalArgumentException {
            return mField.getChar(o);
        }

        @Override
        public short getShort(Object o) throws IllegalAccessException, IllegalArgumentException {
            return mField.getShort(o);
        }

        @Override
        public int getInt(Object o) throws IllegalAccessException, IllegalArgumentException {
            return mField.getInt(o);
        }

        @Override
        public long getLong(Object o) throws IllegalAccessException, IllegalArgumentException {
            return mField.getLong(o);
        }

        @Override
        public float getFloat(Object o) throws IllegalAccessException, IllegalArgumentException {
            return mField.getFloat(o);
        }

        @Override
        public double getDouble(Object o) throws IllegalAccessException, IllegalArgumentException {
            return mField.getDouble(o);
        }

        @Override
        public void set(Object o, Object value) throws IllegalAccessException, IllegalArgumentException {
            mField.set(o, value);
        }

        @Override
        public void setBoolean(Object o, boolean value) throws IllegalAccessException, IllegalArgumentException {
            mField.setBoolean(o, value);
        }

        @Override
        public void setByte(Object o, byte value) throws IllegalAccessException, IllegalArgumentException {
            mField.setByte(o, value);
        }

        @Override
        public void setChar(Object o, char value) throws IllegalAccessException, IllegalArgumentException {
            mField.setChar(o, value);
        }

        @Override
        public void setShort(Object o, short value) throws IllegalAccessException, IllegalArgumentException {
            mField.setShort(o, value);
        }

        @Override
        public void setInt(Object o, int value) throws IllegalAccessException, IllegalArgumentException {
            mField.setInt(o, value);
        }

        @Override
        public void setLong(Object o, long value) throws IllegalAccessException, IllegalArgumentException {
            mField.setLong(o, value);
        }

        @Override
        public void setFloat(Object o, float value) throws IllegalAccessException, IllegalArgumentException {
            mField.setFloat(o, value);
        }

        @Override
        public void setDouble(Object o, double value) throws IllegalAccessException, IllegalArgumentException {
            mField.setDouble(o, value);
        }
    }

    public interface IReflect {
        IReflectClass from(Object object) throws ClassNotFoundException;

        IReflectClass from(Class<?> clazz) throws ClassNotFoundException;

        IReflectClass from(ClassLoader classLoader, String className) throws ClassNotFoundException;

        IReflectClass from(String className) throws ClassNotFoundException;
    }

    public interface IReflectClass {
        Class<?> clazz();

        IReflectConstructor constructor(Class... parameterTypes) throws NoSuchMethodException;

        IReflectMethod method(String methodName, Class... parameterTypes) throws NoSuchMethodException;

        IReflectField field(String fieldName) throws NoSuchFieldException;
    }

    public interface IReflectConstructor {

        Constructor constructor();

        Object newInstance(Object... args) throws
                IllegalArgumentException,
                IllegalAccessException,
                InvocationTargetException,
                InstantiationException;

    }

    public interface IReflectMethod {

        Method method();

        Object invoke(Object receiver, Object... args) throws
                InvocationTargetException,
                IllegalArgumentException,
                IllegalAccessException;
    }

    public interface IReflectField {

        Field field();

        Object get(Object o) throws IllegalAccessException, IllegalArgumentException;

        boolean getBoolean(Object o) throws IllegalAccessException, IllegalArgumentException;

        byte getByte(Object o) throws IllegalAccessException, IllegalArgumentException;

        char getChar(Object o) throws IllegalAccessException, IllegalArgumentException;

        short getShort(Object o) throws IllegalAccessException, IllegalArgumentException;

        int getInt(Object o) throws IllegalAccessException, IllegalArgumentException;

        long getLong(Object o) throws IllegalAccessException, IllegalArgumentException;

        float getFloat(Object o) throws IllegalAccessException, IllegalArgumentException;

        double getDouble(Object o) throws IllegalAccessException, IllegalArgumentException;

        void set(Object o, Object o1) throws IllegalAccessException, IllegalArgumentException;

        void setBoolean(Object o, boolean value) throws IllegalAccessException, IllegalArgumentException;

        void setByte(Object o, byte value) throws IllegalAccessException, IllegalArgumentException;

        void setChar(Object o, char value) throws IllegalAccessException, IllegalArgumentException;

        void setShort(Object o, short value) throws IllegalAccessException, IllegalArgumentException;

        void setInt(Object o, int value) throws IllegalAccessException, IllegalArgumentException;

        void setLong(Object o, long value) throws IllegalAccessException, IllegalArgumentException;

        void setFloat(Object o, float value) throws IllegalAccessException, IllegalArgumentException;

        void setDouble(Object o, double value) throws IllegalAccessException, IllegalArgumentException;
    }
}
