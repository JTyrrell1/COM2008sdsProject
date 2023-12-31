import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerOrders extends JDialog{
    private JFrame frame;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JButton OrdersButton;
    private JButton ReturnButton;

    private JButton TrainCheckBox;
    private JButton TrackCheckBox;
    private JButton CarriagesCheckBox;
    private JButton BundlesCheckBox;
    private int userID;
    private int i; 
    private String ButtonName;

    public CustomerOrders(int ID){
        this.userID = ID;
        frame = new JFrame("Customer Orders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"OrderID", "OrderStatus", "ProductID"}, 0);

        ReturnButton = new JButton("Main Menu");

        ReturnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final BetterCustomerPage window = new BetterCustomerPage(ID);
                window.main(ID);
                dispose();
            }
        });


        orderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(ReturnButton, BorderLayout.NORTH);

        GetOrders(ID);
        
        frame.pack();
        frame.setVisible(true);
    }

    private void GetOrders(int UserID){
        Connection connection = null;
        try{
            connection = DatabaseConnectionHandler.getConnection();

            String query = "SELECT * FROM Orders WHERE userid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, UserID);

            ResultSet resultSet = preparedStatement.executeQuery();
            
            tableModel.setRowCount(0);

            while (resultSet.next()) {
                String OrderID = resultSet.getString("OrderID");
                String OrderStatus = resultSet.getString("OrderStatus");
                String ProductID = resultSet.getString("ProductID");
                tableModel.addRow(new Object[]{OrderID, OrderStatus, ProductID});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        } finally {
            DatabaseConnectionHandler.closeConnection(connection);
        }
    }

    public static void main(int userID) {
        CustomerOrders dialog = new CustomerOrders(userID);
        dialog.pack();
        dialog.setVisible(true);
    }
}
