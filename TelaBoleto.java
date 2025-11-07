
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


public class TelaBoleto {

    public static void main(String[] args) {
        JFrame telaBoleto = new JFrame("Boleto");

		JPanel imagemFundoBoleto = new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon imagem = new ImageIcon("src/FundoEscuro.png");
				g.drawImage(imagem.getImage(), 0, 0, 600, 400, this);
			}
		};
		imagemFundoBoleto.setLayout(new BorderLayout());
		telaBoleto.setContentPane(imagemFundoBoleto);


	Font fonteTituloBoleto = new Font("Verdana", Font.BOLD, 20);

	try {
		fonteTituloBoleto = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("src/PressStart2P-Regular.ttf")).deriveFont(25f);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();	
		ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new java.io.File("src/PressStart2P-Regular.ttf")));
	} catch (Exception e) {
		e.printStackTrace();
	}

        JLabel tituloBoleto = new JLabel("Boleto");
        tituloBoleto.setBounds(130, 5, 300, 100);
        tituloBoleto.setFont(fonteTituloBoleto);
        tituloBoleto.setForeground(Color.WHITE);

        JLabel nomeBoletoLabel = new JLabel("Nome:");
        nomeBoletoLabel.setBounds(50, 120, 100, 30);
        nomeBoletoLabel.setForeground(Color.WHITE);
        JTextField nomeBoleto = new JTextField();
        nomeBoleto.setBounds(120, 120, 200, 30);

        JLabel cpfBoletoLabel = new JLabel("CPF:");
        cpfBoletoLabel.setBounds(50, 170, 100, 30);
        cpfBoletoLabel.setForeground(Color.WHITE);
        JTextField cpfBoleto = new JTextField();
        cpfBoleto.setBounds(120, 170, 200, 30);

        JButton botaoGerarBoleto = new RoundedButton("Gerar Boleto", 25);
        botaoGerarBoleto.setBackground(new Color(255, 51, 153));
        botaoGerarBoleto.setForeground(Color.WHITE);
        botaoGerarBoleto.setFont(new Font("Arial", Font.BOLD, 12));
        botaoGerarBoleto.setOpaque(false);
        botaoGerarBoleto.setBorderPainted(false);
        botaoGerarBoleto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoGerarBoleto.setBounds(190, 250, 120, 30);
        botaoGerarBoleto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }  
        });

        JButton botaoVoltarBoleto = new RoundedButton("Voltar", 25);
        botaoVoltarBoleto.setBackground(new Color(0, 102, 204));
        botaoVoltarBoleto.setForeground(Color.WHITE);
        botaoVoltarBoleto.setFont(new Font("Arial", Font.BOLD, 12));
        botaoVoltarBoleto.setOpaque(false);
        botaoVoltarBoleto.setBorderPainted(false);
        botaoVoltarBoleto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoVoltarBoleto.setBounds(80, 250, 80, 30);
        botaoVoltarBoleto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                telaBoleto.dispose(); 
            }
        });
        

        telaBoleto.add(tituloBoleto);
        telaBoleto.add(nomeBoletoLabel);
        telaBoleto.add(nomeBoleto);
        telaBoleto.add(botaoGerarBoleto);
        telaBoleto.add(cpfBoletoLabel);
        telaBoleto.add(cpfBoleto);
        telaBoleto.add(botaoVoltarBoleto);

        telaBoleto.setLayout(null);
        telaBoleto.setBounds(600, 200, 400, 400);
        telaBoleto.setVisible(true);
    }
}


class RoundedButtonBoleto extends JButton {
    private int radius;

    public RoundedButtonBoleto(String label, int radius) {
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