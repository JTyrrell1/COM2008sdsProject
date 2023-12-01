import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserDetails extends JDialog {
    private JFrame frame;
    private JButton returnButton;
    private int userID;
    private Address userAddress;
    private BankAccount userBankAccount;

    public UserDetails(int ID) {
        this.userID = ID;
        frame = new JFrame("User Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        returnButton = new JButton("Main Menu");

        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final BetterCustomerPage window = new BetterCustomerPage(ID);
                window.main(ID);
                frame.dispose();
            }
        });

        JPanel returnPanel = new JPanel();
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


        setDetails(ID);

        JTextField addressTextField = new JTextField();
        addressTextField.setEditable(false);
        if (userAddress != null) {
            addressTextField.setText(userAddress.toString());
        } else {
            addressTextField.setText("No address found.");
        }

        JTextField bankTextField = new JTextField();
        bankTextField.setEditable(false);
        if (userBankAccount != null) {
            bankTextField.setText(userBankAccount.toString());
        } else {
            bankTextField.setText("No bank account found.");
        }

        JPanel detailsPanel = new JPanel();
        detailsPanel.add(addressTextField, BorderLayout.WEST);
        detailsPanel.add(bankTextField, BorderLayout.EAST);

        frame.add(detailsPanel, BorderLayout.CENTER);



        frame.pack();
        frame.setVisible(true);
    }


    private void setDetails(int UserID) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();

            String queryIndexes = "SELECT * FROM Users WHERE userid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(queryIndexes);
            preparedStatement.setInt(1, UserID);

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
                int accountNumber = resultSet.getInt(2);
                int civ = resultSet.getInt(3);
                int expiryDate = resultSet.getInt(4);
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

    public static void main(int userID) {
        UserDetails dialog = new UserDetails(userID);
    }
}