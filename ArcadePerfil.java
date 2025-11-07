import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedInputStream;
import java.awt.geom.RoundRectangle2D;

public class ArcadePerfil extends JFrame {
    private JFrame lojaframe;
    public Perfil perfil; 

    public ArcadePerfil(int idUsuariologado) {
        this.perfil = buscarPerfilDoBanco(idUsuariologado);
        initializeUI(idUsuariologado);
    }

    private Perfil buscarPerfilDoBanco(int idUsuariologado) {
        Perfil perfil = null;
        String URL = "jdbc:mysql://localhost:3306/Projeto";
        String USUARIO = "root";
        String SENHA = "admin";

        String sql = "SELECT nome, email, nickname, numero_cartao FROM usuario WHERE id_usuario = ?";

        try (Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuariologado); 

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    perfil = new Perfil(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("nickname"),
                        rs.getString("numero_cartao")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return perfil;
    }

    private void initializeUI(int idUsuariologado) {
        setTitle("Perfil");
        setSize(1280, 720);
        setLocationRelativeTo(lojaframe);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                lojaframe.setVisible(true);
            }
        });

        Font customFont = new Font("Arial", Font.PLAIN, 15);
        Font titulofont = new Font("Arial", Font.BOLD, 18);
        Font pixelLabelFont = new Font("Arial", Font.BOLD, 18);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        try {
            InputStream is = new BufferedInputStream(
                getClass().getResourceAsStream("/PressStart2P-Regular.ttf"));
            if (is != null) {
                Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
                titulofont= pixelFont.deriveFont(Font.PLAIN,30f);
                pixelLabelFont = pixelFont.deriveFont(Font.PLAIN, 20f);
                buttonFont = pixelFont.deriveFont(Font.PLAIN, 16f);
                
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(pixelFont);
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Erro ao carregar a fonte para a tela de cadastro. Usando fonte padrão.");
            e.printStackTrace();
        }

        JLabel label = new JLabel("Perfil");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(titulofont);
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(100, 0, 10, 0));
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(label, BorderLayout.NORTH);



        ImagePanel backgroundPanel = new ImagePanel("src/FundoEscuro.png");
        backgroundPanel.setLayout(new BorderLayout(20, 0));

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel imagePanel = new JPanel();
        imagePanel.setOpaque(false);
        imagePanel.setLayout(new BorderLayout());

        JLabel lblImagem;
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new File("src/icon2.jpg")));
            Image img = icon.getImage().getScaledInstance(285, 280, Image.SCALE_SMOOTH);
            lblImagem = new JLabel(new ImageIcon(img));
            lblImagem.setHorizontalAlignment(JLabel.CENTER);
        } catch (IOException e) {
            lblImagem = new JLabel("[Imagem não disponível]");
            lblImagem.setForeground(Color.WHITE);
            lblImagem.setHorizontalAlignment(JLabel.CENTER);
        }
        imagePanel.add(lblImagem, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        if (perfil.nome == null) {
            perfil.nome = "Sem nome";
        }
        
        JPanel nomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        nomePanel.setOpaque(false);

        JLabel lblNomeTitulo = new JLabel("Nome: ");
        lblNomeTitulo.setFont(pixelLabelFont);
        lblNomeTitulo.setForeground(new Color(255, 204, 0));

        JLabel lblNomeValor = new JLabel(perfil.nome);
        lblNomeValor.setFont(pixelLabelFont);
        lblNomeValor.setForeground(new Color(153, 204, 255));

        nomePanel.add(lblNomeTitulo);
        nomePanel.add(lblNomeValor);
        infoPanel.add(nomePanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);

        JPanel nicknamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        nicknamePanel.setOpaque(false);

        JLabel lblNicknameTitulo = new JLabel("Nickname: ");
        lblNicknameTitulo.setFont(pixelLabelFont);
        lblNicknameTitulo.setForeground(new Color(255, 204, 0));

        JLabel lblNicknameValor = new JLabel(perfil.nickname);
        lblNicknameValor.setFont(pixelLabelFont);
        lblNicknameValor.setForeground(new Color(102, 255, 102));

        nicknamePanel.add(lblNicknameTitulo);
        nicknamePanel.add(lblNicknameValor);
        infoPanel.add(nicknamePanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
       
        
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        emailPanel.setOpaque(false);

        JLabel lblEmailTitulo = new JLabel("Email: ");
        lblEmailTitulo.setFont(pixelLabelFont);
        lblEmailTitulo.setForeground(new Color(255, 204, 0));

        JLabel lblEmailValor = new JLabel(perfil.email);
        lblEmailValor.setFont(pixelLabelFont);
        lblEmailValor.setForeground(new Color(102, 204, 255));

        emailPanel.add(lblEmailTitulo);
        emailPanel.add(lblEmailValor);
        infoPanel.add(emailPanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
         
        String cartaoFormatado = perfil.numeroCartao != null && perfil.numeroCartao.length() >= 4 ? 
            "**** **** **** " + perfil.numeroCartao.substring(perfil.numeroCartao.length() - 4) : 
            "Sem cartão";
        
        JPanel numerocartaoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        numerocartaoPanel.setOpaque(false);

        JLabel lblCartaoTitulo = new JLabel("Cartão: ");
        lblCartaoTitulo.setFont(pixelLabelFont);
        lblCartaoTitulo.setForeground(new Color(255, 204, 0));

        JLabel lblCartaoValor = new JLabel(cartaoFormatado);
        lblCartaoValor.setFont(pixelLabelFont);
        lblCartaoValor.setForeground(new Color(255, 153, 255));

        numerocartaoPanel.add(lblCartaoTitulo);
        numerocartaoPanel.add(lblCartaoValor);
        infoPanel.add(numerocartaoPanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(50, 0, 0, 0);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton btnEditar = new RoundedButton("Atualizar Dados", 15);
        btnEditar.setFont(buttonFont);
        btnEditar.setBackground(new Color(0, 102, 204));
        btnEditar.setForeground(new Color(255, 255, 255));
        btnEditar.setBorderPainted(false);
        btnEditar.setFocusPainted(false);
        btnEditar.setMargin(new Insets(12, 25, 12, 25));

        btnEditar.addActionListener(e->{
            ArcadeEdicao Edicaoframe = new ArcadeEdicao(this,idUsuariologado);
            this.dispose();
            Edicaoframe.setVisible(true);
            
        });
        

        JButton btnVoltar = new RoundedButton("Voltar", 15);
        btnVoltar.setFont(buttonFont);
        btnVoltar.setBackground(new Color(255, 51, 153));
        btnVoltar.setForeground(new Color(255, 255, 255));
        btnVoltar.setBorderPainted(false);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setMargin(new Insets(12, 25, 12, 25));
        
        btnVoltar.addActionListener(e -> {
            this.dispose();
            if (lojaframe != null) {
                lojaframe.setVisible(true);
            }
        });

        buttonPanel.add(btnVoltar);
        buttonPanel.add(btnEditar);
        infoPanel.add(buttonPanel, gbc);

        mainPanel.add(imagePanel);
        mainPanel.add(infoPanel);
        backgroundPanel.add(topPanel, BorderLayout.NORTH);
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
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

    class Perfil {
        public String nome;
        public String email;
        public String nickname;
        public String numeroCartao;
        public Perfil(String nome, String email, String nickname, String numeroCartao) {
            this.nome = nome;
            this.email = email;
            this.nickname = nickname;
            this.numeroCartao = numeroCartao;
        }
    }
}