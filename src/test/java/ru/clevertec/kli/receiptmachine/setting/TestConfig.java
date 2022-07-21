package ru.clevertec.kli.receiptmachine.setting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.CardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.PurchaseDto;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;
import ru.clevertec.kli.receiptmachine.util.parse.args.OptionalParser;
import ru.clevertec.kli.receiptmachine.util.parse.args.ParseCartHelper;

@Configuration
@ComponentScan("ru.clevertec.kli.receiptmachine.util")
public class TestConfig {

    @Bean
    public ModelMapperExt modelMapperExt() {
        return new ModelMapperExt();
    }

    @Bean
    @Scope("prototype")
    public ParseCartHelper parseCartHelper(OptionalParser<PurchaseDto> purchaseParser,
        OptionalParser<CardDto> cardParser) {

        return new ParseCartHelper(purchaseParser, cardParser);
    }
}
