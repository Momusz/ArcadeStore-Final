import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArcadeLogin extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;

    public int idUsuarioLogado;
    public String nick;
    public String senha;
    private boolean nickValid;
    private boolean senhaValid;

    private void botaoHab(){
        boolean preenchido = !txtUsername.getText().trim().isEmpty() && txtPassword.getPassword().length > 0;
        boolean valido = nickValid && senhaValid;
        btnLogin.setEnabled(preenchido && valido);
    }

    public ArcadeLogin() {
        initialize();
    }

    private void initialize() {
        setTitle("Arcade Store Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        Font customFont = new Font("Arial", Font.PLAIN, 18);
        Font titleFont = new Font("Calibri", Font.BOLD, 50);
        Font pixelLabelFont = new Font("Arial", Font.BOLD, 18);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        try {
            InputStream is = getClass().getResourceAsStream("/PressStart2P-Regular.ttf");
            if (is == null) {
                System.err.println("Arquivo de fonte não encontrado. Usando fonte padrão.");
            } else {
                try (BufferedInputStream bis = new BufferedInputStream(is)) {
                    Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, bis);
                    titleFont = pixelFont.deriveFont(Font.PLAIN, 50f);
                    pixelLabelFont = pixelFont.deriveFont(Font.PLAIN, 20f);
                    buttonFont = pixelFont.deriveFont(Font.PLAIN, 18f); 
                    
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(pixelFont);
                    
                    System.out.println("Fonte carregada com sucesso: " + pixelFont.getFontName());
                }
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Erro ao carregar a fonte. Usando fonte padrão.");
            e.printStackTrace();
        }

        ImagePanel backgroundPanel = new ImagePanel("src/fundo.jpg");
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(100, 20, 20, 20));

        JLabel label = new NeonGradientLabel("ArcadeStore", titleFont,
            new Color(0, 204, 255),
            new Color(255, 51, 153),
            3); 
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = new JLabel("Usuário:");
        lblUser.setFont(pixelLabelFont);
        lblUser.setForeground(new Color(153, 204, 255));
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtUsername = new JTextField();
        txtUsername.setFont(customFont);
        txtUsername.setMaximumSize(new Dimension(250, 30));
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel msglbl1 = new JLabel();
        msglbl1.setFont(pixelLabelFont);
        msglbl1.setForeground(new Color(153, 204, 255));
        msglbl1.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsername.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validarNick();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validarNick();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validarNick();
            }

            private void validarNick() {

                String[] invalid = {"*", ","," ","@","#","%","á","à","ã","â","é","è","ê","í","ì","î","ó","ò","õ","ô","ú","ù","û","ç","ñ"};
                List<String> nick = new ArrayList<>();
                int ctrl = 0;

                for(int x = 0; x < txtUsername.getText().trim().length(); x++){
                    String s = Character.toString(txtUsername.getText().trim().toLowerCase().charAt(x));
                    nick.add(s);
                }

                for(String i : invalid){
                    for(String j : nick){
                        if (j.equals(i)){
                            ctrl++;
                        }
                    }
                }

                if (txtUsername.getText().trim().isEmpty()) {
                    msglbl1.setText("O nickname não pode ficar vazio!");
                    nickValid = false;
                }

                else if (ctrl > 0) {
                    msglbl1.setText("<html>O nickname não pode conter: *, @, #, %, ','<br>espaço em branco ou letras com acentos</html>");
                    nickValid = false;
                }

                else {
                    msglbl1.setText("");
                    nickValid = true;
                }

                botaoHab();
            }
        });

        JLabel lblPass = new JLabel("Senha:");
        lblPass.setFont(pixelLabelFont);
        lblPass.setForeground(new Color(153, 204, 255));
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtPassword = new JPasswordField();
        txtPassword.setFont(customFont);
        txtPassword.setMaximumSize(new Dimension(250, 30));
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel msglbl2 = new JLabel();
        msglbl2.setFont(pixelLabelFont);
        msglbl2.setForeground(new Color(153, 204, 255));
        msglbl2.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtPassword.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validarSenha();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validarSenha();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validarSenha();
            }

            private void validarSenha() {

                char[] c_senha = txtPassword.getPassword();
                String s_senha = new String(c_senha).trim();

                if (s_senha.isEmpty()) {
                    msglbl2.setText("A senha não pode ficar vazia!");
                    senhaValid = false;
                }

                else {
                    msglbl2.setText("");
                    senhaValid = true;
                }

                botaoHab();
            }
        });

        btnLogin = new RoundedButton("Login", 20);
        btnLogin.setFont(buttonFont);
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(new Color(255, 255, 255));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setEnabled(false);

        btnRegister = new RoundedButton("Criar Conta", 20);
        btnRegister.setFont(buttonFont);
        btnRegister.setBackground(new Color(255, 51, 153));
        btnRegister.setForeground(new Color(255, 255, 255));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginPanel.add(label);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        loginPanel.add(lblUser);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loginPanel.add(txtUsername);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        loginPanel.add(lblPass);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loginPanel.add(txtPassword);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        loginPanel.add(btnLogin);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        loginPanel.add(btnRegister);

        backgroundPanel.add(loginPanel, new GridBagConstraints());
        setContentPane(backgroundPanel);

        btnLogin.addActionListener(a -> {

            String sqlSelect = "SELECT ID_Usuario, Nickname, Senha FROM usuario WHERE Nickname = ?";

            try (Connection connect = ConexaoR.getConnection(); PreparedStatement pstmt = connect.prepareStatement(sqlSelect)) {

                pstmt.setString(1, txtUsername.getText().trim());

                try(ResultSet rs = pstmt.executeQuery()){

                    while (rs.next()){
                        idUsuarioLogado = rs.getInt(1);
                        nick = rs.getString("Nickname");
                        senha = rs.getString("Senha");
                    }
                    if(nick != null && senha != null) {
                        if (nick.equals(txtUsername.getText().trim())) {
                            msglbl2.setText("");
                            nickValid = true;
                        } else {
                            msglbl2.setText("X");
                            nickValid = false;
                        }

                        char[] c_senha = txtPassword.getPassword();
                        String l_senha = new String(c_senha).trim();

                        if (senha.equals(l_senha)) {
                            msglbl2.setText("");
                            senhaValid = true;
                        } else {
                            msglbl2.setText("X");
                            senhaValid = false;
                        }

                        if (nickValid && senhaValid) {
                            JOptionPane.showMessageDialog(null, "<html>Login realizado com sucesso<br><br>Bem-vindo, " + nick + ".</html>", "Válido", JOptionPane.INFORMATION_MESSAGE);
                            new ArcadeLoja(idUsuarioLogado).setVisible(true);
                            this.dispose();
                        } else if (nickValid || senhaValid) {
                            JOptionPane.showMessageDialog(null, "<html>Nickname ou senha incorreto.</html>", "Alerta", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "<html>Nickname e senha incorretos.</html>", "Alerta", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "<html>O usuário: " + txtUsername.getText().trim() + " não existe.</html>", "Inválido", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }

            catch (SQLException e) {
                e.printStackTrace();
            }
        });

        btnRegister.addActionListener(e -> abrirTelaCadastro());
    }

    private void abrirTelaCadastro() {
        this.setVisible(false); 
        ArcadeRegistration registrationFrame = new ArcadeRegistration(this);
        registrationFrame.setVisible(true);
    }
    
    class ImagePanel extends JPanel {
        private BufferedImage image;
        public ImagePanel(String imagePath) {
            try {
                image = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        }
    }

    class NeonGradientLabel extends JLabel {
        private Color startColor;
        private Color endColor;
        private Color glowColor;
        private int glowSize;

        public NeonGradientLabel(String text, Font font, Color start, Color end, int glowSize) {
            super(text);
            this.setFont(font);
            this.startColor = start;
            this.endColor = end;
            this.glowColor = new Color(0, 204, 255, 100);
            this.glowSize = glowSize;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            
            if (getFont().getFontName().contains("PressStart")) {
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            } else {
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }

            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

            g2d.setColor(glowColor);
            for (int i = 0; i < glowSize; i++) {
                g2d.drawString(getText(), x - i, y);
                g2d.drawString(getText(), x + i, y);
                g2d.drawString(getText(), x, y - i);
                g2d.drawString(getText(), x, y + i);
            }
            
            GradientPaint gp = new GradientPaint(
                0, 0, startColor,
                getWidth(), getHeight(), endColor);
            g2d.setPaint(gp);
            g2d.drawString(getText(), x, y);
            g2d.dispose();
        }
    }

    class RoundedButton extends JButton {
        private int radius;
        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setOpaque(false);
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(getBackground());
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            
            super.paintComponent(g2d);
            
            g2d.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
        }
    }


}