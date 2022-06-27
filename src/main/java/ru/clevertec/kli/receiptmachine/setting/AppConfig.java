package ru.clevertec.kli.receiptmachine.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.util.ArrayList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.CardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.PurchaseDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.Receipt;
import ru.clevertec.kli.receiptmachine.pojo.entity.ReceiptPosition;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.repository.impl.LocalRepository;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;
import ru.clevertec.kli.receiptmachine.util.parse.Parsable;
import ru.clevertec.kli.receiptmachine.util.parse.ParseCartHelper;

@Configuration
@EnableWebMvc
@ComponentScan("ru.clevertec.kli.receiptmachine")
@PropertySource("classpath:receipt.properties")
public class AppConfig {

    @Bean
    public ModelMapperExt modelMapper() {
        return new ModelMapperExt();
    }

    @Bean
    public Repository<Receipt> receiptRepository() {
        return new LocalRepository<>(new ArrayList<>());
    }

    @Bean
    public Repository<ReceiptPosition> receiptPositionRepository() {
        return new LocalRepository<>(new ArrayList<>());
    }

    @Bean
    @Scope("prototype")
    public ParseCartHelper parseCartHelper(Parsable<PurchaseDto> purchaseParser,
        Parsable<CardDto> cardParser) {

        return new ParseCartHelper(purchaseParser, cardParser);
    }
}
