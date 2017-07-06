package sample;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;

/**
 * Created by yunusbora on 14.12.2016.
 */
public class LoginPageController {

    private VBox TFvPF_vbox = new VBox(2);
    private VBox hyperlinks_Vbox = new VBox(2);
    private CheckBox rememberMe_cb = new CheckBox();
    private TextField userName_tf = new TextField("");
    private PasswordField userPass_pf = new PasswordField();
    private Hyperlink newAccount_hl = new Hyperlink();
    private Hyperlink forgotPass_hl = new Hyperlink();
    private Button enter_btn = new Button("Enter");
    private BorderPane CBvBTN_borderPane = new BorderPane();
    private FlowPane root= new FlowPane(Orientation.VERTICAL);
    private BackgroundImage background = new BackgroundImage(new Image("sample//design//wallet_background_4.jpg", 850, 650, false,true),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
    private Image logo = new Image("sample//design//logo.png");
    private Label rememberMe_label = new Label("Remember Me: ");
    private HBox rememberMe_hbox = new HBox(2);


    Scene getDesign(Stage stage) {

        ImageView logo_imageView = new ImageView(logo);
        logo_imageView.setPreserveRatio(true);
        logo_imageView.setFitHeight(300);

        rememberMe_cb.selectedProperty().setValue(false);

        userName_tf.setPromptText("Mail Address");
        userPass_pf.setPromptText("Password");

        forgotPass_hl.setText("Forget Your Password?");
        newAccount_hl.setText("Create a new Account");

        rememberMe_hbox.getChildren().addAll(rememberMe_label, rememberMe_cb);

        CBvBTN_borderPane.setLeft(rememberMe_hbox);
        CBvBTN_borderPane.setRight(enter_btn);

        userName_tf.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER)   {
                if (userName_tf.getText().length() == 0) {
                    System.out.println("E-mail Area is empty.");

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Username area can not be empty, please enter a valid username");
                    alert.setTitle("");
                    alert.setHeaderText(null);
                    alert.showAndWait();

                }
                else {
                    try {
                        isPasswordFieldEmpty(stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        userPass_pf.setOnKeyPressed((KeyEvent event) ->    {
            if (event.getCode() == KeyCode.ENTER)   {
                if (userPass_pf.getText().length() < 6) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("You should enter at least 6 characters on Password Field!");
                    alert.setHeaderText(null);
                    alert.showAndWait();

                }

                else{
                    try {
                       enterOnAction(stage);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }


            }
        });

        enter_btn.setOnAction(e -> {
            try {
                enterOnAction(stage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        newAccount_hl.setOnAction(e -> newAccountOnAction());

        TFvPF_vbox.getChildren().addAll(userName_tf, userPass_pf);
        hyperlinks_Vbox.getChildren().addAll(newAccount_hl, forgotPass_hl);

        root.getChildren().addAll(logo_imageView, TFvPF_vbox, CBvBTN_borderPane, hyperlinks_Vbox);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(background));
        root.setHgap(20.0);
        root.setAlignment(Pos.CENTER);
        root.setVgap(15.0);

        return new Scene(root, 800, 600);
    }

    private void isPasswordFieldEmpty(Stage stage) throws Exception {

        if (userPass_pf.getText().length() < 6)    {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("You should enter at least 6 characters on Password Field!");
            alert.setHeaderText(null);
            alert.showAndWait();

            return;
        }
        enterOnAction(stage);
    }

    /***********
    *   This method will be invoked if user want to create a new account.
    *
    ***********/

    private void newAccountOnAction() {


    }

    private void enterOnAction(Stage stage) throws Exception {

        DatabaseConnection db = new DatabaseConnection();
        db.connectionControl();

        String userName = userName_tf.getText();
        String password = Base64.getEncoder().encodeToString(userPass_pf.getText().getBytes());

        System.out.println(" Encoded password = " + password);

        String SQL_USERNAME_CHECK = new StringBuilder().append("SELECT ").append(DatabaseConnection.FeedUser.USERS_TABLE_NAME).append(".").
                append(DatabaseConnection.FeedUser.USERS_COLUMN_EMAIL).append(" \n").append("FROM ").append(DatabaseConnection.DATABASE_NAME).
                append(".").append(DatabaseConnection.FeedUser.USERS_TABLE_NAME).append(" \n").append("WHERE ").
                append(DatabaseConnection.FeedUser.USERS_TABLE_NAME).append(".").append(DatabaseConnection.FeedUser.USERS_COLUMN_EMAIL).append(" = \"").append(userName).append("\"").toString();

        String SQL_PASSWORD_CHECK = new StringBuilder().append("SELECT ").append(DatabaseConnection.FeedUser.USERS_TABLE_NAME).append(".").
                append(DatabaseConnection.FeedUser.USERS_COLUMN_PASSWORD).append(" \n").append("FROM ").append(DatabaseConnection.DATABASE_NAME).
                append(".").append(DatabaseConnection.FeedUser.USERS_TABLE_NAME).append(" \n").append("WHERE ").
                append(DatabaseConnection.FeedUser.USERS_TABLE_NAME).append(".").append(DatabaseConnection.FeedUser.USERS_COLUMN_PASSWORD).append(" = \"").append(password).append("\"").toString();


        String SQL_USER_ENTER = new StringBuilder().append("SELECT ").
                append(DatabaseConnection.FeedUser.USERS_TABLE_NAME).append(".").append(DatabaseConnection.FeedUser.USERS_COLUMN_EMAIL).append(", ").
                append(DatabaseConnection.FeedUser.USERS_TABLE_NAME).append(".").append(DatabaseConnection.FeedUser.USERS_COLUMN_PASSWORD).append(" \n").
                append("FROM ").append(DatabaseConnection.DATABASE_NAME).append(".").append(DatabaseConnection.FeedUser.USERS_TABLE_NAME).append(" \n").
                append("WHERE ").append(DatabaseConnection.FeedUser.USERS_TABLE_NAME).append(".").append(DatabaseConnection.FeedUser.USERS_COLUMN_EMAIL).
                append(" = \"").append(userName).append("\" && ").append(DatabaseConnection.FeedUser.USERS_TABLE_NAME).append(".").
                append(DatabaseConnection.FeedUser.USERS_COLUMN_PASSWORD).append(" = \"").append(password).append("\" ;").toString();

        System.out.println(SQL_USERNAME_CHECK);

        Statement statement = db.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(SQL_USERNAME_CHECK);


        if (rs.next())   {
            System.out.println("Succeed on E-Mail.\n");

            System.out.println(SQL_PASSWORD_CHECK);

            Statement statement1 = db.getConnection().createStatement();
            ResultSet rs1 = statement1.executeQuery(SQL_PASSWORD_CHECK);

            if (rs1.next() && userPass_pf.getText() != "" && userPass_pf.getText() != null) {
                System.out.println("Succeed on Password.\n");
            }else {
                Alert emailNotFoundAlert = new Alert(Alert.AlertType.WARNING);
                emailNotFoundAlert.setTitle("Password is not correct");
                emailNotFoundAlert.setHeaderText(null);
                emailNotFoundAlert.setContentText("This Password not seem correct! \nPlease try again.");
                emailNotFoundAlert.showAndWait();
            }

        }else {
            Alert emailNotFoundAlert = new Alert(Alert.AlertType.WARNING);
            emailNotFoundAlert.setTitle("E-mail not Found");
            emailNotFoundAlert.setHeaderText(null);
            emailNotFoundAlert.setContentText("This e-mail address can not be found! \nPlease try again.");
            emailNotFoundAlert.showAndWait();
        }

        Statement statement2 = db.getConnection().createStatement();
        ResultSet rs2 = statement2.executeQuery(SQL_USER_ENTER);

        if (rs2.next()) {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Scene scene = new Scene(root, 600,400);
            stage.setScene(scene);
        }
    }
}
