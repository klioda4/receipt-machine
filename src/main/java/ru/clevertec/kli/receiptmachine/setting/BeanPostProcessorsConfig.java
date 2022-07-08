package ru.clevertec.kli.receiptmachine.setting;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.kli.receiptmachine.util.spring.aop.postprocessor.CallsLogAnnotationBeanPostProcessor;

@Configuration
public class BeanPostProcessorsConfig {

    @Bean
    public BeanPostProcessor callsLoggingAnnotationBeanPostProcessor() {
        return new CallsLogAnnotationBeanPostProcessor();
    }
}
