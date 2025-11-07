import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.awt.geom.RoundRectangle2D;

public class ArcadeJogo extends JFrame {
    private JFrame lojaframe;

    public ArcadeJogo(JFrame lojaframe, String nome, double preco, String imagemPath,int id ,int idUsuariologado, String categoria, String desenvolvedor) {
        this.lojaframe = lojaframe;
        initializeUI(nome, preco, imagemPath,id,idUsuariologado, categoria, desenvolvedor);
    }

    private void initializeUI(String nome, double preco, String imagemPath, int id,int idUsuariologado, String categoria, String desenvolvedor) {
        setTitle("Jogo");
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
        Font pixelLabelFont = new Font("Arial", Font.BOLD, 18);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        try {
            InputStream is = new BufferedInputStream(
                getClass().getResourceAsStream("/PressStart2P-Regular.ttf"));
            if (is != null) {
                Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
                pixelLabelFont = pixelFont.deriveFont(Font.PLAIN, 20f);
                buttonFont = pixelFont.deriveFont(Font.PLAIN, 18f); 
                
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
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblImagem;
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new File("src/imagens/" + imagemPath)));
            Image img = icon.getImage().getScaledInstance(300, 400, Image.SCALE_SMOOTH);
            lblImagem = new JLabel(new ImageIcon(img));
        } catch (IOException e) {
            lblImagem = new JLabel("[Imagem não disponível]");
            lblImagem.setForeground(Color.WHITE);
        }
        lblImagem.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNome = new JLabel(nome);
        lblNome.setFont(pixelLabelFont);
        lblNome.setForeground(new Color(153, 204, 255));
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNome.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel categoriaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
categoriaPanel.setOpaque(false);

        JLabel lblTituloCategoria = new JLabel("Categorias: ");
        lblTituloCategoria.setFont(pixelLabelFont);
        lblTituloCategoria.setForeground(new Color(255, 204, 0));

        JLabel lblValorCategoria = new JLabel(categoria);
        lblValorCategoria.setFont(pixelLabelFont);
        lblValorCategoria.setForeground(new Color(255, 255, 153));

        categoriaPanel.add(lblTituloCategoria);
        categoriaPanel.add(lblValorCategoria);
        categoriaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoriaPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JPanel desenvolvedoraPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        desenvolvedoraPanel.setOpaque(false);

        JLabel lblTituloDesenvolvedora = new JLabel("Desenvolvedora: ");
        lblTituloDesenvolvedora.setFont(pixelLabelFont);
        lblTituloDesenvolvedora.setForeground(new Color(255, 204, 0));

        JLabel lblValorDesenvolvedora = new JLabel(desenvolvedor);
        lblValorDesenvolvedora.setFont(pixelLabelFont);
        lblValorDesenvolvedora.setForeground(new Color(255, 255, 153));

        desenvolvedoraPanel.add(lblTituloDesenvolvedora);
        desenvolvedoraPanel.add(lblValorDesenvolvedora);
        desenvolvedoraPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        desenvolvedoraPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));  

        JPanel precoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        precoPanel.setOpaque(false);

        JLabel lblTituloPreco = new JLabel("Preço: ");
        lblTituloPreco.setFont(pixelLabelFont);
        lblTituloPreco.setForeground(new Color(255, 204, 0));

        JLabel lblValorPreco = new JLabel("R$ " + String.format("%.2f", preco));
        lblValorPreco.setFont(pixelLabelFont);
        lblValorPreco.setForeground(new Color(255, 255, 153));

        precoPanel.add(lblTituloPreco);
        precoPanel.add(lblValorPreco);
        precoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnComprar = new RoundedButton("Comprar", 15);
        btnComprar.setFont(buttonFont);
        btnComprar.setBackground(new Color(0, 102, 204));
        btnComprar.setForeground(new Color(255, 255, 255));
        btnComprar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnComprar.setBorderPainted(false);
        btnComprar.setFocusPainted(false);

        btnComprar.addActionListener(e -> {
             new Pagamento().Pagamento(id, preco, nome, idUsuariologado);
        });
        
        
        JButton btnVoltar = new RoundedButton("Voltar", 15);
        btnVoltar.setFont(buttonFont);
        btnVoltar.setBackground(new Color(255, 51, 153));
        btnVoltar.setForeground(new Color(255, 255, 255));
        btnVoltar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVoltar.setBorderPainted(false);
        btnVoltar.setFocusPainted(false);
        
        btnVoltar.addActionListener(e -> {
            this.dispose();
            lojaframe.setVisible(true);
        });



        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(btnVoltar);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(btnComprar);
        buttonPanel.add(Box.createHorizontalGlue());

        panel.add(Box.createVerticalGlue());
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblImagem);
        panel.add(lblNome);
        panel.add(categoriaPanel);
        panel.add(desenvolvedoraPanel);
        panel.add(precoPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));


       
        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());

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