package ru.clevertec.kli.receiptmachine.repository.impl.fake;

import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.repository.impl.LocalRepository;

@Repository
public class FakeDiscountCardsRepository extends LocalRepository<DiscountCard> {

    public FakeDiscountCardsRepository() {
        super(new ArrayList<>());
        add(new DiscountCard(4815, 5));
        add(new DiscountCard(1234, 10));
        add(new DiscountCard(4321, 0.5f));
    }
}
