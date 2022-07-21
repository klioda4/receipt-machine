package ru.clevertec.kli.receiptmachine.util.parse.regex;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.kli.receiptmachine.exception.ValidationException;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.util.parse.Parser;

public class ProductParser implements Parser<Product> {

    private static final Logger logger = LogManager.getLogger(ProductParser.class);

    private static final String REGEX_ID = "100|[1-9]\\d?";
    private static final String REGEX_NAME = "[A-ZА-Я][a-zа-я]{2,29}";
    private static final String REGEX_PRICE = '(' + REGEX_ID + ")\\.\\d{2}";
    private static final String REGEX_COUNT = "[12]0|1?[1-9]";
    private static final String REGEX_BOOLEAN = "true|false";
    private static final String REGEX =
        String.format("^(?<id>%s);(?<name>%s);(?<price>%s);(?<count>%s);(?<promotional>%s)$",
            REGEX_ID, REGEX_NAME, REGEX_PRICE, REGEX_COUNT, REGEX_BOOLEAN);

    private final Pattern pattern;

    public ProductParser() {
        pattern = Pattern.compile(REGEX);
    }

    @Override
    public Product parse(String item) throws ValidationException {
        Matcher matcher = pattern.matcher(item);
        if (!matcher.matches()) {
            logger.debug("-\"" + item + "\" failed the Product pattern");
            throw new ValidationException("String \"" + item + "\" does not match the "
                + "Product pattern", item, REGEX);
        }
        logger.debug("+\"" + item + "\" passed the Product pattern");

        Product product = new Product();
        product.setId(
            Integer.parseInt(
                matcher.group("id")));
        product.setName(
            matcher.group("name"));
        product.setPrice(
            new BigDecimal(
                matcher.group("price")));
        product.setCount(
            Integer.parseInt(
                matcher.group("count")));
        product.setPromotional(
            Boolean.parseBoolean(
                matcher.group("promotional")));
        return product;
    }
}
