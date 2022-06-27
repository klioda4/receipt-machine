package ru.clevertec.kli.receiptmachine.util.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeJsonSerializer extends JsonSerializer<LocalDateTime> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(LocalDateTime dateTime, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {

        gen.writeString(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
