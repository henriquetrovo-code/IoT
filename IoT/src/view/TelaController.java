package view;

import com.fazecast.jSerialComm.SerialPort;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TelaController {

    private SerialPort porta;
    private InputStream entrada;
    private OutputStream saida;

    @FXML private Button btnAcionar;
    @FXML private Button btnDesativar;
    @FXML private Button btnConectar;
    @FXML private Label lblStatus;

    public boolean conectar() {
        porta = SerialPort.getCommPort("COM7"); // ajuste conforme necessário
        porta.setBaudRate(9600);
        porta.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);

        if (porta.openPort()) {
            System.out.println("Porta aberta com sucesso!");
            lblStatus.setText("Arduino conectado em " + getDataHoraAtual());
            saida = porta.getOutputStream();
            entrada = porta.getInputStream();

            // sincroniza ao conectar
            atualizaDataHora("conectar");

            return true;
        } else {
            deconectar();
            System.out.println("Falha ao abrir porta.");
            lblStatus.setText("Falha ao abrir porta em " + getDataHoraAtual());
            return false;
        }
    }

    @FXML
    public void initialize() {
        btnAcionar.setOnAction(e -> {
            if (porta != null && porta.isOpen()) {
                enviarComando("ligar " + getDataHoraAtual() + "\n");
                lblStatus.setText("LED Ligado em " + getDataHoraAtual());
            } else {
                lblStatus.setText("Não é possível ligar: Arduino desconectado");
            }
        });

        btnDesativar.setOnAction(e -> {
            if (porta != null && porta.isOpen()) {
                enviarComando("desligar " + getDataHoraAtual() + "\n");
                lblStatus.setText("LED Desligado em " + getDataHoraAtual());
            } else {
                lblStatus.setText("Não é possível desligar: Arduino desconectado");
            }
        });

        btnConectar.setOnAction(e -> {
            if (porta == null || !porta.isOpen()) {
                if (conectar()) {
                    btnConectar.setText("Desconectar");
                    new Thread(this::receberDados).start();
                }
            } else {
                // sincroniza ao desconectar
                enviarComando("desconectar " + getDataHoraAtual() + "\n");

                deconectar();
                btnConectar.setText("Conectar");
                lblStatus.setText("Arduino desconectado em " + getDataHoraAtual());
            }
        });
    }

    private void enviarComando(String comando) {
        try {
            if (saida != null) {
                saida.write(comando.getBytes());
                saida.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void receberDados() {
        StringBuilder acumulador = new StringBuilder();
        try {
            byte[] buffer = new byte[1024];
            while (porta != null && porta.isOpen()) {
                if (entrada.available() > 0) {
                    int len = entrada.read(buffer);
                    if (len > 0) {
                        String recebido = new String(buffer, 0, len);
                        acumulador.append(recebido);

                        int index;
                        while ((index = acumulador.indexOf("\n")) != -1) {
                            String mensagem = acumulador.substring(0, index).trim();
                            acumulador.delete(0, index + 1);

                            javafx.application.Platform.runLater(() -> lblStatus.setText(mensagem));
                        }
                    }
                }
                Thread.sleep(50);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deconectar() {
        try {
            if (entrada != null) entrada.close();
            if (saida != null) saida.close();
            if (porta != null && porta.isOpen()) porta.closePort();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizaDataHora(String acao) {
        if (porta == null || !porta.isOpen()) {
            lblStatus.setText("Arduino desconectado em " + getDataHoraAtual());
        } else {
            String comando = acao + " " + getDataHoraAtual() + "\n";
            enviarComando(comando);
        }
    }

    private String getDataHoraAtual() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return agora.format(fmt);
    }
}
