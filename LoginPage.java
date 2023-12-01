import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.security.SecureRandom;


public class LoginPage extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JPasswordField passwordField1;

    private JFrame frame;

    public LoginPage() {

        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSignUp();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    //Code that runs to validate the users inputs in the email and password for the purpose of logging in.
    private void onOK() {
        String email = textField1.getText();
        char[] password = passwordField1.getPassword();
        if ((!email.isEmpty()) && !(password.length == 0) && !(email.contains(" ")) && !(new String(password).contains(" "))) {
            authenticateUser(email, password);
        } else if ((!email.contains(" ")) && (!new String(password).contains(" "))) {
            JOptionPane.showMessageDialog(frame, "Email or Password has been left blank.");
            main();
        } else {
            JOptionPane.showMessageDialog(frame, "Email or Password contains invalid characters.");
            main();
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    //Code that runs to validate the users inputs in the email and password for the purpose of Signing up.
    private void onSignUp() {
        String email = textField1.getText();
        char[] password = passwordField1.getPassword();
        if ((!email.isEmpty()) && !(password.length == 0) && !(email.contains(" ")) && !(new String(password).contains(" "))) {
            UserSignUp(email, password);
        } else if ((!email.contains(" ")) && (!new String(password).contains(" "))) {
            JOptionPane.showMessageDialog(frame, "Email or Password has been left blank.");
            main();
        } else {
            JOptionPane.showMessageDialog(frame, "Email or Password contains invalid characters.");
            main();
        }
        dispose();
    }

    public static void main() {
        LoginPage dialog = new LoginPage();
        dialog.pack();
        dialog.setVisible(true);
    }

    // Checks the given email and password against a hash of that in the database so users can sign in securely
    private void authenticateUser(String email, char[] inputPassword) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();

            String query = "SELECT * FROM Users WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                //retrieving the user's salt
                String salt = resultSet.getString(9);

                //hashing the inputted password to compare to the stored hashed password
                String hashedInputPassword = PasswordHasher.hashPassword(salt, inputPassword);

                //comparing stored password and inputted password
                String storedPassword = resultSet.getString(3);

                if (storedPassword.equals(hashedInputPassword)) {

                    JOptionPane.showMessageDialog(frame, "Login successful!");

                    int userID = resultSet.getInt(1);

                    BetterCustomerPage maingui = new BetterCustomerPage(userID);
                    maingui.main(userID);

                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid password");
                    main();
                }

            } else {
                JOptionPane.showMessageDialog(frame, "Invalid email");
                main();
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


    //First checks if the given email is already in the database and if not creates an account with the given email and password once again hashing it for security 
    private void UserSignUp(String email, char[] password) {
        Connection connection = null;
        try {
            connection = DatabaseConnectionHandler.getConnection();

            String query = "SELECT * FROM Users WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(frame, "Account already exists");
            } else {
                String IDquery = "SELECT MAX(UserID) FROM Users";
                PreparedStatement IDStatement = connection.prepareStatement(IDquery);
                ResultSet UserID = IDStatement.executeQuery();
                UserID.next();
                int UserVal = UserID.getInt(1);
                UserVal = UserVal + 1;

                //generating random salt for encryption
                SecureRandom random = new SecureRandom();
                byte[] bytes = new byte[20];
                random.nextBytes(bytes);
                String salt = new String(bytes, StandardCharsets.UTF_8);

                String securePassword = PasswordHasher.hashPassword(salt, password);


                String query2 = "INSERT INTO Users (userid, email, password, usertype, salt) VALUES(?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                preparedStatement2.setInt(1, UserVal);
                preparedStatement2.setString(2, email);
                preparedStatement2.setString(3, securePassword);
                preparedStatement2.setString(4, "Customer");
                preparedStatement2.setString(5, salt);
                preparedStatement2.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Account created");
                main();
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


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Sign Up");
        panel2.add(buttonCancel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("Login");
        panel1.add(buttonOK, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textField1 = new JTextField();
        panel3.add(textField1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passwordField1 = new JPasswordField();
        panel3.add(passwordField1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Password");
        panel3.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Email");
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}

