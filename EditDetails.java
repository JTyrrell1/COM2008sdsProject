import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditDetails extends JDialog {
    private final JFrame frame;
    private Address userAddress;
    private BankAccount userBankAccount;
    private int UserID;

    public EditDetails(int ID) {
        frame = new JFrame("Edit User Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JButton returnButton = new JButton("Return to Details");

        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UserDetails(ID);
                frame.dispose();
            }
        });

        JPanel returnPanel = new JPanel();
        returnPanel.add(returnButton);
        frame.add(returnPanel, BorderLayout.SOUTH);

        JLabel addressHeader = new JLabel();
        addressHeader.setText("Edit Address");

        JLabel bankHeader = new JLabel();
        bankHeader.setText("Edit Bank Details");

        JPanel headerPanel = new JPanel();
        headerPanel.add(addressHeader, BorderLayout.WEST);
        headerPanel.add(bankHeader, BorderLayout.EAST);
        frame.add(headerPanel, BorderLayout.NORTH);

        //address form
        GridLayout addressLayout = new GridLayout(4, 2);

        JPanel addressForm = new JPanel();
        addressForm.setLayout(addressLayout);

        addressForm.add(new JLabel("House Number: "));
        JTextField houseNumber = new JTextField("");
        addressForm.add(houseNumber);
        addressForm.add(new JLabel("Road Name: "));
        JTextField roadName = new JTextField("");
        addressForm.add(roadName);
        addressForm.add(new JLabel("City Name: "));
        JTextField cityName = new JTextField("");
        addressForm.add(cityName);
        addressForm.add(new JLabel("Post Code: "));
        JTextField postCode = new JTextField("");
        addressForm.add(postCode);

        frame.add(addressForm, BorderLayout.WEST);

        //bank details form
        GridLayout bankLayout = new GridLayout(5, 2);

        JPanel bankForm = new JPanel();
        bankForm.setLayout(bankLayout);

        bankForm.add(new JLabel("Account Number: "));
        JTextField accountNumber = new JTextField("");
        bankForm.add(accountNumber);
        bankForm.add(new JLabel("Security Number: "));
        JTextField securityNumber = new JTextField("");
        bankForm.add(securityNumber);
        bankForm.add(new JLabel("ExpiryDate (MMYY): "));
        JTextField expiryDate = new JTextField("");
        bankForm.add(expiryDate);
        bankForm.add(new JLabel("First Name: "));
        JTextField firstName = new JTextField("");
        bankForm.add(firstName);
        bankForm.add(new JLabel("Last Name: "));
        JTextField lastName = new JTextField("");
        bankForm.add(lastName);

        frame.add(bankForm, BorderLayout.EAST);

        JButton submitButton = new JButton("Submit Changes");

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SubmitDetails(ID, houseNumber.getText(), roadName.getText(), cityName.getText(), postCode.getText(),
                        accountNumber.getText(), securityNumber.getText(), expiryDate.getText(), firstName.getText(),
                        lastName.getText());
            }
        });

        returnPanel.add(submitButton);

        frame.pack();
        frame.setVisible(true);
    }

    private void SubmitDetails(int UserID, String houseNumber, String roadName, String cityName, String postCode,
                               String accountNumber, String securityCode, String expiryDate, String firstName,
                               String lastName) {
        Connection connection = null;

        try {
            connection = DatabaseConnectionHandler.getConnection();

            String queryIndex = "SELECT AddressID FROM Users WHERE userid = ?";
            PreparedStatement preparedStatement3 = connection.prepareStatement(queryIndex);
            preparedStatement3.setInt(1, UserID);

            ResultSet resultSet = preparedStatement3.executeQuery();

            if (resultSet.next()) {

                String queryAddress = "UPDATE Address SET HouseNumber = ?, RoadName = ?, CityName = ?, PostCode = ? " +
                        "WHERE AddressID = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(queryAddress);
                preparedStatement.setString(1, houseNumber);
                preparedStatement.setString(2, roadName);
                preparedStatement.setString(3, cityName);
                preparedStatement.setString(4, postCode);
                preparedStatement.setInt(5, resultSet.getInt(1));
                preparedStatement.executeUpdate();

            } else {
                String addressIDQuery = "SELECT MAX(AddressID) FROM Address";
                PreparedStatement addressIDStatement = connection.prepareStatement(addressIDQuery);
                ResultSet addressID = addressIDStatement.executeQuery();
                addressID.next();
                int UserVal = addressID.getInt(1);
                UserVal = UserVal + 1;

                String query3 = "UPDATE Users SET AddressID = ?;";
                PreparedStatement preparedStatement4 = connection.prepareStatement(query3);
                preparedStatement4.setInt(1, UserVal);
                preparedStatement4.executeUpdate();

                String query2 = "INSERT INTO Address (HouseNumber, RoadName, CityName, PostCode) VALUES(?, ?, ?, ?)";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                preparedStatement2.setString(1, houseNumber);
                preparedStatement2.setString(2, roadName);
                preparedStatement2.setString(3, cityName);
                preparedStatement2.setString(4, postCode);
                preparedStatement2.executeUpdate();
                JOptionPane.showMessageDialog(frame, "New address added.");
            }


                String queryBank = "UPDATE Banking SET AccountNumber = ?, Civ = ?, ExpiryDate = ?, FirstName = ?," +
                        "LastName = ?;";
                PreparedStatement preparedStatement2 = connection.prepareStatement(queryBank);
                preparedStatement2.setString(1, accountNumber);
                preparedStatement2.setString(2, securityCode);
                preparedStatement2.setString(3, expiryDate);
                preparedStatement2.setString(4, firstName);
                preparedStatement2.setString(5, lastName);
                preparedStatement2.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Details Updated");



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
}