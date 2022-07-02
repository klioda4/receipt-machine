package ru.clevertec.kli.receiptmachine.util.io.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.clevertec.kli.receiptmachine.exception.ValidationException;
import ru.clevertec.kli.receiptmachine.util.parse.Parser;

@RequiredArgsConstructor
public class ObjectReader<T> {

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
                System.out.println("Validation failed: " + line);
                errorWriter.println(line);
            }
        }
        errorWriter.flush();
        return list;
    }
}
