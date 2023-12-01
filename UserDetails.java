import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDetails extends JDialog {
    private final JFrame frame;
    private Address userAddress;
    private BankAccount userBankAccount;

    public UserDetails(int ID) {
        frame = new JFrame("User Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JButton returnButton = new JButton("Main Menu");

        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new BetterCustomerPage(ID);
                frame.dispose();
            }
        });


        JButton editButton = new JButton("Edit Details");

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new EditDetails(ID);
                frame.dispose();
            }
        });

        JPanel returnPanel = new JPanel();
        returnPanel.add(editButton);
        returnPanel.add(returnButton);
        frame.add(returnPanel, BorderLayout.SOUTH);

        JLabel addressHeader = new JLabel();
        addressHeader.setText("Address");

        JLabel bankHeader = new JLabel();
        bankHeader.setText("Bank Account");

        JPanel headerPanel = new JPanel();
        headerPanel.add(addressHeader, BorderLayout.WEST);
        headerPanel.add(bankHeader, BorderLayout.EAST);
        frame.add(headerPanel, BorderLayout.NORTH);


        SetDetails(ID);

        JTextArea addressTextField = new JTextArea();
        addressTextField.setEditable(false);
        if (userAddress != null) {
            addressTextField.setText(userAddress.toString());
        } else {
            addressTextField.setText("No address found.");
        }

        JTextArea bankTextField = new JTextArea();
        bankTextField.setEditable(false);
        if (userBankAccount != null) {
            bankTextField.setText(userBankAccount.toString());
        } else {
            bankTextField.setText("No bank account found. BROKE!");
        }

        JPanel detailsPanel = new JPanel();
        detailsPanel.add(addressTextField, BorderLayout.WEST);
        detailsPanel.add(bankTextField, BorderLayout.EAST);

        frame.add(detailsPanel, BorderLayout.CENTER);



        frame.pack();
        frame.setVisible(true);
    }


    private void SetDetails(int userID) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();

            String queryIndexes = "SELECT * FROM Users WHERE userid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(queryIndexes);
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int addressIndex = resultSet.getInt(7);
                int bankIndex = resultSet.getInt(8);
                this.userAddress = GetAddress(addressIndex);
                this.userBankAccount = GetBankDetails(bankIndex);

            } else {
                JOptionPane.showMessageDialog(frame, "No user logged in");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        } finally {
            DatabaseConnectionHandler.closeConnection(connection);
        }
    }

    private Address GetAddress(int addressIndex) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();

            String queryAddress = "SELECT * FROM Address WHERE AddressID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(queryAddress);
            preparedStatement.setInt(1, addressIndex);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String houseNumber = resultSet.getString(2);
                String roadName = resultSet.getString(3);
                String cityName = resultSet.getString(4);
                String postCode = resultSet.getString(5);

                return new Address(houseNumber, roadName, cityName, postCode);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        } finally {
            DatabaseConnectionHandler.closeConnection(connection);
        }
        return null;
    }

    private BankAccount GetBankDetails(int bankIndex) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();

            String queryBankDetails = "SELECT * FROM Banking WHERE BankID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(queryBankDetails);
            preparedStatement.setInt(1, bankIndex);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String accountNumber = resultSet.getString(2);
                String civ = resultSet.getString(3);
                String expiryDate = resultSet.getString(4);
                String firstName = resultSet.getString(5);
                String lastName = resultSet.getString(6);

                return new BankAccount(accountNumber, civ, expiryDate, firstName, lastName);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
        } finally {
            DatabaseConnectionHandler.closeConnection(connection);
        }
        return null;
    }
}