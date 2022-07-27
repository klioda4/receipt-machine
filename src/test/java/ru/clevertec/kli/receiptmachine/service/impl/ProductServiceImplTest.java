package ru.clevertec.kli.receiptmachine.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.kli.receiptmachine.exception.NotEnoughLeftoverException;
import ru.clevertec.kli.receiptmachine.pojo.dto.ProductDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;
import ru.clevertec.kli.receiptmachine.util.parse.regex.ProductParser;
import ru.clevertec.kli.receiptmachine.util.serialize.strings.impl.ProductStringConverter;
import ru.clevertec.kli.receiptmachine.util.validate.impl.ValidatorImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    Repository<Product> repository;

    ProductServiceImpl testingService;

    List<Product> products;

    @BeforeEach
    void setUp() {
        testingService = new ProductServiceImpl(repository, new ModelMapperExt(),
            new ValidatorImpl<>(new ProductStringConverter(), new ProductParser()));
        products = makeProductList();
    }

    @Test
    void whenRequestItem_thenReturnIt() {
        int id = 1;
        when(repository.get(id)).thenReturn(products.get(id - 1));

        ProductDto product = testingService.get(id);

        assertThat(product.getId(), is(id));
    }

    @Test
    void whenRequestForAll_thenReturnThemWithLinks() {
        when(repository.getAll()).thenReturn(products);

        List<ProductDto> list = testingService.getAll();

        assertThat(list, hasSize(products.size()));
        for (Product product : products) {
            assertThat(list, hasItem(hasProperty("id", is(equalTo(product.getId())))));
        }
    }

    @Test
    void whenRequestForWriteOff_thenReduceQuantity() throws NotEnoughLeftoverException {
        int id = 1;
        Product product = products.get(id - 1);
        when(repository.get(id)).thenReturn(product);

        testingService.writeOff(id, 1);

        assertThat(product.getCount(), is(0));
    }

    private List<Product> makeProductList() {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            products.add(Product.builder()
                .id(i)
                .name("product" + i)
                .count(i)
                .price(BigDecimal.valueOf(i * 2))
                .build());
        }
        return products;
    }
}
