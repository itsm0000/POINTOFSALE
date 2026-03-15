package cafepos.util;

import cafepos.model.MenuItem;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MenuLoader Class
 * 
 * Utility to read and write menu items from/to a CSV file.
 */
public class MenuLoader {

    /**
     * Loads menu items from the specified CSV file path.
     * 
     * @param filePath Path to the CSV file (e.g., "data/menu.csv")
     * @return A list of MenuItem objects
     */
    public static List<MenuItem> loadMenuFromCSV(String filePath) {
        List<MenuItem> menuItems = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Skip the header row
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Split the line by commas
                String[] values = line.split(",");
                
                // We expect exactly 3 values: Name, Price, Category
                if (values.length == 3) {
                    String name = values[0].trim();
                    double price = Double.parseDouble(values[1].trim());
                    String category = values[2].trim();
                    
                    // Create and add the menu item
                    menuItems.add(new MenuItem(name, price, category));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading menu file: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing price in menu file: " + e.getMessage());
            e.printStackTrace();
        }
        
        return menuItems;
    }
    
    /**
     * Saves a list of menu items back into the CSV file.
     * Overwrites the existing file to maintain consistency.
     */
    public static boolean saveMenuToCSV(List<MenuItem> items, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write the header row
            writer.println("Name,Price,Category");
            
            // Write each item
            for (MenuItem item : items) {
                writer.println(item.getName() + "," + item.getPrice() + "," + item.getCategory());
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving menu file: " + e.getMessage());
            return false;
        }
    }
}
