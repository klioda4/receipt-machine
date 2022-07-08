package ru.clevertec.kli.receiptmachine.util.spring.aop.postprocessor;

import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import ru.clevertec.kli.receiptmachine.util.spring.aop.annotation.CallsLog;
import ru.clevertec.kli.receiptmachine.util.spring.aop.handler.CallsLogProxyInvocationHandler;

@RequiredArgsConstructor
public class CallsLogAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final HashMap<String, Class<?>> involvedBeans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
        throws BeansException {

        Class<?> beanClass = bean.getClass();
        if (beanClass.getAnnotation(CallsLog.class) != null) {
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
        var handler = new CallsLogProxyInvocationHandler(bean, new PrintWriter(System.out));
        return Proxy.newProxyInstance(originalClass.getClassLoader(), originalClass.getInterfaces(),
            handler);
    }
}
