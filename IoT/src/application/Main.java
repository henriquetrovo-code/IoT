package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carrega o arquivo FXML da pasta view
        Parent root = FXMLLoader.load(getClass().getResource("/view/tela.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("IoT - Controle Arduino");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
