import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;

public class TelaPix {
    public static void main(String[] args) {
        JFrame telaPix = new JFrame();

		JPanel imagemFundoPix = new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon imagem = new ImageIcon("src/FundoEscuro.png");
				g.drawImage(imagem.getImage(), 0, 0, 600, 400, this);
			}
		};
		imagemFundoPix.setLayout(new BorderLayout());
		telaPix.setContentPane(imagemFundoPix);

        Font fonteTituloPix = new Font("Verdana", Font.BOLD, 20);

        try {
            fonteTituloPix = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/PressStart2P-Regular.ttf"))
                    .deriveFont(25f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/PressStart2P-Regular.ttf")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel Pix = new JLabel("PIX");
        Pix.setBounds(160, 5, 100, 100);
        Pix.setFont(fonteTituloPix);
        Pix.setForeground(Color.WHITE);

        JLabel NomePixLabel = new JLabel("Nome:");
        NomePixLabel.setBounds(50, 110, 100, 30);
        NomePixLabel.setForeground(Color.WHITE);
        JTextField NomePix = new JTextField();
        NomePix.setBounds(120, 110, 200, 30);

        JLabel CpfLabel = new JLabel("CPF:");
        CpfLabel.setBounds(50, 160, 100, 30);
        CpfLabel.setForeground(Color.WHITE);
        JTextField CpfPix = new JTextField();
        CpfPix.setBounds(120, 160, 200, 30);

        
        JButton botaoContinuarPix = new RoundedButton("Continuar", 25);
        botaoContinuarPix.setBackground(new Color(255, 51, 153));
        botaoContinuarPix.setForeground(Color.WHITE);
        botaoContinuarPix.setFont(new Font("Arial", Font.BOLD, 12));
        botaoContinuarPix.setOpaque(false);
        botaoContinuarPix.setBorderPainted(false);
        botaoContinuarPix.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoContinuarPix.setBounds(200, 230, 100, 30);
        botaoContinuarPix.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            }
        });

        JButton botaoVoltar = new RoundedButton("Voltar", 25);
        botaoVoltar.setBackground(new Color(0, 102, 204));
        botaoVoltar.setForeground(Color.WHITE);
        botaoVoltar.setFont(new Font("Arial", Font.BOLD, 12));
        botaoVoltar.setOpaque(false);
        botaoVoltar.setBorderPainted(false);
        botaoVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoVoltar.setBounds(80, 230, 80, 30);
        botaoVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                telaPix.dispose();
            }
        });

        telaPix.add(botaoVoltar);
        telaPix.add(Pix);
        telaPix.add(NomePixLabel);
        telaPix.add(NomePix);
        telaPix.add(CpfLabel);
        telaPix.add(CpfPix);
        telaPix.add(botaoContinuarPix);

        telaPix.setLayout(null);
        telaPix.setBounds(600, 200, 400, 400);
        telaPix.setVisible(true);
    }
}

class RoundedButton extends JButton {
    private int radius;

    public RoundedButton(String label, int radius) {
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