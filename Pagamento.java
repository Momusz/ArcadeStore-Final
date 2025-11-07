import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Pagamento {

void Pagamento(int idjogos, double preco, String nome, int idUsuariologado) {
	JFrame pagamento = new JFrame();	


		JPanel imagemFundoPagamento = new JPanel() {
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon imagem = new ImageIcon("src/FundoEscuro.png");
				g.drawImage(imagem.getImage(), 0, 0, 600, 400, this);
			}
		};
		imagemFundoPagamento.setLayout(new BorderLayout());
		pagamento.setContentPane(imagemFundoPagamento);


	Font fonteTitulo = new Font("Verdana", Font.BOLD, 20);

	try {
		fonteTitulo = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("src/PressStart2P-Regular.ttf")).deriveFont(25f);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();	
		ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new java.io.File("src/PressStart2P-Regular.ttf")));
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	JLabel tituloPagamento = new JLabel("PAGAMENTO ");
	tituloPagamento.setBounds(90, 5, 300, 100);
	tituloPagamento.setFont(fonteTitulo);
	tituloPagamento.setForeground(Color.WHITE);
	
	JButton botaoPix = new JButton("Pix");
	botaoPix.setBounds(120, 130, 150, 30);
	botaoPix.setFocusPainted(false);
	botaoPix.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			TelaPix.main(null);
			
		}
	});

	JButton botaoCartao = new JButton("Cartão de crédito");
	botaoCartao.setBounds(120, 180, 150, 30);
	botaoCartao.setFocusPainted(false);		
	botaoCartao.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			 if (VerificarCartao.verificarCartao(idUsuariologado) == false) {
				    JOptionPane.showMessageDialog(null,
                    "Registre um Cartão de Crédito para prosseguir para a Compra !",
                    "Cartão",
                     JOptionPane.WARNING_MESSAGE);
				VerificarCartao.RegistraCartao(idUsuariologado);
				 pagamento.dispose();
			 } else
				new TelaCartao().TelaCartao(idjogos, preco, nome,idUsuariologado);
				pagamento.dispose();
		}
	});
	
	JButton botaoBoleto = new JButton("Boleto");
	botaoBoleto.setBounds(120, 230, 150, 30);
	botaoBoleto.setFocusPainted(false);
	botaoBoleto.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			TelaBoleto.main(null);
		}
	});
	JButton botaoCancelar = new JButton("Cancelar");
	botaoCancelar.setBounds(120, 280, 150, 30);
	botaoCancelar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			pagamento.setVisible(false);;
		}
	});

	
	pagamento.add(tituloPagamento);
	pagamento.add(botaoCartao);
	pagamento.add(botaoPix);
	pagamento.add(botaoBoleto);
	pagamento.add(botaoCancelar);


	
	pagamento.setLocationRelativeTo(null);
	pagamento.setLayout(new BorderLayout());
	pagamento.setBounds(600, 200, 400, 400);
	pagamento.setVisible(true);
	
	

	}
}


