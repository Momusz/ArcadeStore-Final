import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VerificarCartao {
    public static boolean verificarCartao(int idUsuario) {
        String sql = "select count(CVV) from usuario where id_usuario = ?;";
        try (Connection conexao = ConexaoR.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet resultado = stmt.executeQuery()) {
                if (resultado.next() && resultado.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro: " + e.getMessage(),
                "Erro no Sistema",
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    public static void RegistraCartao(int idUsuariologado) {

        JFrame TelaCartao = new JFrame();

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
		fonteTituloCartao = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("src/PressStart2P-Regular.ttf")).deriveFont(25f);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();	
		ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new java.io.File("src/PressStart2P-Regular.ttf")));
	} catch (Exception e) {
		e.printStackTrace();
	}

        JLabel cartao = new JLabel("Cartão de Crédito");
	    cartao.setBounds(35, 5, 500, 100);
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
        cvvCartaoLabel.setBounds(260, 205, 50, 30);
        JTextField cvvCartao = new JTextField("");
        cvvCartao.setBounds(260, 230, 100, 30);

        JButton botaoConfirmarCartao = new RoundedButton("Confirmar Compra", 25);
        botaoConfirmarCartao.setBackground(new Color(255, 51, 153));
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
                String NumeroDigitado = numeroCartao.getText().trim();
                String NomeDigitado = nomeCartao.getText().trim();
                String ValidadeDigitado = validadeCartao.getText().trim();
                if (cvvDigitado.isEmpty()||NumeroDigitado.isEmpty()||NomeDigitado.isEmpty()||ValidadeDigitado.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, Preencha todos os campos !");
                    return;
                }
                if (!cvvDigitado.matches("\\d{3}")) {
                    JOptionPane.showMessageDialog(null, 
                      "O CVV deve conter exatamente 3 números!", 
                         "CVV Inválido", 
                        JOptionPane.WARNING_MESSAGE);
                    cvvCartao.setText("");
                    cvvCartao.requestFocus();
                    return;
                }
                if (!NumeroDigitado.matches("\\d{16}")) {
                    JOptionPane.showMessageDialog(null, 
                      "O Número do cartão deve conter 16 Números !", 
                         "Número de Cartão Inválido", 
                        JOptionPane.WARNING_MESSAGE);
                    numeroCartao.setText("");
                    numeroCartao.requestFocus();
                    return;
                }

                registrarCartao(idUsuariologado, cvvDigitado, NumeroDigitado);
                TelaCartao.dispose();               
            }
        });
    
        JButton botaoVoltarCartao = new RoundedButton("Voltar", 25);
        botaoVoltarCartao.setBackground(new Color(0, 102, 204));
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
    TelaCartao.add(nomeCartaoLabel);
    TelaCartao.add(nomeCartao);
    TelaCartao.add(numeroCartaoLabel);
    TelaCartao.add(validadeCartaoLabel);
    TelaCartao.add(cvvCartaoLabel);
    TelaCartao.add(numeroCartao);
    TelaCartao.add(validadeCartao);
    TelaCartao.add(cvvCartao);
    
    TelaCartao.setLocationRelativeTo(null);
    TelaCartao.setLayout(null);
    TelaCartao.setBounds(550, 180, 500, 400);
    TelaCartao.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    TelaCartao.add(cartao);
    TelaCartao.setVisible(true);
    }   

    private static void registrarCartao(int idUsuario,String cvvDigitado,String numeroDigitado) {
        String sql = "update usuario set cvv=?, numero_cartao=? where id_usuario =?";
        try (Connection conexao = ConexaoR.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, cvvDigitado);
            stmt.setString(2, numeroDigitado);
            stmt.setInt(3, idUsuario);


            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(null,
                    "✅ Cartão registrado com sucesso!\n\n" +
                    "Agora você pode prosseguir com suas compras.",
                    "Cartão Registrado",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                    "Erro ao registrar a compra.",
                    "Erro no Sistema",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao registrar pedido: " + e.getMessage(),
                "Erro no Sistema",
                JOptionPane.ERROR_MESSAGE);
        }
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





