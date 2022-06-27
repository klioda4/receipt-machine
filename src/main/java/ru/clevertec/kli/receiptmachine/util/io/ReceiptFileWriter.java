package ru.clevertec.kli.receiptmachine.util.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptDto;

/**
 * Writes receipts to files
 */
@Component
public class ReceiptFileWriter implements ReceiptWriter {

    @Value("${receipts.output.folder.path}")
    private String outputFolderPath;
    @Value("${receipts.output.filename.prefix}")
    private String filenamePrefix;

    @Override
    public void write(ReceiptDto receipt) {
        createFolderIfNotExists();
        String filename = getNewFilename();

        try (FileOutputStream outputStream = new FileOutputStream(filename)) {
            PrintWriter writer = new PrintWriter(outputStream);
            ReceiptStreamWriter receiptWriter = new ReceiptStreamWriter(writer);
            receiptWriter.write(receipt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNewFilename() {
        return outputFolderPath
            + "/"
            + filenamePrefix
            + LocalDateTime.now().format(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            + ".txt";
    }

    private void createFolderIfNotExists() {
        File folder = new File(outputFolderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
