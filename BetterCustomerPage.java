import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BetterCustomerPage {
    private JFrame frame;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton OrdersButton;
    private JButton LogOutButton;

    private JCheckBox TrainCheckBox;
    private JCheckBox TrackCheckBox;
    private JCheckBox CarriagesCheckBox;
    private JCheckBox BundlesCheckBox;
    private int userID;

    public BetterCustomerPage(int userID) {
        this.userID = userID;
        frame = new JFrame("Customer DashBoard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        OrdersButton = new JButton("Orders");

        OrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // insert link to orders page
            }
        });

        LogOutButton = new JButton("Log out");

        LogOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final LoginPage window = new LoginPage();
                window.main();
            }
        });


        // Set up the table model.
        tableModel = new DefaultTableModel(new Object[]{"BrandName", "ProductName", "Price"}, 0);

        // Create the table with the model.
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        PullProducts();

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(OrdersButton);
        buttonPanel.add(LogOutButton);
        frame.add(buttonPanel, BorderLayout.NORTH);

        JPanel selectorPanel = new JPanel();
        selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.Y_AXIS));
        TrainCheckBox = new JCheckBox("Trains");
        TrackCheckBox = new JCheckBox("Tracks");
        CarriagesCheckBox = new JCheckBox("Carriages");
        BundlesCheckBox = new JCheckBox("Bundles");
        selectorPanel.add(TrainCheckBox);
        selectorPanel.add(TrackCheckBox);
        selectorPanel.add(CarriagesCheckBox);
        selectorPanel.add(BundlesCheckBox);

        frame.add(selectorPanel, BorderLayout.WEST);



        frame.pack();
        frame.setVisible(true);
    }

    private void PullProducts() {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();
            String query = "SELECT BrandName,ProductName,Price FROM Products";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            tableModel.setRowCount(0);
            while (resultSet.next()) {
                String BrandName = resultSet.getString("BrandName");
                String ProductName = resultSet.getString("ProductName");
                String Price = resultSet.getString("Price");
                tableModel.addRow(new Object[]{BrandName, ProductName, Price});
            }
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(frame, "Database error: " + sqle.getMessage());
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

    public static void main(int ID) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BetterCustomerPage(ID);
            }
        });
    }
}
