import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.*;

public class ManagerPage {

    private JFrame frame;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton promoteButton;
    private JButton demoteButton;

    private JButton returnStaffPage;

    public ManagerPage(int UserID) {
        // Create and set up the window.
        frame = new JFrame("Manager Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Set up the table model.
        tableModel = new DefaultTableModel(new Object[]{"Email", "Forename", "Surname", "UserType"}, 0);

        // Create the table with the model.
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Fetch user data from the database and add to the table model.
        fetchUsers();

        // Create and add the promote button.
        promoteButton = new JButton("Promote to Staff");
        promoteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    String Email = (String) tableModel.getValueAt(selectedRow, 0);
                    promoteUser(Email);
                    fetchUsers(); // Refresh the table.
                }
            }
        });

//        returnStaffPage = new JButton("Staff Page");
//        returnStaffPage.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e){
//            StaffPage maingui = new StaffPage();
//            maingui.main();
//            }
//        }

        // Create and add the demote button.
        demoteButton = new JButton("Demote to Customer");
        demoteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    String Email = (String) tableModel.getValueAt(selectedRow, 0);
                    demoteUser(Email);
                    fetchUsers(); // Refresh the table.
                }
            }
        });

        // Add buttons to the south region of the frame.
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(promoteButton);
        buttonPanel.add(demoteButton);
//        buttonPanel.add(returnStaffPage);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private void fetchUsers() {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();
            String query = "SELECT Email, Forename, Surname, UserType FROM Users";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Clear the existing data in the table model.
            tableModel.setRowCount(0);

            // Add the retrieved users to the table model.
            while (resultSet.next()) {
                String Email = resultSet.getString("Email");
                String Forename = resultSet.getString("Forename");
                String Surname = resultSet.getString("Surname");
                String UserType = resultSet.getString("UserType");
                tableModel.addRow(new Object[]{Email, Forename, Surname, UserType});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        } finally {
            DatabaseConnectionHandler.closeConnection(connection);
        }
    }

    private void promoteUser(String Email) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();
            // Start transaction
            connection.setAutoCommit(false);

            String checkQuery = "SELECT UserType FROM Users WHERE Email = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, Email);
            ResultSet resultSet = checkStmt.executeQuery();

            if(resultSet.next() && "Customer".equals(resultSet.getString("UserType"))) {
                String updateQuery = "UPDATE Users SET UserType = 'Staff' WHERE Email = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setString(1, Email);
                updateStmt.executeUpdate();
                connection.commit(); // Commit the transaction
                JOptionPane.showMessageDialog(frame, "User promoted to staff successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "User is already a staff member or does not exist.");
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

    private void demoteUser(String Email) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();
            // Start transaction
            connection.setAutoCommit(false);

            String checkQuery = "SELECT UserType FROM Users WHERE Email = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, Email);
            ResultSet resultSet = checkStmt.executeQuery();

            if(resultSet.next() && "Staff".equals(resultSet.getString("UserType"))) {
                String updateQuery = "UPDATE Users SET UserType = 'Customer' WHERE email = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setString(1, Email);
                updateStmt.executeUpdate();
                connection.commit(); // Commit the transaction
                JOptionPane.showMessageDialog(frame, "User demoted to customer successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "User is already a customer or does not exist.");
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

    // Main method to start the application.
    public static void main(int UserID) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ManagerPage(UserID);
            }
        });
    }
}


