import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * MainFrame Class
 * 
 * This is the main GUI window for the Cafe POS System.
 * It creates the user interface using Java Swing components and handles
 * all user interactions (button clicks).
 * 
 * Layout:
 * - LEFT: Grid of menu item buttons
 * - RIGHT: Receipt display area
 * - BOTTOM: Control buttons (Clear, Calculate Total, Checkout)
 */
public class MainFrame extends JFrame {
    
    // The current order being built
    private Order currentOrder;
    
    // GUI Components that we need to access from multiple methods
    private JTextArea receiptArea;        // Displays the receipt
    private JLabel totalLabel;            // Shows the current total
    private ArrayList<MenuItem> menuItems; // The list of available menu items
    
    /**
     * Constructor - Sets up the entire GUI
     * 
     * This method creates the window and all its components.
     */
    public MainFrame() {
        // Initialize the current order
        currentOrder = new Order();
        
        // Load the hardcoded menu items
        loadMenu();
        
        // Set up the window properties
        setTitle("CafePOS - Coffee Shop Point of Sale");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Create and add all the panels
        createMenuPanel();
        createReceiptPanel();
        createControlPanel();
        
        // Make the window visible
        setVisible(true);
    }
    
    /**
     * Loads the hardcoded menu items
     * 
     * Instead of using a database, we create the menu items right here
     * in the code. This makes the program easier to run and explain.
     */
    private void loadMenu() {
        // Create a new ArrayList to hold all menu items
        menuItems = new ArrayList<MenuItem>();
        
        // Add coffee items
        // Each line creates a new MenuItem and adds it to the list
        menuItems.add(new MenuItem("Espresso", 3.00, "Coffee"));
        menuItems.add(new MenuItem("Cappuccino", 4.00, "Coffee"));
        menuItems.add(new MenuItem("Latte", 4.50, "Coffee"));
        menuItems.add(new MenuItem("Americano", 3.50, "Coffee"));
        menuItems.add(new MenuItem("Mocha", 5.00, "Coffee"));
        
        // Add food items
        menuItems.add(new MenuItem("Croissant", 3.50, "Food"));
        menuItems.add(new MenuItem("Muffin", 2.50, "Food"));
        menuItems.add(new MenuItem("Bagel", 3.00, "Food"));
        menuItems.add(new MenuItem("Cookie", 2.00, "Food"));
        
        // Add other beverages
        menuItems.add(new MenuItem("Hot Chocolate", 3.50, "Beverage"));
        menuItems.add(new MenuItem("Tea", 2.50, "Beverage"));
        menuItems.add(new MenuItem("Iced Coffee", 4.00, "Beverage"));
    }
    
