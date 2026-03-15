/**
 * MenuItem Class
 * 
 * This class represents a single item that can be sold in the coffee shop.
 * Each menu item has a name (e.g., "Espresso"), a price (e.g., 3.50),
 * and a category (e.g., "Coffee" or "Food").
 * 
 * This is a simple data holder class (also called a "model" or "bean").
 * It follows the principle of encapsulation by keeping fields private
 * and providing getter methods to access the data.
 */
public class MenuItem {
    
    // Private fields - these can only be accessed through getter methods
    private String name;      // The display name of the item (e.g., "Cappuccino")
    private double price;     // The cost in dollars (e.g., 4.25)
    private String category;  // The category for organization (e.g., "Beverages")
    
    /**
     * Constructor - Creates a new MenuItem
     * 
     * @param name The name of the menu item
     * @param price The price of the item in dollars
     * @param category The category this item belongs to
     */
    public MenuItem(String name, double price, String category) {
        // Initialize all fields with the values passed to the constructor
        this.name = name;
        this.price = price;
        this.category = category;
    }
    
    /**
     * Gets the name of this menu item
     * 
     * @return The name as a String
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the price of this menu item
     * 
     * @return The price as a double
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Gets the category of this menu item
     * 
     * @return The category as a String
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Returns a string representation of this menu item
     * 
     * This method is useful for displaying the item in lists or receipts.
     * It formats the item as "Name - $Price"
     * 
     * @return A formatted string showing the item name and price
     */
    @Override
    public String toString() {
        // Use String.format to ensure price always shows 2 decimal places
        return name + " - $" + String.format("%.2f", price);
    }
}
