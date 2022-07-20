package ru.clevertec.kli.receiptmachine.util.aop.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import ru.clevertec.kli.receiptmachine.util.aop.annotation.CallsLog;
import ru.clevertec.kli.receiptmachine.util.aop.dto.CallsLogItem;
import ru.clevertec.kli.receiptmachine.util.serialize.gson.LocalDateTimeGsonSerializer;

@RequiredArgsConstructor
public class CallsLogProxyInvocationHandler implements InvocationHandler {

    private final Object originalObject;
    private final Class<?> originalClass;
    private final PrintWriter logWriter;
    private final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGsonSerializer())
        .create();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!originalClass.isAnnotationPresent(CallsLog.class)
            && !method.isAnnotationPresent(CallsLog.class)
            && !isAnnotationPresentOnMethodLike(method)) {

            return method.invoke(originalObject, args);
        }

        LocalDateTime dateTime = LocalDateTime.now();
        Object result = method.invoke(originalObject, args);

        CallsLogItem logItem = CallsLogItem.builder()
            .methodName(originalObject.getClass().getSimpleName() + '.' + method.getName())
            .callTime(dateTime)
            .arguments(args)
            .methodResult(result)
            .build();
        logWriter.println(gson.toJson(logItem));
        logWriter.flush();
        return result;
    }

    private boolean isAnnotationPresentOnMethodLike(Method signatureExample) {
        String methodName = signatureExample.getName();
        Class<?>[] parameterTypes = signatureExample.getParameterTypes();
        try {
            Method methodToCheck = originalClass.getMethod(methodName, parameterTypes);
            return methodToCheck.isAnnotationPresent(CallsLog.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Incorrect class or method was specified", e);
        }
    }
}
