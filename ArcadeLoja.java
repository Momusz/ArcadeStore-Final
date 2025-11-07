import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArcadeLoja extends JFrame {
    public int idUsuariologado;
    public ArcadeLoja(int idUsuariologado) {
        initialize(idUsuariologado);
    }

    private void initialize(int idUsuariologado) {
        setTitle("Arcade Store Loja");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        Font titleFont = new Font("Calibri", Font.BOLD, 50);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        try {
            InputStream is = getClass().getResourceAsStream("/PressStart2P-Regular.ttf");
            if (is != null) {
                try (BufferedInputStream bis = new BufferedInputStream(is)) {
                    Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, bis);
                    titleFont = pixelFont.deriveFont(Font.PLAIN, 50f);
                    buttonFont = pixelFont.deriveFont(Font.PLAIN, 16f);

                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(pixelFont);
                }
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Erro ao carregar a fonte. Usando padrão.");
        }

        ImagePanel backgroundPanel = new ImagePanel("src/fundo.jpg");
        backgroundPanel.setLayout(new BorderLayout());

        JLabel label = new NeonGradientLabel("ArcadeStore", titleFont,
                new Color(0, 204, 255),
                new Color(255, 51, 153),
                3);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(100, 0, 10, 0));

        JPanel painelbotao = new JPanel();
        painelbotao.setOpaque(false);
        painelbotao.setLayout(new BoxLayout(painelbotao, BoxLayout.X_AXIS));
        
        JButton btnsair = new RoundedButton("Sair", 15);
        btnsair.setFont(buttonFont);
        btnsair.setBackground(new Color(255, 51, 153));
        btnsair.setForeground(Color.WHITE);
        btnsair.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnsair.setBorderPainted(false);
        btnsair.addActionListener(e -> {
            System.exit(0);
        });

        JButton btnbiblioteca = new RoundedButton("Biblioteca", 15);
        btnbiblioteca.setFont(buttonFont);
        btnbiblioteca.setBackground(new Color(0, 102, 204));
        btnbiblioteca.setForeground(Color.WHITE);
        btnbiblioteca.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnbiblioteca.setBorderPainted(false);
        btnbiblioteca.addActionListener(e->{
            new ArcadeBiblioteca(idUsuariologado).setVisible(true);
        });


        JButton btnperfil = new RoundedButton("Perfil", 15);
        btnperfil.setFont(buttonFont);
        btnperfil.setBackground(new Color(0, 102, 204));
        btnperfil.setForeground(Color.WHITE);
        btnperfil.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnperfil.setBorderPainted(false);
        btnperfil.addActionListener(e->{
            new ArcadePerfil(idUsuariologado).setVisible(true);
        });

        painelbotao.add(Box.createHorizontalGlue());
        painelbotao.add(btnsair);
        painelbotao.add(Box.createRigidArea(new Dimension(20, 0)));
        painelbotao.add(btnbiblioteca);
        painelbotao.add(Box.createRigidArea(new Dimension(20, 0)));
        painelbotao.add(btnperfil);
        painelbotao.add(Box.createHorizontalGlue());

        JPanel panelBotoes = new JPanel();
        panelBotoes.setLayout(new BoxLayout(panelBotoes, BoxLayout.Y_AXIS));
        panelBotoes.setOpaque(false);
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(50, 20, 20, 20)); 
        panelBotoes.add(painelbotao);

        JPanel container = new JPanel();
        container.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        container.setOpaque(false);

        try {
            List<Jogo> jogos = buscarJogosDoBanco();

            for (Jogo jogo : jogos) {
                RoundedPanel card = new RoundedPanel(30);
                card.setPreferredSize(new Dimension(220, 340));
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBackground(new Color(255,255,255,40));
 
                card.setBorder(BorderFactory.createEmptyBorder());
                card.setOpaque(false);

                JLabel lblNome = new JLabel(jogo.nome, SwingConstants.CENTER); 
                lblNome.setFont(new Font("Arial", Font.BOLD, 19));
                lblNome.setForeground(Color.WHITE);
                lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel lblPreco = new JLabel("R$ " + String.format("%.2f", jogo.preco));
                lblPreco.setFont(new Font("Arial", Font.BOLD, 19));
                lblPreco.setForeground(Color.WHITE);
                lblPreco.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel lblImagem;
                
                try {
                    ImageIcon icon = new ImageIcon("src/imagens/" + jogo.imagem);
                    Image img = icon.getImage().getScaledInstance(180, 230, Image.SCALE_SMOOTH);
                    lblImagem = new JLabel(new ImageIcon(img));
                } catch (Exception e) {
                    lblImagem = new JLabel("[Sem Imagem]");
                }
                lblImagem.setAlignmentX(Component.CENTER_ALIGNMENT);

                
                RoundedButton btnVer = new RoundedButton("Ver", 20);
                btnVer.setFont(buttonFont);
                btnVer.setBackground(new Color(0, 102, 204));
                btnVer.setForeground(Color.WHITE);
                btnVer.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnVer.setBorderPainted(false);

                btnVer.addActionListener(e -> {
                    ArcadeJogo infoFrame = new ArcadeJogo(ArcadeLoja.this, jogo.nome, jogo.preco, jogo.imagem,jogo.id, idUsuariologado, jogo.categoria, jogo.desenvolvedor);
                    infoFrame.setVisible(true);
                });
                
                card.add(Box.createRigidArea(new Dimension(0, 10)));
                card.add(lblImagem);
                card.add(Box.createRigidArea(new Dimension(0, 10)));
                card.add(lblNome);
                card.add(lblPreco);
                card.add(Box.createRigidArea(new Dimension(0, 10)));
                card.add(btnVer);
                container.add(card);
                
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            ex.printStackTrace();
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(label, BorderLayout.NORTH);
        topPanel.add(panelBotoes, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(container);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0)); 
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)), BorderLayout.CENTER);
        mainPanel.add(scroll, BorderLayout.CENTER);

        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        setContentPane(backgroundPanel);
    }

    private List<Jogo> buscarJogosDoBanco() throws SQLException {
        List<Jogo> lista = new ArrayList<>();

        String URL = "jdbc:mysql://localhost:3306/Projeto";
        String USUARIO = "root";
        String SENHA = "admin";

        try (Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT jogos.id_jogos, jogos.nome, jogos.valor, jogos.categoria, jogos.imagens,desenvolvedor.nome as devnome FROM jogos inner join desenvolvedor on jogos.id_dev=desenvolvedor.id_dev")) {

            while (rs.next()) {
                lista.add(new Jogo(
                        rs.getString("nome"),
                        rs.getDouble("valor"),
                        rs.getString("imagens"),
                        rs.getInt("id_jogos"),
                        rs.getString("categoria"),
                        rs.getString("devnome")
                ));
            }
        }
        return lista;
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
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

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
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2d.setColor(getForeground());
            g2d.setFont(getFont());
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(getText(), x, y);

            g2d.dispose();
        }
    }

    class Jogo {
        public String nome;
        public double preco;
        public String imagem;
        public int id;
        public String categoria;
        public String desenvolvedor;

        public Jogo(String nome, double preco, String imagem, int id, String categoria, String desenvolvedor) {
            this.nome = nome;
            this.preco = preco;
            this.imagem = imagem;
            this.id = id;
            this.categoria = categoria;
            this.desenvolvedor = desenvolvedor; 
        }
    }

    class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); 
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2d.dispose();
        }
    }
}