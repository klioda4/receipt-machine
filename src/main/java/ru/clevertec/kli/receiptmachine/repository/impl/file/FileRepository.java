package ru.clevertec.kli.receiptmachine.repository.impl.file;

import java.io.IOException;
import java.util.List;
import ru.clevertec.kli.receiptmachine.pojo.entity.Entity;
import ru.clevertec.kli.receiptmachine.repository.impl.LocalRepository;
import ru.clevertec.kli.receiptmachine.util.io.input.FileObjectReader;

public class FileRepository<T extends Entity<?>> extends LocalRepository<T> {

    public FileRepository(List<T> list, FileObjectReader<T> reader) throws IOException {
        super(list);
        addItemsFromSource(reader);
    }

    private void addItemsFromSource(FileObjectReader<T> reader) throws IOException {
        List<T> items = reader.readAll();
        for (T item : items) {
            add(item);
        }
    }
}
