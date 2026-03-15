package cafepos.util;

import cafepos.model.Order;
import javax.swing.SwingUtilities;

/**
 * ReceiptThread Class
 * 
 * Demonstrates Concurrency (Multithreading) by taking the I/O blocking
 * file write operation off the Event Dispatch Thread (EDT).
 */
public class ReceiptThread extends Thread {
    
    private Order currentOrder;
    private Runnable onCompleteCallback;
    
    public ReceiptThread(Order order, Runnable onCompleteCallback) {
        this.currentOrder = order;
        this.onCompleteCallback = onCompleteCallback;
    }
    
    @Override
    public void run() {
        try {
            // Write the receipt to disk (simulated latency to prove thread logic)
            Thread.sleep(500); // 500ms delay to prove UI doesn't freeze
            
            // Perform the actual I/O save
            ReceiptSaver.saveReceipt(currentOrder);
            
            // Run the callback back on the UI thread safely
            if (onCompleteCallback != null) {
                SwingUtilities.invokeLater(onCompleteCallback);
            }
            
        } catch (InterruptedException e) {
            System.err.println("Receipt saving thread was interrupted.");
        }
    }
}
