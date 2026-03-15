package cafepos.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * DashboardDialog Class
 * 
 * Demonstrates advanced file parsing, UI lists, and Data Structures (HashMap)
 * to calculate business analytics from raw text files.
 */
public class DashboardDialog extends JDialog {

    private JList<String> fileList;
    private DefaultListModel<String> listModel;
    private JTextArea receiptPreview;
    private JLabel totalRevenueLabel;
    private JLabel topItemLabel;
    
    // UI Theme
    private final Color BG_COLOR = new Color(30, 30, 30);
    private final Color PANEL_COLOR = new Color(45, 45, 45);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color ACCENT_COLOR = new Color(46, 204, 113); // Green

    public DashboardDialog(JFrame parent) {
        super(parent, "Analytics Dashboard", true);
        setSize(700, 500);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        setupAnalyticsTopPanel(mainPanel);
        setupSplitPane(mainPanel);
        
        setContentPane(mainPanel);
        
        // After UI is ready, process the files
        calculateAnalytics();
    }
    
    private void setupAnalyticsTopPanel(JPanel parent) {
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        topPanel.setBackground(BG_COLOR);
        
        totalRevenueLabel = createStatLabel("Total Revenue: $0.00");
        topItemLabel = createStatLabel("Most Popular: None");
        
        topPanel.add(totalRevenueLabel);
        topPanel.add(topItemLabel);
        
        parent.add(topPanel, BorderLayout.NORTH);
    }
    
    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(PANEL_COLOR);
        label.setForeground(ACCENT_COLOR);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        label.setPreferredSize(new Dimension(200, 60));
        return label;
    }
    
    private void setupSplitPane(JPanel parent) {
        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        fileList.setBackground(PANEL_COLOR);
        fileList.setForeground(TEXT_COLOR);
        fileList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fileList.setSelectionBackground(ACCENT_COLOR);
        fileList.setSelectionForeground(Color.BLACK);
        
        fileList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                previewReceipt(fileList.getSelectedValue());
            }
        });
        
        JScrollPane listScroll = new JScrollPane(fileList);
        listScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY), "Saved Receipts",
                0, 0, new Font("Segoe UI", Font.BOLD, 12), TEXT_COLOR));
        listScroll.getViewport().setBackground(PANEL_COLOR);

        receiptPreview = new JTextArea();
        receiptPreview.setEditable(false);
        receiptPreview.setBackground(new Color(20, 20, 20));
        receiptPreview.setForeground(new Color(0, 255, 0)); // Hacker green for receipts
        receiptPreview.setFont(new Font("Consolas", Font.PLAIN, 14));
        receiptPreview.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane previewScroll = new JScrollPane(receiptPreview);
        previewScroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, previewScroll);
        splitPane.setDividerLocation(200);
        splitPane.setBackground(BG_COLOR);
        splitPane.setBorder(null);
        
        parent.add(splitPane, BorderLayout.CENTER);
    }
    
    private void previewReceipt(String filename) {
        if (filename == null) return;
        
        File file = new File("receipts/" + filename);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                receiptPreview.setText("");
                String line;
                while ((line = br.readLine()) != null) {
                    receiptPreview.append(line + "\n");
                }
            } catch (IOException e) {
                receiptPreview.setText("Error reading file.");
            }
        }
    }
    
    private void calculateAnalytics() {
        File folder = new File("receipts/");
        if (!folder.exists() || !folder.isDirectory()) return;
        
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) return;
        
        double totalRevenue = 0.0;
        Map<String, Integer> itemCounts = new HashMap<>(); // Demonstrates HashMaps
        
        for (File file : files) {
            listModel.addElement(file.getName());
            
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                boolean inOrderItems = false;
                
                while ((line = br.readLine()) != null) {
                    // Extract grand total
                    if (line.startsWith("GRAND TOTAL:")) {
                        String[] parts = line.split("\\$");
                        if (parts.length == 2) {
                            totalRevenue += Double.parseDouble(parts[1].trim());
                        }
                    }
                    
                    // Basic parsing trap for items (looks for "x " meaning "Quantity x Item")
                    // Note: This matches the format used in Order.java getOrderSummary
                    if (line.contains("---")) continue; // skip dividers
                    if (line.contains("x ")) {
                        String[] parts = line.trim().split("x ");
                        if (parts.length >= 2) {
                            try {
                                int qty = Integer.parseInt(parts[0].trim());
                                // Extract name (everything before the trailing spaces and price)
                                String namePart = parts[1].trim();
                                // Basic hack: The name is letters, the price contains $
                                int dollarIndex = namePart.indexOf("$");
                                if (dollarIndex > 0) {
                                    String pureName = namePart.substring(0, dollarIndex).trim();
                                    itemCounts.put(pureName, itemCounts.getOrDefault(pureName, 0) + qty);
                                }
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed analyzing file: " + file.getName());
            }
        }
        
        // Find most popular
        String mostPopular = "None";
        int maxQty = 0;
        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            if (entry.getValue() > maxQty) {
                maxQty = entry.getValue();
                mostPopular = entry.getKey();
            }
        }
        
        totalRevenueLabel.setText(String.format("Total Revenue: $%.2f", totalRevenue));
        topItemLabel.setText("Most Popular: " + mostPopular + " (" + maxQty + ")");
    }
}
