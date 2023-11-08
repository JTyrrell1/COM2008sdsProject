import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        final String text = "Window";
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final testing window = new testing();
                window.setVisible(true);
            }
        });
    }
}
