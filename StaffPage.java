import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.*;
import java.util.Vector;

public class StaffPage extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JPanel buttonPanel;
    private String[] categories = {"Tracks", "Controllers", "LocoMotives", "Rolling Stocks", "Train Sets", "Track Packs"}; // Your category identifiers
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;

    public StaffPage() {
        // Set up the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setTitle("Product Management");

        // Table setup
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Product Name");
        tableModel.addColumn("Brand Name");
        tableModel.addColumn("Product Code");
        tableModel.addColumn("Price");
        tableModel.addColumn("Gauge");
        tableModel.addColumn("Era");
        tableModel.addColumn("DCC Code");
        tableModel.addColumn("Quantity");

        // Button panel setup
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (String category : categories) {
            JButton button = new JButton(category);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Filter the table by the category
                    fetchProducts(category);
                }
            });
            buttonPanel.add(button);
        }
        addButton = new JButton("Add Product");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddProductDialog addDialog = new AddProductDialog(StaffPage.this,1);
                addDialog.setVisible(true);
                // After the dialog is disposed, you might want to refresh the product list
                fetchProducts(null); // Or however you retrieve and display the products
            }
        });

        deleteButton = new JButton("Delete Product");

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String ProductID = (String) tableModel.getValueAt(selectedRow, 0);
                    System.out.println(ProductID);
                    deleteProduct(Integer.valueOf(ProductID));
                    fetchProducts(null); // Refresh the table.
                }
            }
        });


        editButton = new JButton("Edit Product");


        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.add(addButton);
        buttonPanel2.add(deleteButton);
        buttonPanel2.add(editButton);

        add(buttonPanel2, BorderLayout.SOUTH);

        // Add components to frame
        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Fetch all products initially
        fetchProducts(null);

        // Display the frame
        setVisible(true);
    }

    private void fetchProducts(String filter) {
        // Clear the existing data in the table model.
        tableModel.setRowCount(0);

        // Establish a connection and prepare a query.
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();

            // Start building the query
            String query = "SELECT ProductID, ProductName, BrandName,ProductCode, Price, Gauge, Era, DccCode, Quantity FROM Products";
            if (filter != null) {
                if (filter == "Tracks") {
                    query += " WHERE ProductCode LIKE 'R%'";
                }
                if (filter == "Controllers") {
                    query += " WHERE ProductCode LIKE 'C%'";
                }
                if (filter == "LocoMotives") {
                    query += " WHERE ProductCode LIKE 'L%'";
                }
                if (filter == "Rolling Stocks") {
                    query += " WHERE ProductCode LIKE 'S%'";
                }
                if (filter == "Train Sets") {
                    query += " WHERE ProductCode LIKE 'M%'";
                }
                if (filter == "Track Packs") {
                    query += " WHERE ProductCode LIKE 'P%'";
                }
            }

            PreparedStatement statement = connection.prepareStatement(query);


            // Execute the query and process the results
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String ProductID = resultSet.getString("ProductID");
                String ProductName = resultSet.getString("ProductName");
                String BrandName = resultSet.getString("BrandName");
                String ProductCode = resultSet.getString("ProductCode");
                String Price = resultSet.getString("Price");
                String Gauge = resultSet.getString("Gauge");
                String Era = resultSet.getString("Era");
                String DccCode = resultSet.getString("DccCode");
                String Quantity = resultSet.getString("Quantity");

                // Add a row to the table model for each product
                tableModel.addRow(new Object[]{ProductID,ProductName, BrandName, ProductCode, Price, Gauge, Era, DccCode, Quantity});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching products: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close the connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteProduct(Integer ProductID) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();
            // Start transaction
            connection.setAutoCommit(false);

            String checkQuery = "DELETE FROM Products WHERE ProductID = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, String.valueOf(ProductID));
            checkStmt.execute();
  //          ResultSet resultSet = checkStmt.executeQuery();
        }
        catch (SQLException sqle) {
            JOptionPane.showMessageDialog(this, "Database error: " + sqle.getMessage());
        }finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StaffPage();
            }
        });
    }
}