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
    private JButton AddToCartButton;

    private JButton TrainCheckBox;
    private JButton TrackCheckBox;
    private JButton CarriagesCheckBox;
    private JButton BundlesCheckBox;
    private int userID;
    private int i; 
    private String ButtonName;
    private JTextField UserInput;

    private String[] categories = {"All","Tracks", "Controllers", "LocoMotives", "Rolling Stocks", "Train Sets", "Track Packs"};

    public BetterCustomerPage(int userID) {
        this.userID = userID;
        frame = new JFrame("Customer DashBoard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        OrdersButton = new JButton("Orders");

        OrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomerOrders NeoSpace = new CustomerOrders(userID);
                NeoSpace.main(userID);
                frame.dispose();
            }
        });

        LogOutButton = new JButton("Log out");

        LogOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final LoginPage window = new LoginPage();
                window.main();
                frame.dispose();
            }
        });

        AddToCartButton = new JButton("Add to cart");

        AddToCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                AddToCart(userID);
            }
        });

        UserInput = new JTextField();

        JPanel AddPanel = new JPanel();
        AddPanel.add(UserInput);
        AddPanel.add(AddToCartButton);
        frame.add(AddPanel, BorderLayout.SOUTH);

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
        for (String category : categories) {
            JButton button = new JButton(category);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Filter the table by the category
                    fetchProducts(category);
                }
            });
            selectorPanel.add(button);
        }

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

    private void fetchProducts(String filter) {
        // Clear the existing data in the table model.
        tableModel.setRowCount(0);

        // Establish a connection and prepare a query.
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();

            // Start building the query
            String query = "SELECT ProductName, BrandName, Price, ProductID FROM Products";
            if (filter != null) {
                if (filter == "All"){
                    query = "SELECT ProductName, BrandName, Price, ProductID FROM Products";
                }
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
                String ProductName = resultSet.getString("ProductName");
                String BrandName = resultSet.getString("BrandName");
                String Price = resultSet.getString("Price");
                int ProductID = resultSet.getInt("ProductID");


                // Add a row to the table model for each product
                tableModel.addRow(new Object[]{BrandName,ProductName, Price, ProductID});
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    
    private void AddToCart(int UserID){
        Connection connection = null;
        int selectedRow = userTable.getSelectedRow();
        try{
            connection = DatabaseConnectionHandler.getConnection();

            String IDquery = "SELECT MAX(OrderID) FROM Orders";
                PreparedStatement IDStatement = connection.prepareStatement(IDquery);
                ResultSet OrderID = IDStatement.executeQuery();
                OrderID.next();
                int UserVal = OrderID.getInt(1);
                UserVal = UserVal + 1;

            int ProductID = (int) tableModel.getValueAt(selectedRow, 3);

            String query = "INSERT  into Orders values (?,?,?,?)";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query);
            preparedStatement2.setInt(1, UserVal);
            preparedStatement2.setString(2, "Confirmed");
            preparedStatement2.setInt(3, ProductID);
            preparedStatement2.setInt(4, UserID);
        } catch (SQLException e) {
            e.printStackTrace();
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


}
