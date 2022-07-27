package ru.clevertec.kli.receiptmachine.util.aop.postprocessor;

import java.lang.reflect.InvocationHandler;
import java.util.function.BiFunction;
import org.springframework.beans.BeansException;
import ru.clevertec.kli.receiptmachine.util.aop.annotation.Transactional;
import ru.clevertec.kli.receiptmachine.util.aop.handler.TransactionalInvocationHandler;
import ru.clevertec.kli.receiptmachine.util.database.transaction.TransactionManager;

public class TransactionalAnnotationBeanPostProcessor extends ProxyBeanPostProcessor {

    public TransactionalAnnotationBeanPostProcessor() {
        super(Transactional.class);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
        throws BeansException {

        if (bean instanceof TransactionManager) {
            var transactionManager = (TransactionManager) bean;
            var handlerProducer = getHandlerProducer(transactionManager);
            setInvocationHandlerProducer(handlerProducer);
        }
        return super.postProcessBeforeInitialization(bean, beanName);
    }

    private BiFunction<Object, Class<?>, InvocationHandler> getHandlerProducer(
        TransactionManager transactionManager) {

        return (beanObject, beanClass) -> new TransactionalInvocationHandler(beanObject,
            beanClass, transactionManager);
    }
}
