package ru.clevertec.kli.receiptmachine.util.aop.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

public class AnnotationFinder {

    public static <T extends Annotation> Optional<T> findAnnotation(Method method,
        Class<?> originalClass, Class<T> annotationClass) {

        Optional<T> annotation;
        annotation = getClassAnnotation(originalClass, annotationClass);
        if (annotation.isPresent()) {
            return annotation;
        }
        annotation = getInterfaceAnnotation(method, annotationClass);
        if (annotation.isPresent()) {
            return annotation;
        }
        return getMethodAnnotationOfDifferentClass(method, originalClass, annotationClass);
    }

    private static <T extends Annotation> Optional<T> getClassAnnotation(Class<?> originalClass,
        Class<T> annotationClass) {

        return Optional.ofNullable(
            originalClass.getAnnotation(annotationClass));
    }

    private static <T extends Annotation> Optional<T> getInterfaceAnnotation(Method method,
        Class<T> annotationClass) {

        return Optional.ofNullable(
            method.getAnnotation(annotationClass));
    }

    private static <T extends Annotation> Optional<T> getMethodAnnotationOfDifferentClass(
        Method signatureExample, Class<?> cl, Class<T> annotationClass) {

        String methodName = signatureExample.getName();
        Class<?>[] parameterTypes = signatureExample.getParameterTypes();
        try {
            Method methodToCheck = cl.getMethod(methodName, parameterTypes);
            return Optional.ofNullable(
                methodToCheck.getAnnotation(annotationClass));
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Incorrect class or method was specified", e);
        }
    }
}
