import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ConfirmaCompra {
    private static final String URL = "jdbc:mysql://localhost:3306/Projeto";
    private static final String USUARIO = "root";
    private static final String SENHA = "admin";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public void confirmarCompra(int idjogos, String nomeJogo, double preco, int idUsuariologado, String cvvDigitado,JFrame TelaCartao) {
        if (verificarJogoNaBiblioteca(idUsuariologado, idjogos)) {
            return; 
        }

        int confirmacao = JOptionPane.showConfirmDialog(null,
            "Confirme os dados da compra:\n\n" +
            "Jogo: " + nomeJogo + "\n" +
            "Preço: R$ " + String.format("%.2f", preco) + "\n\n" +
            "Deseja prosseguir com a compra?",
            "Confirmar Compra",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirmacao != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Compra cancelada.");
            return;
        }

        validarCVV(idUsuariologado, cvvDigitado,  idjogos,  preco,  nomeJogo, TelaCartao); 
    }

    private static boolean verificarJogoNaBiblioteca(int idUsuario, int idjogos) {
        String sql = "SELECT COUNT(*) FROM pedido WHERE id_usuario = ? AND id_jogos = ?";
        try (Connection conexao = getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idjogos);

            try (ResultSet resultado = stmt.executeQuery()) {
                if (resultado.next() && resultado.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(null,
                        "Você já possui este jogo em sua biblioteca!",
                        "Jogo já adquirido",
                        JOptionPane.WARNING_MESSAGE);
                    return true;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao verificar biblioteca: " + e.getMessage(),
                "Erro no Sistema",
                JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private static void validarCVV(int idUsuario, String cvvDigitado, int idjogos, double preco, String nomeJogo, JFrame TelaCartao) {
        if (cvvDigitado == null || cvvDigitado.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Operação cancelada.");
            return;
        }

        String sql = "SELECT 1 FROM usuario WHERE ID_Usuario = ? AND cvv = ? LIMIT 1";
        try (Connection conexao = getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setString(2, cvvDigitado.trim());

            try (ResultSet resultado = stmt.executeQuery()) {
                if (resultado.next()) {
                    JOptionPane.showMessageDialog(null,
                        "CVV válido, prosseguindo com a compra.",
                        "Validação CVV",
                        JOptionPane.INFORMATION_MESSAGE);
                         registrarCompra(idUsuario, idjogos, preco, nomeJogo, TelaCartao);
                } else {
                    JOptionPane.showMessageDialog(null,
                        "❌ CVV incorreto. Compra não confirmada.",
                        "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE);
                        return;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao validar CVV: " + e.getMessage(),
                "Erro no Sistema",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void registrarCompra(int idUsuario, int idjogos, double preco, String nomeJogo, JFrame TelaCartao) {
        String sql = "INSERT INTO pedido (id_usuario, id_jogos, data_compra,id_pagamento) VALUES (?, ?, NOW(),?)";
        try (Connection conexao = getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idjogos);
            stmt.setDouble(3, 4);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(null,
                    "✅ Compra registrada com sucesso!\n\n" +
                    "Jogo: " + nomeJogo + "\n" +
                    "Valor: R$ " + String.format("%.2f", preco),
                    "Compra Realizada",
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
                String sql2 = "INSERT INTO biblioteca (id_usuario, id_jogos) VALUES (?, ?)";
        try (Connection conexao = getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql2)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idjogos);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                JOptionPane.showMessageDialog(null,
                    "✅ Compra adcionada a biblioteca!",
                    "Compra Realizada",
                    JOptionPane.INFORMATION_MESSAGE);
                    TelaCartao.dispose();
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
