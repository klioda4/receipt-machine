package ru.clevertec.kli.receiptmachine.util.io.input;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.kli.receiptmachine.exception.ValidationException;
import ru.clevertec.kli.receiptmachine.util.parse.Parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectReaderTest {

    StringWriter errorStringWriter;
    @Mock
    Parser<Object> parser;

    ObjectReader<Object> testObjectReader;

    @BeforeEach
    void setUp() {
        BufferedReader bufferedReader = new BufferedReader(makeStringReader());
        errorStringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(errorStringWriter);
        testObjectReader = new ObjectReader<>(bufferedReader, printWriter, parser);
    }

    @Test
    void whenReadAll_thenReturnCorrectList() throws IOException, ValidationException {
        Object[] expected = makeObjectsArray();
        when(parser.parse("line1")).thenReturn(expected[0]);
        when(parser.parse("line2")).thenReturn(expected[1]);
        when(parser.parse("line3")).thenReturn(expected[2]);
        when(parser.parse("bad_line")).thenThrow(new ValidationException("bad_line", "test"));

        List<Object> result = testObjectReader.readAll();

        assertThat(result, contains(expected[0], expected[1], expected[2]));
    }

    @Test
    void whenParserThrowsException_thenWriteToErrorStream()
        throws ValidationException, IOException {

        Object[] expected = makeObjectsArray();
        when(parser.parse("line1")).thenReturn(expected[0]);
        when(parser.parse("line2")).thenReturn(expected[1]);
        when(parser.parse("line3")).thenReturn(expected[2]);
        when(parser.parse("bad_line")).thenThrow(new ValidationException("bad_line", "test"));

        testObjectReader.readAll();

        assertThat(errorStringWriter.toString().contains("bad_line"), is(true));
    }

    StringReader makeStringReader() {
        String testString = "line1\nline2\nbad_line\nline3";
        return new StringReader(testString);
    }

    Object[] makeObjectsArray() {
        Object[] objects = new Object[3];
        IntStream.range(0, 3).forEach(i -> objects[i] = new Object());
        return objects;
    }
}