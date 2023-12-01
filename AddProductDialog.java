import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddProductDialog extends JDialog {
    private JTextField productIDField, productNameField, brandNameField, productCodeField, priceField, gaugeField, eraField, dccCodeField, quantityField;
    private JButton addButton;

    public AddProductDialog(Frame owner,Integer ProductID, String title) {
        super(owner, title, true);

        Integer ID = null;
        String BrandName = null;
        String ProductName = null;
        String ProductCode = null;
        Float Price = null;
        String Gauge = null;
        String Era = null;
        String DccCode = null;
        Integer Quantity = null;

        if (ProductID != null) {
            Connection connection = null;
            try {
                connection = DatabaseConnectionHandler.getConnection();
                // Start transaction
                connection.setAutoCommit(false);

                String checkQuery = "SELECT ProductID, BrandName, ProductName, ProductCode, Price, Gauge, Era, DccCode, Quantity FROM Products WHERE ProductID = ?";
                PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                checkStmt.setInt(1, ProductID);
                ResultSet resultSet = checkStmt.executeQuery();
                resultSet.next();

                ID = Integer.valueOf(resultSet.getString("ProductID"));
                BrandName = resultSet.getString("BrandName");
                ProductName = resultSet.getString("ProductName");
                ProductCode = resultSet.getString("ProductCode");
                Price = Float.parseFloat(resultSet.getString("Price"));
                Gauge = resultSet.getString("Gauge");
                Era = resultSet.getString("Era");
                DccCode = resultSet.getString("DccCode");
                Quantity = Integer.valueOf(resultSet.getString("Quantity"));

            } catch (SQLException sqle) {
                JOptionPane.showMessageDialog(owner, "Database error: " + sqle.getMessage());
            } finally {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            }
        }


        setLayout(new GridLayout(0, 2));

        // Form fields
        add(new JLabel("Product ID:"));
        if (ProductID == null) {
            add(new JLabel());
            productIDField = new JTextField();
            add(productIDField);
        }
        else{
            add(new JLabel(String.valueOf(ID)));
        }


        add(new JLabel("Brand Name:"));
        if (ProductID != null) {
            brandNameField = new JTextField(BrandName);
        }
        else{
            brandNameField = new JTextField();
        }
        add(brandNameField);

        add(new JLabel("Product Name:"));
        if (ProductID != null) {
            productNameField = new JTextField(ProductName);
        }
        else{
            productNameField = new JTextField();
        }
        add(productNameField);

        add(new JLabel("Product Code:"));
        if (ProductID != null) {
            productCodeField = new JTextField(ProductCode);
        }
        else{
            productCodeField = new JTextField();
        }
        add(productCodeField);

        add(new JLabel("Price:"));
        if (ProductID != null) {
            priceField = new JTextField(String.valueOf(Price));
        }
        else{
            priceField = new JTextField();
        }
        add(priceField);

        add(new JLabel("Gauge:"));
        if (ProductID != null) {
            gaugeField = new JTextField(Gauge);
        }
        else{
            gaugeField = new JTextField();
        }
        add(gaugeField);

        add(new JLabel("Era:"));
        if (ProductID != null) {
            eraField = new JTextField(Era);
        }
        else{
            eraField = new JTextField();
        }
        add(eraField);

        add(new JLabel("DccCode:"));
        if (ProductID != null) {
            dccCodeField = new JTextField(DccCode);
        }
        else{
            dccCodeField = new JTextField();
        }
        add(dccCodeField);

        add(new JLabel("Quantity:"));
        if (ProductID != null) {
            quantityField = new JTextField(String.valueOf(Quantity));
        }
        else{
            quantityField = new JTextField();
        }
        add(quantityField);

        // Add button
        addButton = new JButton(title);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProductToDatabase(ProductID);
            }
        });
        add(addButton);

        setSize(350, 300);
    }

    private void addProductToDatabase( Integer id) {
        try {
            // Database insertion
            Connection conn = null;
            try {
                conn = DatabaseConnectionHandler.getConnection();
                PreparedStatement preparedStatement = null;
                if (id != null) {
                    String sql = "UPDATE Products SET ProductID = ?, ProductName = ?, BrandName = ?, ProductCode = ?, Price = ?, Gauge = ?, Era = ?, DccCode = ?, quantity = ? WHERE ProductID = ?";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(10, id);
                    preparedStatement.setInt(1, id);
                }
                else {
                    String sql = "INSERT INTO Products (ProductID, ProductName, BrandName, ProductCode, Price, Gauge, Era, DccCode, Quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    preparedStatement = conn.prepareStatement(sql);
                    if (productIDField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Product ID cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int ProductID = Integer.parseInt(productIDField.getText().trim());
                    preparedStatement.setInt(1, ProductID);
                }

                if (productNameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Product Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                preparedStatement.setString(2, productNameField.getText().trim());

                if (brandNameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Brand Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                preparedStatement.setString(3, brandNameField.getText().trim());

                if (productCodeField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Product Code cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else{
                    String productCode = productCodeField.getText().trim().toUpperCase();
                    if (!productCode.matches("[RCNLSP][0-9]{3,5}")) {
                        JOptionPane.showMessageDialog(this, "Invalid Product Code.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    preparedStatement.setString(4, productCode);
                }
                preparedStatement.setString(4, productCodeField.getText().trim());

                if (priceField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Price cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                float price;
                try {
                    price = Float.parseFloat(priceField.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number for Price.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                preparedStatement.setFloat(5, price);

                String gaugeValue = gaugeField.getText().trim().toUpperCase();
                if (gaugeValue.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Gauge cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!(gaugeValue.equals("OO") || gaugeValue.equals("TT") || gaugeValue.equals("NN"))) {
                    JOptionPane.showMessageDialog(this, "Invalid Gauge. Must be 'OO', 'TT', or 'NN'.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                preparedStatement.setString(6, gaugeValue);

                String eraValue = eraField.getText().trim();
                if (!eraValue.isEmpty()) {
                    if (!eraValue.matches("Era [1-9]-[1-9]") && !eraValue.matches("Era 1[0-1]-1[0-1]")) {
                        JOptionPane.showMessageDialog(this, "Invalid Era format. Must be 'Era ?-?' where ? is 1-11.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                // Check if the Era field is empty and set NULL in the database if it is
                if (eraValue.isEmpty()) {
                    preparedStatement.setNull(7, Types.VARCHAR);
                } else {
                    preparedStatement.setString(7, eraValue);
                }

                String DccCode = dccCodeField.getText().trim();
                // Check if the Era field is empty and set NULL in the database if it is
                if (DccCode.isEmpty()) {
                    preparedStatement.setObject(8, Types.VARCHAR);
                } else {
                    if (!(DccCode.equals("Analogue") || DccCode.equals("DCC-Ready") || DccCode.equals("DCC-Fitted")|| DccCode.equals("DCC-Sound"))) {
                        JOptionPane.showMessageDialog(this, "Invalid DccCode. Must be 'Analogue', 'DCC-Ready', 'DCC-Sound', or 'DCC-Fitted'.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    preparedStatement.setString(8, DccCode);
                }
                preparedStatement.setString(8, DccCode);

                if (quantityField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Quantity cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int Quantity = Integer.parseInt(quantityField.getText().trim());
                preparedStatement.setInt(9, Quantity);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Product added successfully.");
                    dispose(); // Close the dialog
                } else {
                    JOptionPane.showMessageDialog(this, "No rows added. Please check your input values.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for Product ID.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Close the connection
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (HeadlessException e) {
            throw new RuntimeException(e);
        }
    }
}

