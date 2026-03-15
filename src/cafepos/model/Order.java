package cafepos.model;

import java.util.ArrayList;

/**
 * Order Class
 * 
 * This class manages the current customer's order.
 * It uses OrderItems to group identical menu items by quantity.
 */
public class Order {
    
    private ArrayList<OrderItem> items;
    private static final double TAX_RATE = 0.08; // 8% sales tax
    
    public Order() {
        items = new ArrayList<OrderItem>();
    }
    
    /**
     * Adds an item. If it already exists, increments the quantity.
     */
    public void addItem(MenuItem item) {
        for (OrderItem orderItem : items) {
            if (orderItem.getItem().getName().equals(item.getName())) {
                orderItem.incrementQuantity();
                return;
            }
        }
        // If not found, add a new one
        items.add(new OrderItem(item, 1));
    }
    
    /**
     * Removes the last added item or decrements its quantity.
     * Useful for undoing a mistake.
     */
    public void removeLastItem() {
        if (!items.isEmpty()) {
            OrderItem last = items.get(items.size() - 1);
            if (last.getQuantity() > 1) {
                last.decrementQuantity();
            } else {
                items.remove(items.size() - 1);
            }
        }
    }
    
    public void clearOrder() {
        items.clear();
    }
    
    /**
     * Calculates the subtotal (before tax)
     */
    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (int i = 0; i < items.size(); i++) {
            subtotal += items.get(i).getTotalPrice();
        }
        return subtotal;
    }
    
    /**
     * Calculates the tax amount based on subtotal
     */
    public double calculateTax() {
        return calculateSubtotal() * TAX_RATE;
    }
    
    /**
     * Calculates the grand total (subtotal + tax)
     */
    public double calculateGrandTotal() {
        return calculateSubtotal() + calculateTax();
    }
    
    public String getOrderSummary() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("========== CAFE POS ==========\n");
        receipt.append("        CURRENT ORDER        \n");
        receipt.append("==============================\n\n");
        
        if (items.size() == 0) {
            receipt.append("No items in order.\n");
        } else {
            // Using a traditional for loop to match previous style
            for (int i = 0; i < items.size(); i++) {
                OrderItem current = items.get(i);
                MenuItem item = current.getItem();
                
                // Format: 2x Espresso @ $3.00 = $6.00
                String line = String.format("%dx %-15s @ $%.2f = $%.2f", 
                    current.getQuantity(), 
                    item.getName(), 
                    item.getPrice(), 
                    current.getTotalPrice());
                    
                receipt.append(line);
                receipt.append("\n");
            }
            
            receipt.append("\n------------------------------\n");
            
            double subtotal = calculateSubtotal();
            double tax = calculateTax();
            double total = calculateGrandTotal();
            
            receipt.append(String.format("SUBTOTAL:              $%.2f\n", subtotal));
            receipt.append(String.format("TAX (8%%):              $%.2f\n", tax));
            receipt.append("------------------------------\n");
            receipt.append(String.format("GRAND TOTAL:           $%.2f\n", total));
            receipt.append("==============================\n");
            receipt.append("    Thank you for visiting!    \n");
        }
        
        return receipt.toString();
    }
    
    public int getItemCount() {
        int count = 0;
        for (OrderItem item : items) {
            count += item.getQuantity();
        }
        return count;
    }
}
