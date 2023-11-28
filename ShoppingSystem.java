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
                
                //need to get User[] users, Product[] products and Order[] orders from database
                //ShoppingSystem shoppingSystem = new ShoppingSystem(users, products, orders);
                final LoginPage window = new LoginPage();
                window.main();


            }
        });
    }

                
    public ShoppingSystem(User[] u, Product[] p, Order[] o) {
        users = u;
        products = p;
        orders = o;
    }
}
