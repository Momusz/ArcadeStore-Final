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

public class ArcadeEdicao extends JFrame {
    private JFrame ArcadePerfil;
    private int idUsuariologado;

    public ArcadeEdicao(JFrame ArcadePerfil,int idUsuariologado) {
        this.idUsuariologado=idUsuariologado;
        this.ArcadePerfil = ArcadePerfil;
        initializeUI();
    }

    private JTextField txtNome;
    private JTextField txtNewUser;
    private JTextField txtEmail;
    private JPasswordField txtNewPass;
    private JPasswordField txtConfirmPass;
    private JTextField txtNumeroCartao;
    private JTextField txtCVV;
    private JButton btnSave;
    private JButton btnBack;

    private String nome;
    private String nick;
    private String email;
    private String senha;
    private String numeroCartao;
    private String cvv;
    private boolean nomeValid = true;
    private boolean nickValid = true;
    private boolean emailValid = true;
    private boolean senhaValid = true;
    private boolean cSenhaValid = true;
    private boolean cartaoValid = true;
    private boolean cvvValid = true;

    private void botaoHab() {
        boolean preenchido = !txtNome.getText().trim().isEmpty() &&
                           !txtNewUser.getText().trim().isEmpty() &&
                           !txtEmail.getText().trim().isEmpty() &&
                           txtNewPass.getPassword().length > 0 &&
                           txtConfirmPass.getPassword().length > 0 &&
                           !txtNumeroCartao.getText().trim().isEmpty() &&
                           !txtCVV.getText().trim().isEmpty();

        boolean valido = nomeValid && nickValid && emailValid && senhaValid && cSenhaValid && cartaoValid && cvvValid;
        btnSave.setEnabled(preenchido && valido);
    }

