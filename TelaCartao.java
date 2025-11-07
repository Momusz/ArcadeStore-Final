
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class TelaCartao {
    void TelaCartao(int idjogos, double preco, String nome ,int idUsuariologado) {

        JFrame TelaCartao = new JFrame();
        TelaCartao.setTitle("Pagamento com Cartão de Crédito");
	
		JPanel imagemFundoCartao = new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon imagem = new ImageIcon("src/FundoEscuro.png");
				g.drawImage(imagem.getImage(), 0, 0, 600, 400, this);
			}
		};
		imagemFundoCartao.setLayout(new BorderLayout());
		TelaCartao.setContentPane(imagemFundoCartao);

        Font fonteTituloCartao = new Font("Verdana", Font.BOLD, 20);

	try {
		fonteTituloCartao = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("C:/Users/monic/OneDrive/Documentos/ArcadeStore/Teste/src/PressStart2P-Regular.ttf")).deriveFont(25f);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();	
		ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new java.io.File("C:/Users/monic/OneDrive/Documentos/ArcadeStore/Teste/src/PressStart2P-Regular.ttf")));
	} catch (Exception e) {
		e.printStackTrace();
	}

        JLabel cartao = new JLabel("Digite o CVV do Cartão cadastrado");
	    cartao.setBounds(50, 5, 500, 100);
        cartao.setFont(fonteTituloCartao);
        cartao.setForeground(Color.WHITE);

        JLabel nomeCartaoLabel = new JLabel("Nome no Cartão:");
        nomeCartaoLabel.setBounds(100, 50, 100, 100);
        nomeCartaoLabel.setForeground(Color.WHITE);
        JTextField nomeCartao = new JTextField("");
        nomeCartao.setBounds(100, 110, 300, 30);

        JLabel numeroCartaoLabel = new JLabel("Número do Cartão:");
        numeroCartaoLabel.setForeground(Color.WHITE);
        numeroCartaoLabel.setBounds(100, 110, 200, 100);
        JTextField numeroCartao = new JTextField("");
        numeroCartao.setBounds(100, 170, 300, 30);

        JLabel validadeCartaoLabel = new JLabel("Validade:");
        validadeCartaoLabel.setForeground(Color.WHITE);
        validadeCartaoLabel.setBounds(100, 170, 100, 100);
        JTextField validadeCartao = new JTextField("");
        validadeCartao.setBounds(100, 230, 100, 30);

        JLabel cvvCartaoLabel = new JLabel("CVV:");
        cvvCartaoLabel.setForeground(Color.WHITE);
        cvvCartaoLabel.setBounds(190, 105, 50, 30);
        JTextField cvvCartao = new JTextField("");
        cvvCartao.setBounds(190, 130, 100, 30);

        JButton botaoConfirmarCartao = new RoundedButton("Confirmar Compra", 25);
        botaoConfirmarCartao.setBackground(new Color(0, 102, 204));
        botaoConfirmarCartao.setForeground(Color.WHITE);
        botaoConfirmarCartao.setFont(new Font("Arial", Font.BOLD, 12));
        botaoConfirmarCartao.setOpaque(false);
        botaoConfirmarCartao.setBorderPainted(false);
        botaoConfirmarCartao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoConfirmarCartao.setBounds(240, 300, 140, 30);
        botaoConfirmarCartao.setFocusPainted(false);
        botaoConfirmarCartao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String cvvDigitado = cvvCartao.getText().trim();
                 ConfirmaCompra confirma = new ConfirmaCompra();
                confirma.confirmarCompra(idjogos, nome, preco, idUsuariologado,cvvDigitado, TelaCartao);
                
            }
        });
    
        JButton botaoVoltarCartao = new RoundedButton("Voltar", 25);
        botaoVoltarCartao.setBackground(new Color(255, 51, 153));
        botaoVoltarCartao.setForeground(Color.WHITE);
        botaoVoltarCartao.setFont(new Font("Arial", Font.BOLD, 12));
        botaoVoltarCartao.setOpaque(false);
        botaoVoltarCartao.setBorderPainted(false);
        botaoVoltarCartao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoVoltarCartao.setBounds(110, 300, 80, 30);
        botaoVoltarCartao.setFocusPainted(false);
        botaoVoltarCartao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                TelaCartao.dispose(); 
            }
        });

    TelaCartao.add(botaoVoltarCartao);
    TelaCartao.add(botaoConfirmarCartao);
    TelaCartao.add(cvvCartaoLabel);
    TelaCartao.add(cvvCartao);
    
    TelaCartao.setLocationRelativeTo(null);
    TelaCartao.setLayout(null);
    TelaCartao.setBounds(550, 180, 500, 400);
    TelaCartao.add(cartao);
    TelaCartao.setVisible(true);
    }   

}

class RoundedButtonCartao extends JButton {
    private int radius;

    public RoundedButtonCartao(String label, int radius) {
        super(label);
        this.radius = radius;
        setContentAreaFilled(false);
    }

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
    }

    Shape shape;

    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius);
        }
        return shape.contains(x, y);
    }
}