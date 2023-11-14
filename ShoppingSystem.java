import javax.swing.*;
import java.awt.*;

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
                
                final GUI window = new GUI();
                GUI.main();


            }
        });
    }

                
    public ShoppingSystem(User[] u, Product[] p, Order[] o) {
        users = u;
        products = p;
        orders = o;
    }
}
