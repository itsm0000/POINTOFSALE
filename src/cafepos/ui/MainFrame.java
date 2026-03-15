package cafepos.ui;

import cafepos.model.MenuItem;
import cafepos.model.Order;
import cafepos.util.MenuLoader;
import cafepos.util.ReceiptThread;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * MainFrame Class
 * 
 * Advanced GUI demonstrating Custom Look & Feel, Menu Bars, 
 * Dynamic UI updating, and threading integration.
 */
public class MainFrame extends JFrame {
    
    private Order currentOrder;
    private JTextArea receiptArea;
    private JLabel totalLabel;
    private List<MenuItem> menuItems;
    private JPanel menuPanel;
    private JScrollPane menuScrollPane;
    
    // Custom Dark Mode Palette
    private final Color BG_COLOR = new Color(30, 30, 30);
    private final Color PANEL_COLOR = new Color(45, 45, 45);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(231, 76, 60);
    
    private final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 16);
    
    public MainFrame() {
        // Purposely NOT using UIManager.getSystemLookAndFeelClassName()
        // so we can demonstrate complete control over component rendering.
        
        currentOrder = new Order();
        loadMenuFiles();
        
        setTitle("CafePOS - CE Challenge Edition");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        createMenuBar();
        
        JPanel mainContent = new JPanel(new BorderLayout(15, 15));
        mainContent.setBackground(BG_COLOR);
        mainContent.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(mainContent);
        
        buildMenuPanelArea();
        buildReceiptPanelArea();
        buildControlPanelArea();
        
        setVisible(true);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(20, 20, 20));
        menuBar.setForeground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
        
        JMenu systemMenu = new JMenu("System");
        systemMenu.setForeground(TEXT_COLOR);
        
        JMenuItem adminItem = new JMenuItem("Admin Menu Editor (CRUD)");
        adminItem.addActionListener(e -> new AdminDialog(this).setVisible(true));
        
        JMenuItem dashboardItem = new JMenuItem("Analytics Dashboard");
        dashboardItem.addActionListener(e -> new DashboardDialog(this).setVisible(true));
        
        JMenuItem exitItem = new JMenuItem("Exit POS");
        exitItem.addActionListener(e -> System.exit(0));
        
        systemMenu.add(dashboardItem);
        systemMenu.add(adminItem);
        systemMenu.addSeparator();
        systemMenu.add(exitItem);
        
        menuBar.add(systemMenu);
        setJMenuBar(menuBar);
    }
    
    private void loadMenuFiles() {
        menuItems = MenuLoader.loadMenuFromCSV("data/menu.csv");
        if (menuItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Could not load menu data.", "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Called publicly by AdminDialog when edits are saved.
     */
    public void refreshMenuUI() {
        loadMenuFiles();
        menuPanel.removeAll();
        populateMenuButtons();
        menuPanel.revalidate();
        menuPanel.repaint();
    }
    
    private void buildMenuPanelArea() {
        menuPanel = new JPanel();
        menuPanel.setBackground(BG_COLOR);
        menuPanel.setLayout(new GridLayout(0, 3, 10, 10));
        
        populateMenuButtons();
        
        menuScrollPane = new JScrollPane(menuPanel);
        menuScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY), 
            "Menu Items", 0, 0, BOLD_FONT, TEXT_COLOR));
        menuScrollPane.setBackground(BG_COLOR);
        menuScrollPane.getViewport().setBackground(BG_COLOR);
        
        add(menuScrollPane, BorderLayout.CENTER);
    }
    
    private void populateMenuButtons() {
        for (MenuItem item : menuItems) {
            String htmlText = "<html><center><font color='white'>" + item.getName() + "</font><br><font color='#2ecc71'>$" + 
                String.format("%.2f", item.getPrice()) + "</font></center></html>";
                
            JButton itemBtn = createCustomButton(htmlText, PANEL_COLOR);
            itemBtn.setPreferredSize(new Dimension(150, 90));
            
            itemBtn.addActionListener(e -> {
                currentOrder.addItem(item);
                updateReceiptDisplay();
            });
            
            menuPanel.add(itemBtn);
        }
    }
    
    private void buildReceiptPanelArea() {
        JPanel receiptPanel = new JPanel(new BorderLayout(5, 5));
        receiptPanel.setBackground(PANEL_COLOR);
        receiptPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        receiptPanel.setPreferredSize(new Dimension(380, 0));
        
        JLabel receiptTitle = new JLabel("Current Order", SwingConstants.CENTER);
        receiptTitle.setFont(BOLD_FONT);
        receiptTitle.setForeground(Color.WHITE);
        receiptPanel.add(receiptTitle, BorderLayout.NORTH);
        
        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setBackground(new Color(20, 20, 20)); // Hacker dark
        receiptArea.setForeground(new Color(0, 255, 0)); // Terminal green
        receiptArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        receiptArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        receiptPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(PANEL_COLOR);
        totalPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        totalLabel = new JLabel("Total: $0.00", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        totalLabel.setForeground(SUCCESS_COLOR);
        totalPanel.add(totalLabel, BorderLayout.CENTER);
        
        receiptPanel.add(totalPanel, BorderLayout.SOUTH);
        
        add(receiptPanel, BorderLayout.EAST);
        updateReceiptDisplay();
    }
    
    private void buildControlPanelArea() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setBackground(BG_COLOR);
        
        JButton removeBtn = createCustomButton("Remove Last", WARNING_COLOR);
        removeBtn.addActionListener(e -> {
            currentOrder.removeLastItem();
            updateReceiptDisplay();
        });
        
        JButton clearBtn = createCustomButton("Clear Order", new Color(192, 57, 43)); // Dark Red
        clearBtn.addActionListener(e -> {
            currentOrder.clearOrder();
            updateReceiptDisplay();
        });
        
        JButton checkoutBtn = createCustomButton("Checkout / Pay", SUCCESS_COLOR);
        checkoutBtn.setFont(BOLD_FONT);
        checkoutBtn.setPreferredSize(new Dimension(200, 50));
        
        checkoutBtn.addActionListener(e -> {
            if (currentOrder.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "Order is empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            checkoutBtn.setEnabled(false);
            checkoutBtn.setText("Processing...");
            double total = currentOrder.calculateGrandTotal();
            
            // Multithreading demonstration: offload to SwingWorker/Thread
            Runnable onComplete = () -> {
                JOptionPane.showMessageDialog(this,
                    "Payment Successful!\nTotal Paid: $" + String.format("%.2f", total),
                    "Complete", JOptionPane.INFORMATION_MESSAGE);
                
                currentOrder.clearOrder();
                updateReceiptDisplay();
                checkoutBtn.setText("Checkout / Pay");
                checkoutBtn.setEnabled(true);
            };
            
            // Start background thread
            new ReceiptThread(currentOrder, onComplete).start();
        });
        
        controlPanel.add(removeBtn);
        controlPanel.add(clearBtn);
        controlPanel.add(checkoutBtn);
        
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Demonstrates custom graphics control and hover states.
     */
    private JButton createCustomButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(MAIN_FONT);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        // Remove standard Windows borders to allow solid colors to shine
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }
    
    private void updateReceiptDisplay() {
        receiptArea.setText(currentOrder.getOrderSummary());
        totalLabel.setText("Total: $" + String.format("%.2f", currentOrder.calculateGrandTotal()));
    }
    
    public static void main(String[] args) {
        // Run UI initialization on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
