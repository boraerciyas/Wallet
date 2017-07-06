package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by yunusbora on 6.06.2017.
 */
public class WalletMainController {


    private Image menuAltButtonImage = new Image("design/menu-alt-512.png");
    private Button menuAltButton = new Button();
    private BorderPane root = new BorderPane();



    Scene getScene(Stage stage)    {


        return new Scene(root, 800, 600);
    }
}
