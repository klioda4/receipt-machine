package ru.clevertec.kli.receiptmachine.util.aop.postprocessor;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.util.function.BiFunction;
import ru.clevertec.kli.receiptmachine.util.aop.annotation.CallsLog;
import ru.clevertec.kli.receiptmachine.util.aop.handler.CallsLogInvocationHandler;

public class CallsLogAnnotationBeanPostProcessor extends ProxyBeanPostProcessor {

    public CallsLogAnnotationBeanPostProcessor() {
        super(CallsLogInvocationHandler::new, CallsLog.class);
    }
}
