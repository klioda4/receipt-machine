package ru.clevertec.kli.receiptmachine.util.aop.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.kli.receiptmachine.util.aop.annotation.CallsLog;
import ru.clevertec.kli.receiptmachine.util.aop.dto.CallsLogItem;
import ru.clevertec.kli.receiptmachine.util.aop.helper.ProxyUtils;
import ru.clevertec.kli.receiptmachine.util.serialize.gson.LocalDateTimeGsonSerializer;

public class CallsLogInvocationHandler implements InvocationHandler {

    private final Object originalObject;
    private final Class<?> originalClass;

    private final Logger logger;
    private final Gson gson;

    public CallsLogInvocationHandler(Object originalObject, Class<?> originalClass) {
        this.originalObject = originalObject;
        this.originalClass = originalClass;
        logger = LogManager.getLogger(originalClass);
        gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGsonSerializer())
            .create();
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Optional<CallsLog> optional = ProxyUtils.findAnnotation(method, originalClass,
                CallsLog.class);
            if (optional.isEmpty()) {
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
            CallsLog annotation = optional.get();
            logger.log(toLog4gLevel(annotation.value()), gson.toJson(logItem));

            return result;
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private org.apache.logging.log4j.Level toLog4gLevel(java.lang.System.Logger.Level level) {
        switch (level) {
            case OFF:
                return Level.OFF;
            case ERROR:
                return Level.ERROR;
            case WARNING:
                return Level.WARN;
            case INFO:
                return Level.INFO;
            case DEBUG:
                return Level.DEBUG;
            case TRACE:
                return Level.TRACE;
            case ALL:
                return Level.ALL;
            default:
                throw new IllegalArgumentException();
        }
    }
}
