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
    private JButton DetailsButton;
    private JButton LogOutButton;
    private JButton AddToCartButton;
    private JButton StaffButton;

    private JButton TrainCheckBox;
    private JButton TrackCheckBox;
    private JButton CarriagesCheckBox;
    private JButton BundlesCheckBox;
    private int userID;
    private int i; 
    private String ButtonName;

    private String[] categories = {"All","Tracks", "Controllers", "LocoMotives", "Rolling Stocks", "Train Sets", "Track Packs"};

    public BetterCustomerPage(int userID) {
        this.userID = userID;
        frame = new JFrame("Customer DashBoard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //Checks what type of user the logged in user is and then if they have high enough permissions it shows the user the button to take them to the staff area.
        String UserRank = GetUserType(userID);
        if ((UserRank != null) && ((UserRank.equals("Staff"))  || (UserRank.equals("Manager")))){
            StaffButton = new JButton("Staff");

            StaffButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    StaffPage RiumHeart = new StaffPage(userID);
                    RiumHeart.main(userID);
                    frame.dispose();
                }
            });
        }

        DetailsButton = new JButton("User Details");

        DetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UserDetails(userID);
                frame.dispose();
            }
        });


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
                window.startUp();
                frame.dispose();
            }
        });

        AddToCartButton = new JButton("Order");

        AddToCartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                AddToCart(userID);
            }
        });

        JPanel AddPanel = new JPanel();
        AddPanel.add(AddToCartButton);
        frame.add(AddPanel, BorderLayout.SOUTH);

        // Set up the table model.
        tableModel = new DefaultTableModel(new Object[]{"BrandName", "ProductName", "Price", "ProductID"}, 0);

        // Create the table with the model.
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        PullProducts();

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(DetailsButton);
        buttonPanel.add(OrdersButton);
        buttonPanel.add(LogOutButton);
        buttonPanel.add(StaffButton);
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

    //Pulls all the products from the database and uses them to poulate the table show on the page.
    private void PullProducts() {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();
            String query = "SELECT BrandName,ProductName,Price,ProductID FROM Products";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            tableModel.setRowCount(0);
            while (resultSet.next()) {
                String BrandName = resultSet.getString("BrandName");
                String ProductName = resultSet.getString("ProductName");
                String Price = resultSet.getString("Price");
                String ProductID = resultSet.getString("ProductID");
                tableModel.addRow(new Object[]{BrandName, ProductName, Price, ProductID});
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

    //Filters the products in the database according to the selected filter and then puts them in the table on the screen.
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
    
    //Lets the customer click on a product and then on the order button to order a product
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

            int ProductID = Integer.parseInt(tableModel.getValueAt(selectedRow, 3).toString());
            String query = "INSERT  into Orders values (?,?,?,?)";
            PreparedStatement preparedStatement2 = connection.prepareStatement(query);
            preparedStatement2.setInt(1, UserVal);
            preparedStatement2.setString(2, "Confirmed");
            preparedStatement2.setInt(3, ProductID);
            preparedStatement2.setInt(4, UserID);
            preparedStatement2.executeUpdate();
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

    //Gets the type of the currently logged in user
    private String GetUserType(int UserID){
        Connection connection = null;
        try{
            connection = DatabaseConnectionHandler.getConnection();

            String UserTypeQuery = "SELECT UserType From Users WHERE UserID = ?";
            PreparedStatement preparedStatement3 = connection.prepareStatement(UserTypeQuery);
            preparedStatement3.setInt(1, UserID);
            ResultSet UType = preparedStatement3.executeQuery();

            if (UType.next()) {
                System.out.println(UType.getString(1));
                return UType.getString(1);
            } else {
                return "Customer";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            // Close the connection
            if (connection != null) {
                try {
                    connection.close();
                    return null;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

}
