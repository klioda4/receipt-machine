package ru.clevertec.kli.receiptmachine.util.aop.postprocessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@RequiredArgsConstructor
public class ProxyBeanPostProcessor implements BeanPostProcessor {

    private final HashMap<String, Class<?>> involvedBeans = new HashMap<>();

    /**
     * Function to get invocation handler using bean instance and its original class
     */
    private final BiFunction<Object, Class<?>, InvocationHandler> invocationHandlerGetter;
    private final Class<? extends Annotation> annotationClass;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
        throws BeansException {

        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(annotationClass)
            || isAnnotationPresentOnAnyMethod(beanClass, annotationClass)) {

            involvedBeans.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
        throws BeansException {

        Class<?> originalClass = involvedBeans.get(beanName);
        if (originalClass == null) {
            return bean;
        }
        InvocationHandler handler = invocationHandlerGetter.apply(bean, originalClass);
        return Proxy.newProxyInstance(originalClass.getClassLoader(), originalClass.getInterfaces(),
            handler);
    }

    private static boolean isAnnotationPresentOnAnyMethod(Class<?> beanClass,
        Class<? extends Annotation> annotationClass) {

        for (Method method : beanClass.getMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }
}
