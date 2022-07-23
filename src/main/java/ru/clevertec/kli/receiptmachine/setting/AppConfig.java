package ru.clevertec.kli.receiptmachine.setting;

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
import ru.clevertec.kli.receiptmachine.repository.factory.RepositoryFactory;
import ru.clevertec.kli.receiptmachine.repository.factory.impl.JdbcRepositoryFactory;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;
import ru.clevertec.kli.receiptmachine.util.database.datasource.DataSource;
import ru.clevertec.kli.receiptmachine.util.parse.args.OptionalParser;
import ru.clevertec.kli.receiptmachine.util.parse.args.ParseCartHelper;

@Configuration
@EnableWebMvc
@ComponentScan("ru.clevertec.kli.receiptmachine")
@PropertySource("classpath:receipt.properties")
public class AppConfig {

    @Value("${calc.countOfPromotionalProductsToGetDiscount}")
    private int countOfPromotionalProductsToGetDiscount;
    @Value("${calc.promotionalProductDiscountPercent}")
    private float promotionalProductDiscountPercent;

    @Bean
    public ModelMapperExt modelMapper() {
        return new ModelMapperExt();
    }

    @Bean
    public RepositoryFactory repositoryFactory(DataSource dataSource) {
        return new JdbcRepositoryFactory(dataSource);
    }

    @Bean
    @Scope("prototype")
    public ParseCartHelper parseCartHelper(OptionalParser<PurchaseDto> purchaseParser,
        OptionalParser<CardDto> cardParser) {

        return new ParseCartHelper(purchaseParser, cardParser);
    }

    @Bean
    public Repository<Product> productRepository(RepositoryFactory factory) {
        return factory.getProductRepository();
    }

    @Bean
    public Repository<DiscountCard> discountCardRepository(RepositoryFactory factory) {
        return factory.getDiscountCardRepository();
    }

    @Bean
    public Repository<Receipt> receiptRepository(RepositoryFactory factory) {
        return factory.getReceiptRepository();
    }

    @Bean
    public Repository<ReceiptPosition> receiptPositionRepository(RepositoryFactory factory) {
        return factory.getReceiptPositionRepository();
    }

    @Bean
    public int countOfPromotionalProductsToGetDiscount() {
        return countOfPromotionalProductsToGetDiscount;
    }

    @Bean
    public float promotionalProductDiscountPercent() {
        return promotionalProductDiscountPercent;
    }
}
