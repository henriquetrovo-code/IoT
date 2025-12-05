package application;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EventoDAO {

    public static void registrarEvento(
            String descricao, 
            String statusLed, 
            String tipoEvento
    ) {
        String sql = "INSERT INTO eventos_arduino (descricao, status_led, tipo_evento) VALUES (?, ?, ?)";

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, descricao);
            stmt.setString(2, statusLed);
            stmt.setString(3, tipoEvento);

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}