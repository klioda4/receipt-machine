package ru.clevertec.kli.receiptmachine.util.io.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.clevertec.kli.receiptmachine.util.parse.Parser;

@RequiredArgsConstructor
public class FileObjectReader<T> {

    private final String inputFilename;
    private final String invalidDataFilename;

    private final Parser<T> parser;

    public List<T> readAll() throws IOException {
        createDirectoryIfNotExists(invalidDataFilename);
        try (InputStream stream = this.getClass().getResourceAsStream('/' + inputFilename)) {
            if (stream == null) {
                throw new IOException("Can't make stream of file " + inputFilename + " of "
                    + "classpath");
            }

            try (var reader = new BufferedReader(
                new InputStreamReader(stream));
                var errorWriter = new PrintWriter(
                    new FileOutputStream(invalidDataFilename))) {

                ObjectReader<T> objectReader = new ObjectReader<>(reader, errorWriter, parser);
                return objectReader.readAll();
            }
        }
    }

    private static void createDirectoryIfNotExists(String filename) {
        File file = new File(filename);
        String parentPath = file.getParent();
        if (parentPath != null) {
            File parentDirectory = new File(parentPath);
            parentDirectory.mkdirs();
        }
    }
}
