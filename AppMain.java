import javax.swing.SwingUtilities;

public class AppMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ArcadeLogin().setVisible(true);
        });
    }
}
