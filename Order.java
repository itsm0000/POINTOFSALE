import java.util.ArrayList;

/**
 * Order Class
 * 
 * This class manages the current customer's order.
 * It keeps track of all items that have been added and provides
 * methods to add items, clear the order, and calculate the total cost.
 * 
 * Think of this as the "shopping cart" in an online store.
 */
public class Order {
    
    // An ArrayList to store all the items in the current order
    // We use ArrayList because it can grow dynamically as items are added
    private ArrayList<MenuItem> items;
    
    /**
     * Constructor - Creates a new empty order
     * 
     * When a new order is created, we initialize an empty ArrayList
     * to hold the menu items.
     */
    public Order() {
        // Create a new empty list to store menu items
        items = new ArrayList<MenuItem>();
    }
    
    /**
     * Adds a menu item to the current order
     * 
     * @param item The MenuItem to add to the order
     */
    public void addItem(MenuItem item) {
        // Add the item to our ArrayList
        // The ArrayList will automatically expand to make room
        items.add(item);
    }
    
    /**
     * Removes all items from the order
     * 
     * This is used when the customer wants to start over,
     * or after they complete their purchase.
     */
    public void clearOrder() {
        // Remove all elements from the ArrayList
        // This resets the order to empty
        items.clear();
    }
    
    /**
     * Calculates the total cost of all items in the order
     * 
     * This method uses a traditional for loop (NOT streams or lambdas)
     * to add up the price of each item.
     * 
     * @return The total price as a double
     */
    public double calculateTotal() {
        // Initialize a variable to accumulate the total
        double totalPrice = 0.0;
        
        // Loop through each item in the order
        // We use a traditional for loop so it's easy to explain
        for (int i = 0; i < items.size(); i++) {
            // Get the item at index i
            MenuItem currentItem = items.get(i);
            
            // Add this item's price to our running total
            totalPrice = totalPrice + currentItem.getPrice();
        }
        
        // Return the final sum
        return totalPrice;
    }
    
    /**
     * Generates a formatted receipt showing all items and the total
     * 
     * This creates a String that can be displayed in the GUI's text area.
     * It lists each item on a separate line, then shows the total at the bottom.
     * 
     * @return A formatted String representing the receipt
     */
    public String getOrderSummary() {
        // Use StringBuilder for efficient string concatenation
        // (This is better than using + in a loop)
        StringBuilder receipt = new StringBuilder();
        
        // Add a header for the receipt
        receipt.append("===== CURRENT ORDER =====\n\n");
        
        // Check if the order is empty
        if (items.size() == 0) {
            // If no items, show a message
            receipt.append("No items in order.\n");
        } else {
            // Loop through all items and add each one to the receipt
            // Using a traditional for loop for clarity
            for (int i = 0; i < items.size(); i++) {
                // Get the current item
                MenuItem currentItem = items.get(i);
                
                // Add the item to the receipt using its toString() method
                // The toString() method formats it as "Name - $Price"
                receipt.append(currentItem.toString());
                receipt.append("\n");
            }
            
            // Add a separator line before the total
            receipt.append("\n-------------------------\n");
            
            // Calculate and display the total
            // Use String.format to ensure 2 decimal places
            double total = calculateTotal();
            receipt.append("TOTAL: $" + String.format("%.2f", total));
        }
        
        // Convert the StringBuilder to a String and return it
        return receipt.toString();
    }
    
    /**
     * Gets the number of items currently in the order
     * 
     * @return The count of items as an integer
     */
    public int getItemCount() {
        return items.size();
    }
}
