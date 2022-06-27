package ru.clevertec.kli.receiptmachine.repository.impl.fake;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;
import org.springframework.stereotype.Repository;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.repository.impl.LocalRepository;

@Repository
public class FakeProductsRepository extends LocalRepository<Product> {

    public FakeProductsRepository() {
        super(new ArrayList<>());

        for (int i = 1; i < 20; i++) {
            Product newProduct = Product.builder()
                .id(i)
                .name("Product " + i)
                .price(getRandomPrice())
                .promotional(i <= 10)
                .build();
            add(newProduct);
        }
    }

    private BigDecimal getRandomPrice() {
        double rawPrice = new Random().nextDouble();
        return new BigDecimal(rawPrice)
            .setScale(2, RoundingMode.HALF_EVEN)
            .movePointRight(2);
    }
}
