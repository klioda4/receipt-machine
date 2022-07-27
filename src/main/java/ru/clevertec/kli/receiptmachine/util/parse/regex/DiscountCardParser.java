package ru.clevertec.kli.receiptmachine.util.parse.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.kli.receiptmachine.exception.ValidationException;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.util.parse.Parser;

public class DiscountCardParser implements Parser<DiscountCard> {

    private static final Logger logger = LogManager.getLogger(DiscountCardParser.class);

    private static final String REGEX_NUMBER = "\\d{1,6}";
    private static final String REGEX_DISCOUNT = "100(\\.0*)?|\\d{1,2}(\\.\\d*)?";
    private static final String REGEX =
        String.format("^(?<number>%s);(?<discount>%s)", REGEX_NUMBER, REGEX_DISCOUNT);

    private final Pattern pattern;

    public DiscountCardParser() {
        pattern = Pattern.compile(REGEX);
    }

    @Override
    public DiscountCard parse(String item) throws ValidationException {
        Matcher matcher = pattern.matcher(item);
        if (!matcher.matches()) {
            logger.debug("-\"" + item + "\" failed the Discount card pattern");
            throw new ValidationException("String \"" + item + "\" does not match the "
                + "Discount card pattern", item);
        }
        logger.debug("+\"" + item + "\" passed the Discount card pattern");

        DiscountCard card = new DiscountCard();
        card.setNumber(
            Integer.parseInt(
                matcher.group("number")));
        card.setDiscount(
            Float.parseFloat(
                matcher.group("discount")));
        return card;
    }
}
