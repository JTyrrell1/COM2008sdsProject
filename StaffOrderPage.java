import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffOrderPage {

    private JFrame frame;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JButton fulfillButton;
    private JButton deleteButton;
    private JButton pastButton;
    private JButton currentButton;

    private static boolean past;

    private JButton returnStaffPage;

    public StaffOrderPage() {
        // Create and set up the window.
        frame = new JFrame("Staff Order Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());


        // Set up the table model.
        tableModel = new DefaultTableModel(new Object[]{"OrderID", "OrderStatus", "ProductID", "FullName", "HouseNumber", "RoadName", "CityName", "PostCode", "Stock"}, 0);

        // Create the table with the model.
        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Fetch user data from the database and add to the table model.
        past = false;
        fetchOrders(past);

        fulfillButton = new JButton("Fulfill Order");
        fulfillButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow != -1) {
                    String OrderID = (String) tableModel.getValueAt(selectedRow, 0);
                    String ProductID = (String) tableModel.getValueAt(selectedRow, 2);
                    String Quantity = (String) tableModel.getValueAt(selectedRow, 8);
                    if (Integer.valueOf(Quantity) < 1) {
                        JOptionPane.showMessageDialog(frame, "Stock is too low.");
                    }
                    else{
                        fulfillOrder(Integer.valueOf(OrderID),Integer.valueOf(ProductID));
                    }
                    fetchOrders(past); // Refresh the table.
                }
            }
        });


        deleteButton = new JButton("Delete Order");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow != -1) {
                    String OrderID = (String) tableModel.getValueAt(selectedRow, 0);
                    String OrderStatus = (String) tableModel.getValueAt(selectedRow, 1);
                    System.out.println(OrderStatus);
                    if (OrderStatus.equals("Fulfilled")) {
                        JOptionPane.showMessageDialog(frame, "Cannot delete already fulfilled order.");
                    }
                    else {
                        deleteOrder(Integer.valueOf(OrderID));
                    }
                    fetchOrders(past); // Refresh the table.
                }
            }
        });

        pastButton = new JButton("View Past Orders");
        pastButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                past = true;
                fetchOrders(past);
            }
        });

        currentButton = new JButton("View Current Orders");
        currentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                past = false;
                fetchOrders(past);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(fulfillButton);
        buttonPanel.add(pastButton);
        buttonPanel.add(currentButton);
        buttonPanel.add(deleteButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

    }
    private void fetchOrders(boolean past) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();
            String query = "SELECT o.OrderID, o.OrderStatus, o.ProductID,CONCAT(u.Forename, ' ', u.Surname) AS FullName, a.HouseNumber, a.RoadName, a.CityName, a.PostCode, p.Quantity " +
                    "FROM Orders o, Products p, Users u, Address a " +
                    "WHERE o.ProductID = p.ProductID " +
                    "AND u.UserID = o.UserID " +
                    "AND u.AddressID = a.AddressID ";
            if (past == true) {
                query += "AND (o.OrderStatus = 'Fulfilled' OR o.OrderStatus = 'Confirmed')";
            }
            else {
                query += "AND o.OrderStatus = 'Confirmed'";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Clear the existing data in the table model.
            tableModel.setRowCount(0);
            // Add the retrieved users to the table model.
            while (resultSet.next()) {
                String OrderID = resultSet.getString("OrderID");
                String OrderStatus = resultSet.getString("OrderStatus");
                String ProductID = resultSet.getString("ProductID");
                String FullName = resultSet.getString("FullName");
                String HouseNumber = resultSet.getString("HouseNumber");
                String RoadName = resultSet.getString("RoadName");
                String CityName = resultSet.getString("CityName");
                String PostCode = resultSet.getString("PostCode");
                String Quantity = resultSet.getString("Quantity");
                tableModel.addRow(new Object[]{OrderID, OrderStatus, ProductID, FullName, HouseNumber, RoadName, CityName, PostCode, Quantity});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        } finally {
            DatabaseConnectionHandler.closeConnection(connection);
        }
    }

    private void fulfillOrder(Integer OrderID, Integer ProductID) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();
            // Start transaction
            connection.setAutoCommit(false);

            String checkQuery = "SELECT OrderStatus FROM Orders WHERE OrderID = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, OrderID);
            ResultSet resultSet = checkStmt.executeQuery();

            if(resultSet.next() && "Confirmed".equals(resultSet.getString("OrderStatus"))) {
                String updateOrderQuery = "UPDATE Orders SET OrderStatus = 'Fulfilled' WHERE OrderID = ?";
                PreparedStatement updateOrderStmt = connection.prepareStatement(updateOrderQuery);
                String updateProductQuery = "UPDATE Products SET Quantity = Quantity - 1 WHERE ProductID = ?";
                PreparedStatement updateProductStmt = connection.prepareStatement(updateProductQuery);
                updateOrderStmt.setInt(1, OrderID);
                updateOrderStmt.executeUpdate();
                updateProductStmt.setInt(1, ProductID);
                updateProductStmt.executeUpdate();
                connection.commit(); // Commit the transaction
                JOptionPane.showMessageDialog(frame, "Order fulfilled successfully");
            } else {
                JOptionPane.showMessageDialog(frame, "Order is already fulfilled or does not exist.");
            }
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(frame, "Database error: " + sqle.getMessage());
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

    private void deleteOrder(Integer OrderID) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();
            // Start transaction
            connection.setAutoCommit(false);

            String Query = "DELETE FROM Orders WHERE OrderID = ?";
            PreparedStatement Stmt = connection.prepareStatement(Query);
            Stmt.setInt(1, OrderID);

            int rowsAffected = Stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Commit the transaction if the deletion was successful
                connection.commit();
                JOptionPane.showMessageDialog(frame, "Order deleted successfully.");
            } else {
                // Rollback if no rows were affected
                connection.rollback();
                JOptionPane.showMessageDialog(frame, "Order not found.");
            }

        }
        catch (SQLException sqle) {
            JOptionPane.showMessageDialog(frame, "Database error: " + sqle.getMessage());
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
                new StaffOrderPage();
            }
        });
    }
}

