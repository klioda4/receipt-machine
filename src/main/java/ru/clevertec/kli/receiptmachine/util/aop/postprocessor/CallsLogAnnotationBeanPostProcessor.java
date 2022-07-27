package ru.clevertec.kli.receiptmachine.util.aop.postprocessor;

import ru.clevertec.kli.receiptmachine.util.aop.annotation.CallsLog;
import ru.clevertec.kli.receiptmachine.util.aop.handler.CallsLogInvocationHandler;

public class CallsLogAnnotationBeanPostProcessor extends ProxyBeanPostProcessor {

    public CallsLogAnnotationBeanPostProcessor() {
        super(CallsLog.class);
        setInvocationHandlerProducer(CallsLogInvocationHandler::new);
    }
}
