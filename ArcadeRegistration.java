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
import java.sql.*;
import java.util.Arrays;

public class ArcadeRegistration extends JFrame {
    private JFrame loginFrame;

    public ArcadeRegistration(JFrame loginFrame) {
        this.loginFrame = loginFrame;
        initializeUI();
    }

    private JTextField txtNewUser;
    private JTextField txtEmail;
    private JPasswordField txtNewPass;
    private JPasswordField txtConfirmPass;
    private JButton btnSave;
    private JButton btnBack;

    private String nick;
    private String email;
    private String senha;
    private boolean nickValid = true;
    private boolean emailValid = true;
    private boolean senhaValid = true;
    private boolean cSenhaValid = true;

    private void botaoHab() {
        boolean preenchido = !txtNewUser.getText().trim().isEmpty() && !txtEmail.getText().trim().isEmpty() && txtNewPass.getPassword().length > 0 && txtConfirmPass.getPassword().length > 0;
        boolean valido = nickValid && emailValid && senhaValid && cSenhaValid;
        btnSave.setEnabled(preenchido && valido);
    }

    private void initializeUI() {
        setTitle("Criar Conta - Arcade Store");
        setSize(1280, 720);
        setLocationRelativeTo(loginFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                loginFrame.setVisible(true);
            }
        });

        Font customFont = new Font("Arial", Font.PLAIN, 15);
        Font pixelLabelFont = new Font("Arial", Font.BOLD, 18);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        Font msgFont = new Font("Arial", Font.BOLD, 12);

        try {
            InputStream is = new BufferedInputStream(
                getClass().getResourceAsStream("/PressStart2P-Regular.ttf"));
            if (is != null) {
                Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
                pixelLabelFont = pixelFont.deriveFont(Font.PLAIN, 20f);
                buttonFont = pixelFont.deriveFont(Font.PLAIN, 18f);
                msgFont = pixelFont.deriveFont(Font.PLAIN, 12f);

                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(pixelFont);
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Erro ao carregar a fonte para a tela de cadastro. Usando fonte padrão.");
            e.printStackTrace();
        }

        ImagePanel backgroundPanel = new ImagePanel("src/FundoEscuro.png");
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser = new JLabel("Nickname:");
        lblUser.setFont(pixelLabelFont);
        lblUser.setForeground(new Color(153, 204, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblUser, gbc);

        txtNewUser = new JTextField();
        txtNewUser.setFont(customFont);
        txtNewUser.setPreferredSize(new Dimension(400, 35));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(txtNewUser, gbc);

        JLabel msglbl1 = new JLabel();
        msglbl1.setFont(msgFont);
        msglbl1.setForeground(new Color(255, 102, 102));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(msglbl1, gbc);

        txtNewUser.getDocument().addDocumentListener(new DocumentListener() {
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
                String[] invalid = {"*",",", " ", "@", "#", "%", "á", "à", "ã", "â", "é", "è", "ê", "í", "ì", "î", "ó", "ò", "õ", "ô", "ú", "ù", "û", "ç", "ñ"};
                int ctrl = 0;
                boolean nick_igual = false;
                String nicknameText = txtNewUser.getText().trim();

                for (String i : invalid) {
                    if (nicknameText.contains(i)) {
                        ctrl++;
                        break;
                    }
                }

                try (Connection connect = ConexaoR.getConnection()) {
                    Statement stmt = connect.createStatement();
                    try (ResultSet rs = stmt.executeQuery("SELECT Nickname FROM usuario;")) {
                        while (rs.next()) {
                            if (rs.getString("Nickname").equals(nicknameText)) {
                                nick_igual = true;
                                break;
                            }
                        }
                    }
                } catch (SQLException exc) {
                    exc.printStackTrace();
                }

                if (nicknameText.isEmpty()) {
                    msglbl1.setText("O nickname não pode ficar vazio!");
                    nickValid = false;
                } else if (ctrl > 0) {
                    msglbl1.setText("<html>O nickname não pode conter: *, @, #, %,<br>espaço em branco ou letras com acentos.</html>");
                    nickValid = false;
                } else if (nick_igual) {
                    msglbl1.setText("<html>Esse nickname já foi cadastrado.<br>Tente outro nickname.</html>");
                    nickValid = false;
                } else {
                    msglbl1.setText("");
                    nickValid = true;
                }
                botaoHab();
            }
        });

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(pixelLabelFont);
        lblEmail.setForeground(new Color(153, 204, 255));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(lblEmail, gbc);

        txtEmail = new JTextField();
        txtEmail.setFont(customFont);
        txtEmail.setPreferredSize(new Dimension(400, 35));
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(txtEmail, gbc);

        JLabel msglbl2 = new JLabel();
        msglbl2.setFont(msgFont);
        msglbl2.setForeground(new Color(255, 102, 102));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(msglbl2, gbc);

        txtEmail.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validarEmail();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validarEmail();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validarEmail();
            }

            private void validarEmail() {
                String[] invalid = {"*", ",", " ", "#", "%", "á", "à", "ã", "â", "é", "è", "ê", "í", "ì", "î", "ó", "ò", "õ", "ô", "ú", "ù", "û", "ç", "ñ"};
                int ctrl = 0;
                String emailText = txtEmail.getText().trim();

                for (String i : invalid) {
                    if (emailText.contains(i)) {
                        ctrl++;
                        break;
                    }
                }

                if (emailText.isEmpty()) {
                    msglbl2.setText("O email não pode ficar vazio!");
                    emailValid = false;
                } else if (ctrl > 0) {
                    msglbl2.setText("<html>O email não pode conter: *, #, %,<br>espaço em branco ou letras com acentos.</html>");
                    emailValid = false;
                } else {
                    msglbl2.setText("");
                    emailValid = true;
                }
                botaoHab();
            }
        });

        JLabel lblPass = new JLabel("Senha:");
        lblPass.setFont(pixelLabelFont);
        lblPass.setForeground(new Color(153, 204, 255));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(lblPass, gbc);

        txtNewPass = new JPasswordField();
        txtNewPass.setFont(customFont);
        txtNewPass.setPreferredSize(new Dimension(400, 35));
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(txtNewPass, gbc);

        JLabel msglbl3 = new JLabel();
        msglbl3.setFont(msgFont);
        msglbl3.setForeground(new Color(255, 102, 102));
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(msglbl3, gbc);

        txtNewPass.getDocument().addDocumentListener(new DocumentListener() {
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
                char[] c_senha1 = txtNewPass.getPassword();
                char[] c_senha2 = txtConfirmPass.getPassword();

                if (new String(c_senha1).trim().isEmpty()) {
                    msglbl3.setText("Este campo não pode ficar vazio!");
                    senhaValid = false;
                } else {
                    msglbl3.setText("");
                    senhaValid = true;
                }

                if (c_senha2.length > 0 && !Arrays.equals(c_senha1, c_senha2)) {
                    msglbl3.setText("As senhas são diferentes!");
                    cSenhaValid = false;
                } else {
                    cSenhaValid = true;
                }
                botaoHab();
            }
        });

        JLabel lblConfirmPass = new JLabel("Confirmar Senha:");
        lblConfirmPass.setFont(pixelLabelFont);
        lblConfirmPass.setForeground(new Color(153, 204, 255));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(lblConfirmPass, gbc);

        txtConfirmPass = new JPasswordField();
        txtConfirmPass.setFont(customFont);
        txtConfirmPass.setPreferredSize(new Dimension(400, 35));
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panel.add(txtConfirmPass, gbc);

        JLabel msglbl4 = new JLabel();
        msglbl4.setFont(msgFont);
        msglbl4.setForeground(new Color(255, 102, 102));
        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(msglbl4, gbc);

        txtConfirmPass.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validarCSenha();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validarCSenha();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validarCSenha();
            }

            private void validarCSenha() {
                char[] c_senha1 = txtNewPass.getPassword();
                char[] c_senha2 = txtConfirmPass.getPassword();

                if (new String(c_senha2).trim().isEmpty()) {
                    msglbl4.setText("Este campo não pode ficar vazio!");
                    cSenhaValid = false;
                } else {
                    msglbl4.setText("");
                    cSenhaValid = true;
                }

                if (!Arrays.equals(c_senha1, c_senha2)) {
                    msglbl4.setText("As senhas são diferentes!");
                    cSenhaValid = false;
                } else {
                    msglbl4.setText("");
                    cSenhaValid = true;
                }
                botaoHab();
            }
        });
        
        btnSave = new RoundedButton("Finalizar Cadastro", 15);
        btnSave.setFont(buttonFont);
        btnSave.setBackground(new Color(0, 102, 204));
        btnSave.setForeground(new Color(255, 255, 255));
        btnSave.setEnabled(false);

        btnBack = new RoundedButton("Voltar", 15);
        btnBack.setFont(buttonFont);
        btnBack.setBackground(new Color(255, 51, 153));
        btnBack.setForeground(new Color(255, 255, 255));

        btnBack.addActionListener(e -> {
            this.dispose();
            loginFrame.setVisible(true);
        });

        btnSave.addActionListener(a -> {
            if (nickValid && emailValid && senhaValid && cSenhaValid) {
                this.nick = txtNewUser.getText().trim();
                this.email = txtEmail.getText().trim().toLowerCase();
                char[] c_senha = txtNewPass.getPassword();
                this.senha = new String(c_senha).trim();

                try (Connection connect = ConexaoR.getConnection()) {
                    String sqlInsert = "INSERT INTO usuario (Nickname, Email, Senha) VALUES (?, ?, ?)";
                    try (PreparedStatement pstmt = connect.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                        pstmt.setString(1, nick);
                        pstmt.setString(2, email);
                        pstmt.setString(3, senha);
                        pstmt.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso", "Válido", JOptionPane.INFORMATION_MESSAGE);
                        btnSave.setEnabled(false);
                        this.dispose();
                        loginFrame.setVisible(true);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cadastro negado", "Inválido", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnSave);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(btnBack);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        backgroundPanel.add(panel, new GridBagConstraints());
        setContentPane(backgroundPanel);
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