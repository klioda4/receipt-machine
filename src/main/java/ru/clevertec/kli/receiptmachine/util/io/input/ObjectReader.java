package ru.clevertec.kli.receiptmachine.util.io.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.kli.receiptmachine.exception.ValidationException;
import ru.clevertec.kli.receiptmachine.util.parse.Parser;

@RequiredArgsConstructor
public class ObjectReader<T> {

    private static final Logger logger = LogManager.getLogger(ObjectReader.class);

    private final BufferedReader reader;
    private final PrintWriter errorWriter;
    private final Parser<T> parser;

    public List<T> readAll() throws IOException {
        List<T> list = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            try {
                T item = parser.parse(line);
                list.add(item);
            } catch (ValidationException e) {
                logger.debug("Writing invalid line to error writer.");
                errorWriter.println(line);
            }
        }
        errorWriter.flush();
        return list;
    }
}
