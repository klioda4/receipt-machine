package ru.clevertec.kli.receiptmachine.repository.impl.file;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.kli.receiptmachine.pojo.entity.Entity;
import ru.clevertec.kli.receiptmachine.repository.impl.LocalRepository;
import ru.clevertec.kli.receiptmachine.util.io.input.FileObjectReader;

public class FileRepository<T extends Entity<?>> extends LocalRepository<T> {

    private static final Logger logger = LogManager.getLogger(FileRepository.class);

    public FileRepository(List<T> list, FileObjectReader<T> reader) throws IOException {
        super(list);
        addItemsFromSource(reader);
    }

    private void addItemsFromSource(FileObjectReader<T> reader) throws IOException {
        logger.trace("Reading from giving source");
        List<T> items = reader.readAll();
        logger.debug("Done reading from giving source. Objects taken: " + items.size());
        for (T item : items) {
            add(item);
        }
    }
}
