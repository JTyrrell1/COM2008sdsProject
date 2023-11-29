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
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton editStatus;
    private JButton demoteButton;

    private JButton returnStaffPage;

    public StaffOrderPage() {
        // Create and set up the window.
        frame = new JFrame("Staff Order Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Set up the table model.
        tableModel = new DefaultTableModel(new Object[]{"OrderID", "OrderStatus", "ProductID", "UserID"}, 0);

        // Create the table with the model.
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Fetch user data from the database and add to the table model.
        fetchOrders();

        // Create and add the promote button.

        frame.pack();
        frame.setVisible(true);

    }
    private void fetchOrders() {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();
            String query = "SELECT OrderID, OrderStatus, ProductID, UserID FROM Orders";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Clear the existing data in the table model.
            tableModel.setRowCount(0);
            // Add the retrieved users to the table model.
            while (resultSet.next()) {
                String OrderID = resultSet.getString("OrderID");
                String OrderStatus = resultSet.getString("OrderStatus");
                String ProductID = resultSet.getString("ProductID");
                String UserID = resultSet.getString("UserID");
                tableModel.addRow(new Object[]{OrderID, OrderStatus, ProductID, UserID});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        } finally {
            DatabaseConnectionHandler.closeConnection(connection);
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

