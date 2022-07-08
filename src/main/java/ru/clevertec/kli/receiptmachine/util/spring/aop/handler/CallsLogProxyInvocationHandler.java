package ru.clevertec.kli.receiptmachine.util.spring.aop.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import ru.clevertec.kli.receiptmachine.util.serialize.gson.LocalDateTimeGsonSerializer;
import ru.clevertec.kli.receiptmachine.util.spring.aop.dto.CallsLogItem;

@RequiredArgsConstructor
public class CallsLogProxyInvocationHandler implements InvocationHandler {

    private final Object originalObject;
    private final PrintWriter logWriter;
    private final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGsonSerializer())
        .create();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
}
