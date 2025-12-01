package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.fazecast.jSerialComm.SerialPort;

public class TelaController {

    @FXML private Button btnLigar;
    @FXML private Button btnDesligar;
    @FXML private Button btnConectar;

    private SerialPort porta;

    @FXML
    private void conectarArduino() {
        porta = SerialPort.getCommPort("COM6"); 
        porta.setBaudRate(9600);

        if (porta.openPort()) {
            System.out.println("Arduino conectado!");
        } else {
            System.out.println("Erro ao conectar.");
        }
    }
   
    @FXML
    private Button btnDesconectar;

    @FXML
    private void desconectarArduino() {
        if (porta != null && porta.isOpen()) {
            porta.closePort();
            System.out.println("Arduino desconectado.");
        } else {
            System.out.println("Nenhuma conexão ativa.");
        }
    }


    @FXML
    private void ligarArduino() {
        enviarComando("1");
    }

    @FXML
    private void desligarArduino() {
        enviarComando("0");
    }

    private void enviarComando(String comando) {
        if (porta != null && porta.isOpen()) {
            try {
                porta.getOutputStream().write(comando.getBytes());
                porta.getOutputStream().flush();
                System.out.println("Comando enviado: " + comando);
            } catch (Exception e) {
                System.out.println("Erro ao enviar comando: " + e.getMessage());
            }
        } else {
            System.out.println("Arduino não está conectado.");
        }
    }
}
