package ru.clevertec.kli.receiptmachine.setting;

import java.io.IOException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.CardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.PurchaseDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.pojo.entity.Receipt;
import ru.clevertec.kli.receiptmachine.pojo.entity.ReceiptPosition;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.repository.impl.LocalRepository;
import ru.clevertec.kli.receiptmachine.repository.impl.file.FileRepository;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;
import ru.clevertec.kli.receiptmachine.util.io.input.FileObjectReader;
import ru.clevertec.kli.receiptmachine.util.parse.args.OptionalParser;
import ru.clevertec.kli.receiptmachine.util.parse.args.ParseCartHelper;
import ru.clevertec.kli.receiptmachine.util.parse.regex.DiscountCardParser;
import ru.clevertec.kli.receiptmachine.util.parse.regex.ProductParser;

@Configuration
@EnableWebMvc
@ComponentScan("ru.clevertec.kli.receiptmachine")
@PropertySource("classpath:receipt.properties")
public class AppConfig {

    @Value("${objects.input.invalid.filename}")
    private String invalidInputDataFilename;
    @Value("${products.input.filename}")
    private String productsInputFilename;
    @Value("${cards.input.filename}")
    private String cardsInputFilename;
    @Value("${calc.countOfPromotionalProductsToGetDiscount}")
    private int countOfPromotionalProductsToGetDiscount;
    @Value("${calc.promotionalProductDiscountPercent}")
    private float promotionalProductDiscountPercent;

    @Bean
    public ModelMapperExt modelMapper() {
        return new ModelMapperExt();
    }

    @Bean
    public Repository<DiscountCard> discountCardRepository() throws IOException {
        return new FileRepository<>(
            new ArrayList<>(),
            discountCardFileObjectReader());
    }

    @Bean
    public Repository<Product> productRepository() throws IOException {
        return new FileRepository<>(
            new ArrayList<>(),
            productFileObjectReader());
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
    public ParseCartHelper parseCartHelper(OptionalParser<PurchaseDto> purchaseParser,
        OptionalParser<CardDto> cardParser) {

        return new ParseCartHelper(purchaseParser, cardParser);
    }

    @Bean
    public int countOfPromotionalProductsToGetDiscount() {
        return countOfPromotionalProductsToGetDiscount;
    }

    @Bean
    public float promotionalProductDiscountPercent() {
        return promotionalProductDiscountPercent;
    }

    private FileObjectReader<DiscountCard> discountCardFileObjectReader() {
        return new FileObjectReader<>(
            cardsInputFilename,
            invalidInputDataFilename,
            new DiscountCardParser());
    }

    private FileObjectReader<Product> productFileObjectReader() {
        return new FileObjectReader<>(
            productsInputFilename,
            invalidInputDataFilename,
            new ProductParser());
    }
}
