package ru.clevertec.kli.receiptmachine.util.aop.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Savepoint;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import ru.clevertec.kli.receiptmachine.util.aop.annotation.Transactional;
import ru.clevertec.kli.receiptmachine.util.aop.helper.AnnotationFinder;
import ru.clevertec.kli.receiptmachine.util.database.transaction.TransactionManager;

@RequiredArgsConstructor
public class TransactionalInvocationHandler implements InvocationHandler {

    private final Object originalObject;
    private final Class<?> originalClass;

    private final TransactionManager transactionManager;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Optional<Transactional> optional = AnnotationFinder.findAnnotation(method,
                originalClass, Transactional.class);
            if (optional.isEmpty()) {
                return method.invoke(originalObject, args);
            }

            Savepoint savepoint = transactionManager.begin();
            try {
                Object result = method.invoke(originalObject, args);
                transactionManager.finish(savepoint);
                return result;
            } catch (Exception e) {
                transactionManager.rollback(savepoint);
                throw e;
            }
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
