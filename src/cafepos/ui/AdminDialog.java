package cafepos.ui;

import cafepos.model.MenuItem;
import cafepos.util.MenuLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminDialog Class
 * 
 * Demonstrates CRUD operations and MVC pattern using JTable and Models.
 */
public class AdminDialog extends JDialog {

    private JTable menuTable;
    private DefaultTableModel tableModel;
    private MainFrame parentFrame;
    private final String CSV_PATH = "data/menu.csv";
    
    // UI Theme matching MainFrame
    private final Color BG_COLOR = new Color(30, 30, 30);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color BTN_COLOR = new Color(41, 128, 185);

    public AdminDialog(MainFrame parent) {
        super(parent, "Admin: Menu Editor", true); // true = Modal dialog
        this.parentFrame = parent;
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        setupTable(mainPanel);
        setupControls(mainPanel);
        
        setContentPane(mainPanel);
    }
    
    private void setupTable(JPanel parent) {
        String[] columns = {"Name", "Price", "Category"};
        tableModel = new DefaultTableModel(columns, 0);
        
        // Load data into model
        List<MenuItem> currentMenu = MenuLoader.loadMenuFromCSV(CSV_PATH);
        for (MenuItem item : currentMenu) {
            tableModel.addRow(new Object[]{item.getName(), item.getPrice(), item.getCategory()});
        }
        
        menuTable = new JTable(tableModel);
        menuTable.setBackground(new Color(40, 40, 40));
        menuTable.setForeground(TEXT_COLOR);
        menuTable.setGridColor(Color.DARK_GRAY);
        menuTable.setRowHeight(25);
        menuTable.getTableHeader().setBackground(new Color(60, 60, 60));
        menuTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        
        parent.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupControls(JPanel parent) {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(BG_COLOR);
        
        JButton addBtn = createAdminBtn("Add Row");
        addBtn.addActionListener(e -> tableModel.addRow(new Object[]{"New Item", 0.0, "Misc"}));
        
        JButton deleteBtn = createAdminBtn("Delete Selected");
        deleteBtn.addActionListener(e -> {
            int selectedRow = menuTable.getSelectedRow();
            if (selectedRow >= 0) {
                tableModel.removeRow(selectedRow);
            }
        });
        
        JButton saveBtn = createAdminBtn("Save Changes");
        saveBtn.setBackground(new Color(39, 174, 96)); // Green
        saveBtn.addActionListener(e -> saveAndClose());
        
        controlPanel.add(addBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(saveBtn);
        
        parent.add(controlPanel, BorderLayout.SOUTH);
    }
    
    private void saveAndClose() {
        // Stop any active cell editing before extracting data
        if (menuTable.isEditing()) {
            menuTable.getCellEditor().stopCellEditing();
        }
        
        List<MenuItem> updatedItems = new ArrayList<>();
        
        // Iterate through table model and build list
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            try {
                String name = tableModel.getValueAt(row, 0).toString();
                double price = Double.parseDouble(tableModel.getValueAt(row, 1).toString());
                String category = tableModel.getValueAt(row, 2).toString();
                updatedItems.add(new MenuItem(name, price, category));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid data format on row " + (row + 1) + ". Save aborted.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Save to file
        if (MenuLoader.saveMenuToCSV(updatedItems, CSV_PATH)) {
            // Tell parent to refresh logic
            parentFrame.refreshMenuUI();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to write to file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JButton createAdminBtn(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(BTN_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }
}
