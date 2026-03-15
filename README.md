# Cafe POS System - CE Challenge Edition

A modern, pure-Java Point of Sale system developed with advanced concepts in mind. This POS system demonstrates Object-Oriented Design, Custom UI Rendering, Data Structures, Multithreading, and File I/O.

## Features

### 1. Custom Dark Mode UI
*   Bypasses the restrictive default Look & Feel to provide a custom, sleek Dark Mode appearance.
*   Utilizes pure Swing components (`JPanel`, `JButton`, `JTextArea`) configured manually for borderless, flat-design rendering.
*   Implements `MouseAdapter` listeners for interactive color-brightening hover states.

### 2. Admin Menu Editor (CRUD Operations)
*   A dedicated control panel accessible via the top `JMenuBar`.
*   Connects a `JTable` to the `data/menu.csv` via a `DefaultTableModel`.
*   Support Create, Read, Update, and Delete operations.
*   Changes are persisted todisk using `PrintWriter` and instantly trigger a refresh event on the main UI.

### 3. Analytics Dashboard
*   Replaces basic receipt viewing with actionable business intelligence.
*   Demonstrates programmatic directory traversal by scanning `receipts/` for all historical order files.
*   Parses text to calculate lifetime "Total Revenue".
*   Utilizes a `HashMap<String, Integer>` to track item frequencies and calculate the "Most Popular Item".

### 4. Asynchronous File Writing (Multithreading)
*   The `ReceiptSaver` disk I/O operation is offloaded to a custom background `ReceiptThread`.
*   Ensures the Event Dispatch Thread (EDT) remains unblocked, keeping the UI perfectly responsive.
*   Utilizes `SwingUtilities.invokeLater()` to safely return execution back to the EDT once disk saving is complete.

## Project Structure

*   `src/cafepos/model/`: Data structures (`MenuItem.java`, `Order.java`, `OrderItem.java`)
*   `src/cafepos/ui/`: Presentation layer (`MainFrame.java`, `AdminDialog.java`, `DashboardDialog.java`)
*   `src/cafepos/util/`: Helper classes (`MenuLoader.java`, `ReceiptSaver.java`, `ReceiptThread.java`)
*   `data/`: Contains the `menu.csv` database file.
*   `receipts/`: Destination for generated text receipts.
*   `bin/`: Compiled `.class` files.

## How to Run

1. Clone or download the repository.
2. Ensure you have the Java Development Kit (JDK) installed.
3. Open a terminal in the root directory.
4. Compile the project:
   ```bash
   javac -d bin src/cafepos/model/*.java src/cafepos/util/*.java src/cafepos/ui/*.java
   ```
5. Run the application:
   ```bash
   java -cp bin cafepos.ui.MainFrame
   ```