    private void initializeUI() {
        setTitle("Criar Conta - Arcade Store");
        setSize(1280, 720);
        setLocationRelativeTo(ArcadePerfil);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ArcadePerfil.setVisible(true);
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

        try (Connection connect = ConexaoR.getConnection()) {
            PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM usuario WHERE id_usuario = ?;");
            pstmt.setInt(1, idUsuariologado);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    nome = rs.getString("Nome");
                    nick = rs.getString("Nickname");
                    email = rs.getString("Email");
                    senha = rs.getString("Senha");
                    numeroCartao = rs.getString("Numero_Cartao");
                    cvv = rs.getString("CVV");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        JLabel lblNome = new JLabel("Nome Completo:");
        lblNome.setFont(pixelLabelFont);
        lblNome.setForeground(new Color(153, 204, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblNome, gbc);

        txtNome = new JTextField();
        txtNome.setText(nome);
        txtNome.setFont(customFont);
        txtNome.setPreferredSize(new Dimension(400, 35));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(txtNome, gbc);

        JLabel msglblNome = new JLabel();
        msglblNome.setFont(msgFont);
        msglblNome.setForeground(new Color(255, 102, 102));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(msglblNome, gbc);

        txtNome.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validarNome(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validarNome(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validarNome(); }

            private void validarNome() {
                String nomeText = txtNome.getText().trim();
                
                if (nomeText.isEmpty()) {
                    msglblNome.setText("O nome não pode ficar vazio!");
                    nomeValid = false;
                } else if (nomeText.length() < 2) {
                    msglblNome.setText("O nome deve ter pelo menos 2 caracteres!");
                    nomeValid = false;
                } else {
                    msglblNome.setText("");
                    nomeValid = true;
                }
                botaoHab();
            }
        });

        JLabel lblUser = new JLabel("Nickname:");
        lblUser.setFont(pixelLabelFont);
        lblUser.setForeground(new Color(153, 204, 255));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(lblUser, gbc);

        txtNewUser = new JTextField();
        txtNewUser.setText(nick);
        txtNewUser.setFont(customFont);
        txtNewUser.setPreferredSize(new Dimension(400, 35));
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(txtNewUser, gbc);

        JLabel msglbl1 = new JLabel();
        msglbl1.setFont(msgFont);
        msglbl1.setForeground(new Color(255, 102, 102));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(msglbl1, gbc);

        txtNewUser.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validarNick(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validarNick(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validarNick(); }

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
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(lblEmail, gbc);

        txtEmail = new JTextField();
        txtEmail.setText(email);
        txtEmail.setFont(customFont);
        txtEmail.setPreferredSize(new Dimension(400, 35));
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(txtEmail, gbc);

        JLabel msglbl2 = new JLabel();
        msglbl2.setFont(msgFont);
        msglbl2.setForeground(new Color(255, 102, 102));
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(msglbl2, gbc);

        txtEmail.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validarEmail(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validarEmail(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validarEmail(); }

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
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(lblPass, gbc);

        txtNewPass = new JPasswordField();
        txtNewPass.setText(senha);
        txtNewPass.setFont(customFont);
        txtNewPass.setPreferredSize(new Dimension(400, 35));
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panel.add(txtNewPass, gbc);

        JLabel msglbl3 = new JLabel();
        msglbl3.setFont(msgFont);
        msglbl3.setForeground(new Color(255, 102, 102));
        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(msglbl3, gbc);

        txtNewPass.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validarSenha(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validarSenha(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validarSenha(); }

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
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        panel.add(lblConfirmPass, gbc);

        txtConfirmPass = new JPasswordField();
        txtConfirmPass.setText(senha);
        txtConfirmPass.setFont(customFont);
        txtConfirmPass.setPreferredSize(new Dimension(400, 35));
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        panel.add(txtConfirmPass, gbc);

        JLabel msglbl4 = new JLabel();
        msglbl4.setFont(msgFont);
        msglbl4.setForeground(new Color(255, 102, 102));
        gbc.gridx = 1;
        gbc.gridy = 9;
        panel.add(msglbl4, gbc);

        txtConfirmPass.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validarCSenha(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validarCSenha(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validarCSenha(); }

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

        JLabel lblNumeroCartao = new JLabel("Número do Cartão:");
        lblNumeroCartao.setFont(pixelLabelFont);
        lblNumeroCartao.setForeground(new Color(153, 204, 255));
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        panel.add(lblNumeroCartao, gbc);

        txtNumeroCartao = new JTextField();
        txtNumeroCartao.setText(numeroCartao);
        txtNumeroCartao.setFont(customFont);
        txtNumeroCartao.setPreferredSize(new Dimension(400, 35));
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        panel.add(txtNumeroCartao, gbc);

        JLabel msglblCartao = new JLabel();
        msglblCartao.setFont(msgFont);
        msglblCartao.setForeground(new Color(255, 102, 102));
        gbc.gridx = 1;
        gbc.gridy = 11;
        panel.add(msglblCartao, gbc);

        txtNumeroCartao.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validarCartao(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validarCartao(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validarCartao(); }

            private void validarCartao() {
                String cartaoText = txtNumeroCartao.getText().trim().replaceAll("\\s+", "");
                
                if (cartaoText.isEmpty()) {
                    msglblCartao.setText("O número do cartão não pode ficar vazio!");
                    cartaoValid = false;
                } else if (!cartaoText.matches("\\d+")) {
                    msglblCartao.setText("O cartão deve conter apenas números!");
                    cartaoValid = false;
                } else if (cartaoText.length() < 13 || cartaoText.length() > 19) {
                    msglblCartao.setText("O cartão deve ter entre 13 e 19 dígitos!");
                    cartaoValid = false;
                } else {
                    msglblCartao.setText("");
                    cartaoValid = true;
                }
                botaoHab();
            }
        });

        JLabel lblCVV = new JLabel("CVV:");
        lblCVV.setFont(pixelLabelFont);
        lblCVV.setForeground(new Color(153, 204, 255));
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        panel.add(lblCVV, gbc);

        txtCVV = new JTextField();
        txtCVV.setText(cvv);
        txtCVV.setFont(customFont);
        txtCVV.setPreferredSize(new Dimension(400, 35));
        gbc.gridy = 13;
        gbc.gridwidth = 1;
        panel.add(txtCVV, gbc);

        JLabel msglblCVV = new JLabel();
        msglblCVV.setFont(msgFont);
        msglblCVV.setForeground(new Color(255, 102, 102));
        gbc.gridx = 1;
        gbc.gridy = 13;
        panel.add(msglblCVV, gbc);

        txtCVV.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { validarCVV(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validarCVV(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validarCVV(); }

            private void validarCVV() {
                String cvvText = txtCVV.getText().trim();
                
                if (cvvText.isEmpty()) {
                    msglblCVV.setText("O CVV não pode ficar vazio!");
                    cvvValid = false;
                } else if (!cvvText.matches("\\d+")) {
                    msglblCVV.setText("O CVV deve conter apenas números!");
                    cvvValid = false;
                } else if (cvvText.length() != 3 && cvvText.length() != 4) {
                    msglblCVV.setText("O CVV deve ter 3 ou 4 dígitos!");
                    cvvValid = false;
                } else {
                    msglblCVV.setText("");
                    cvvValid = true;
                }
                botaoHab();
            }
        });

        btnSave = new RoundedButton("Finalizar Edição", 15);
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
            ArcadePerfil.setVisible(true);
        });

        botaoHab();

        btnSave.addActionListener(a -> {
            if (nomeValid && nickValid && emailValid && senhaValid && cSenhaValid && cartaoValid && cvvValid) {
                this.nome = txtNome.getText().trim();
                this.nick = txtNewUser.getText().trim();
                this.email = txtEmail.getText().trim().toLowerCase();
                char[] c_senha = txtNewPass.getPassword();
                this.senha = new String(c_senha).trim();
                this.numeroCartao = txtNumeroCartao.getText().trim().replaceAll("\\s+", "");
                this.cvv = txtCVV.getText().trim();

                try (Connection connect = ConexaoR.getConnection()) {
                    String sqlInsert = "update usuario set Nome=?, Nickname=?, Email=?, Senha=?, numero_cartao=?, cvv=?  where id_usuario =?";
                    try (PreparedStatement pstmt = connect.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                        pstmt.setString(1, nome);
                        pstmt.setString(2, nick);
                        pstmt.setString(3, email);
                        pstmt.setString(4, senha);
                        pstmt.setString(5, numeroCartao);
                        pstmt.setString(6, cvv);
                        pstmt.setInt(7,idUsuariologado);
                        pstmt.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso", "Válido", JOptionPane.INFORMATION_MESSAGE);
                        btnSave.setEnabled(false);
                        this.dispose(); 
                        ArcadePerfil perfil= new ArcadePerfil(idUsuariologado);
                        perfil.setVisible(true);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Atualização negada", "Inválido", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnSave);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(btnBack);

        JPanel obsPanel = new JPanel();
        obsPanel.setOpaque(false);
        obsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel obsLbl = new JLabel("*Preencha todos os campos");
        obsLbl.setFont(msgFont);
        obsLbl.setForeground(new Color(255, 102, 102));
        obsPanel.add(obsLbl);

        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);
        gbc.gridy = 15;
        panel.add(obsPanel, gbc);

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