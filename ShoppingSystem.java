import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ShoppingSystem {

    private User[] users;
    private Product[] products;
    private Order[] orders;

    public static void main(String[] args) {
        final String text = "Window";
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                final LoginPage window = new LoginPage();
                window.main();


            }
        });
    }
}