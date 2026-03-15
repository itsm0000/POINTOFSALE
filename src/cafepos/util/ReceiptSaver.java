package cafepos.util;

import cafepos.model.Order;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ReceiptSaver Class
 * 
 * Utility to save a completed order receipt to a text file.
 */
public class ReceiptSaver {

    /**
     * Saves the order summary to a new text file in the receipts folder.
     * 
     * @param order The completed order
     */
    public static void saveReceipt(Order order) {
        // Generate a filename based on the current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = dtf.format(now);
        String filename = "receipts/Receipt_" + timestamp + ".txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write the formatted receipt to the file
            writer.println(order.getOrderSummary());
            System.out.println("Receipt saved to " + filename);
        } catch (IOException e) {
            System.err.println("Failed to save receipt: " + e.getMessage());
        }
    }
}