    /**
     * Creates the left panel containing menu item buttons
     * 
     * This panel displays all available items as clickable buttons
     * arranged in a grid layout.
     */
    private void createMenuPanel() {
        // Create a panel to hold all menu buttons
        JPanel menuPanel = new JPanel();
        
        // Use GridLayout to arrange buttons in rows and columns
        // Parameters: rows (0 means any number), columns (3), horizontal gap, vertical gap
        menuPanel.setLayout(new GridLayout(0, 3, 10, 10));
        
        // Add a border with padding around the panel
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create a button for each menu item
        // We use a traditional for loop (not forEach or streams)
        for (int i = 0; i < menuItems.size(); i++) {
            // Get the menu item at index i
            final MenuItem item = menuItems.get(i);
            
            // Create a button with the item's name and price
            JButton itemButton = new JButton("<html><center>" + item.getName() + 
                                            "<br>$" + String.format("%.2f", item.getPrice()) + 
                                            "</center></html>");
            
            // Set button properties for better appearance
            itemButton.setFont(new Font("Arial", Font.BOLD, 14));
            itemButton.setPreferredSize(new Dimension(150, 80));
            
            // Add an ActionListener to handle button clicks
            // When clicked, this adds the item to the order
            itemButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Add this item to the current order
                    currentOrder.addItem(item);
                    
                    // Update the receipt display to show the new item
                    updateReceiptDisplay();
                }
            });
            
            // Add the button to the panel
            menuPanel.add(itemButton);
        }
        
        // Add the menu panel to the left side of the main window
        add(menuPanel, BorderLayout.WEST);
    }
    
    /**
     * Creates the right panel showing the receipt
     * 
     * This panel displays the current order items and total.
     */
    private void createReceiptPanel() {
        // Create a panel to hold the receipt area
        JPanel receiptPanel = new JPanel();
        receiptPanel.setLayout(new BorderLayout(5, 5));
        receiptPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add a title label at the top
        JLabel receiptTitle = new JLabel("Current Order", SwingConstants.CENTER);
        receiptTitle.setFont(new Font("Arial", Font.BOLD, 18));
        receiptPanel.add(receiptTitle, BorderLayout.NORTH);
        
        // Create a text area to display the receipt
        receiptArea = new JTextArea();
        receiptArea.setEditable(false);  // Users can't type in it
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        // Wrap the text area in a scroll pane in case the order is long
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        receiptPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create a label to show the total
        totalLabel = new JLabel("Total: $0.00", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalLabel.setForeground(new Color(0, 150, 0));  // Green color
        receiptPanel.add(totalLabel, BorderLayout.SOUTH);
        
        // Add the receipt panel to the right side of the main window
        add(receiptPanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates the bottom panel with control buttons
     * 
     * This panel contains buttons for clearing the order,
     * calculating the total, and checking out.
     */
    private void createControlPanel() {
        // Create a panel for the control buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        // Create the "Clear Order" button
        JButton clearButton = new JButton("Clear Order");
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setPreferredSize(new Dimension(150, 40));
        
        // Add action listener for the clear button
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear all items from the order
                currentOrder.clearOrder();
                
                // Update the display to show empty order
                updateReceiptDisplay();
            }
        });
        
        // Create the "Calculate Total" button
        JButton calculateButton = new JButton("Calculate Total");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 14));
        calculateButton.setPreferredSize(new Dimension(150, 40));
        
        // Add action listener for the calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Calculate the total cost
                double total = currentOrder.calculateTotal();
                
                // Show the total in a popup dialog
                JOptionPane.showMessageDialog(MainFrame.this,
                    "Total Amount: $" + String.format("%.2f", total),
                    "Order Total",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Create the "Checkout" button
        JButton checkoutButton = new JButton("Checkout / Pay");
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutButton.setPreferredSize(new Dimension(150, 40));
        checkoutButton.setBackground(new Color(0, 150, 0));
        checkoutButton.setForeground(Color.WHITE);
        
        // Add action listener for the checkout button
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if there are any items in the order
                if (currentOrder.getItemCount() == 0) {
                    // Show an error if order is empty
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Cannot checkout - order is empty!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                } else {
                    // Calculate the total
                    double total = currentOrder.calculateTotal();
                    
                    // Show a success message
                    JOptionPane.showMessageDialog(MainFrame.this,
                        "Payment Successful!\nTotal: $" + String.format("%.2f", total) + 
                        "\n\nThank you for your purchase!",
                        "Checkout Complete",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Clear the order after successful checkout
                    currentOrder.clearOrder();
                    updateReceiptDisplay();
                }
            }
        });
        
        // Add all buttons to the control panel
        controlPanel.add(clearButton);
        controlPanel.add(calculateButton);
        controlPanel.add(checkoutButton);
        
        // Add the control panel to the bottom of the main window
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Updates the receipt display with the current order
     * 
     * This method is called whenever items are added or removed
     * to refresh what the user sees.
     */
    private void updateReceiptDisplay() {
        // Get the formatted receipt from the Order object
        String receipt = currentOrder.getOrderSummary();
        
        // Display it in the text area
        receiptArea.setText(receipt);
        
        // Update the total label
        double total = currentOrder.calculateTotal();
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }
    
    /**
     * Main method - Entry point of the program
     * 
     * This is where the program starts running.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Create the main window on the Event Dispatch Thread
        // This is the proper way to start a Swing application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and display the main frame
                new MainFrame();
            }
        });
    }
}
