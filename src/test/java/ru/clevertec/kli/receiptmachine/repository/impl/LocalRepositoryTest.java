package ru.clevertec.kli.receiptmachine.repository.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.kli.receiptmachine.pojo.entity.Entity;

class LocalRepositoryTest {

    List<TestEntity> innerList;
    LocalRepository<TestEntity> testRepository;

    @BeforeEach
    void setUp() {
        innerList = makeEntityList();
        testRepository = new LocalRepository<>(innerList);
    }

    @Test
    void whenAdd_thenAddToInnerList() {
        TestEntity newItem = new TestEntity(11);

        testRepository.add(newItem);

        assertThat(testRepository.getItems().contains(newItem), is(true));
    }

    @Test
    void whenGetAll_thenReturnAll() {
        List<TestEntity> result = testRepository.getAll();

        assertThat(result, contains(innerList.toArray()));
    }

    @Test
    void whenRemove_thenRemoveFromInnerList() {
        TestEntity removeEntity = innerList.get(0);

        testRepository.remove(removeEntity);

        assertThat(testRepository.getItems(), not(hasItem(removeEntity)));
    }

    List<TestEntity> makeEntityList() {
        return IntStream.range(1, 5)
            .mapToObj(TestEntity::new)
            .collect(Collectors.toList());
    }

    static class TestEntity implements Entity<Integer> {

        int id;

        public TestEntity(int id) {
            this.id = id;
        }

        @Override
        public Integer getId() {
            return id;
        }
    }
}