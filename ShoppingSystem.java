import javax.swing.*;

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
}