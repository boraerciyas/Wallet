package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public Stage theStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        Scene scene = new Scene(root, 600,400);
//        primaryStage.setScene(scene);

        theStage = primaryStage;
        LoginPageController loginPageController = new LoginPageController();
        primaryStage.setScene(loginPageController.getDesign(primaryStage));
        primaryStage.setTitle("Wallet Calculator");
    primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
