package ru.clevertec.kli.receiptmachine.util.io;

import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptPositionDto;

/**
 * Writes receipts to specified object of PrintWriter.
 */
public class ReceiptStreamWriter implements ReceiptWriter {

    private static final int RECEIPT_WIDTH = 35;
    private static final String RECEIPT_FORMAT = "%3s %-15s %7s %7s\n";
    private static final String RECEIPT_TOTAL_FORMAT = "%-14s %10s%10s\n";

    private final PrintWriter writer;

    public ReceiptStreamWriter(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void write(ReceiptDto receipt) {
        writer.printf("%25s\n", "CASH RECEIPT");
        writer.println(receipt.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
        writer.println(receipt.getTime().format(DateTimeFormatter.ISO_LOCAL_TIME));

        String divider = "_".repeat(RECEIPT_WIDTH);
        writer.println(divider);
        writer.printf(RECEIPT_FORMAT, "qty", "description", "price", "total");
        for (ReceiptPositionDto position : receipt.getPositions()) {
            printPosition(position);
        }

        writer.println(divider);
        printTotalSection(receipt);

        writer.flush();
    }

    private void printPosition(ReceiptPositionDto pos) {
        writer.printf(RECEIPT_FORMAT, pos.getQuantity(), pos.getProductName(),
            "$" + pos.getProductPrice(), "$" + pos.getValueWithoutDiscount());
        if (pos.getProductDiscountPercent() != 0) {
            writer.printf(RECEIPT_FORMAT, "", "",
                "-" + pos.getProductDiscountPercent() + '%',
                "$" + pos.getValueWithoutDiscount()
                    .subtract(pos.getValueWithDiscount()));
        }
    }

    private void printTotalSection(ReceiptDto receipt) {
        if (receipt.getDiscountCardPercent() != 0) {
            writer.printf(RECEIPT_TOTAL_FORMAT,
                "Value", "",
                "$" + receipt.getSumWithoutCardDiscount());
            writer.printf(RECEIPT_TOTAL_FORMAT,
                "Card discount",
                receipt.getDiscountCardPercent() + "%",
                "$" + receipt.getSumWithoutCardDiscount()
                    .subtract(receipt.getTotalValue()));
        }
        writer.printf(RECEIPT_TOTAL_FORMAT, "TOTAL", "", "$" + receipt.getTotalValue());
    }
}
