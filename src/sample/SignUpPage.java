package sample;

/**
 * Created by yunusbora on 12.09.2016.
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SignUpPage implements Initializable {
    @FXML
    public TextField wallet_signupPage_name_tf;
    public TextField wallet_signupPage_sirname_tf;
    public DatePicker wallet_signupPage_birthday_datePicker;
    public TextField wallet_signupPage_job_tf;
    public TextField wallet_signupPage_income_tf;
    public ColorPicker wallet_signupPage_colour_colourPicker;
    public Button wallet_signupPage_register_Button;
    public TextField wallet_signupPage_email_tf;

    private DatabaseConnection databaseConnection = new DatabaseConnection();

    public SignUpPage() throws Exception {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wallet_signupPage_birthday_datePicker.setValue(LocalDate.of(1950, 01, 01));
        wallet_signupPage_register_Button.setOnAction(e -> createUser(
                wallet_signupPage_name_tf.getText(),
                wallet_signupPage_sirname_tf.getText(),
                wallet_signupPage_birthday_datePicker.getValue(),
                wallet_signupPage_job_tf.getText(),
                Double.valueOf(wallet_signupPage_income_tf.getText()),
                wallet_signupPage_colour_colourPicker.getValue().toString(),
                wallet_signupPage_email_tf.getText()
        ));
    }
    //fuck you all

    private void createUser(String name,
                            String sirname,
                            LocalDate localDate,
                            String job,
                            Double income,
                            String customColors,
                            String email) {
        checkUsersTable();
        String Query = "INSERT INTO wallet.users(userName, userSirname, userBirthDate, userJob, userIncome, userFavouriteColor, userEmail) " +
                "VALUES (" + "'" +
                name + "'" + ", " + "'" +
                sirname + "'" + ", " + "'" +
                localDate.toString() + "'" + ", " + "'" +
                job + "'" + ", " +
                income + ", " + "'" +
                customColors + "'" + ", " + "'" +
                email + "'" +
                ")";

        wallet_signupPage_name_tf.clear();
        wallet_signupPage_sirname_tf.clear();
        wallet_signupPage_job_tf.clear();
        wallet_signupPage_email_tf.clear();
        wallet_signupPage_income_tf.clear();

        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(Query);
            preparedStatement.execute();
            System.out.println("User Created!");
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkUsersTable() {

        try {
            String coulmnLabel = "users";
            try (ResultSet rs = databaseConnection.getConnection().getMetaData().getTables(null, null, coulmnLabel, null)) {
                while (rs.next()) {
                    String tName = rs.getString("TABLE_NAME");
                    if (tName != null && tName.equals("users")) {
                        return;
                    }
                }
            }
            createUserTable();
            System.out.println("Users Table Checked and ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUserTable() {
        String creatingUsersTableQuery = "CREATE TABLE wallet.users(" +
                "userID int not null AUTO_INCREMENT PRIMARY KEY, " +
                "userName VARCHAR(20) NOT NULL, " +
                "userSirname VARCHAR(25) NOT NULL, " +
                "userBirthDate DATE, " +
                "userJob VARCHAR(40), " +
                "userIncome FLOAT, " +
                "userFavouriteColor VARCHAR(15), " +
                "userEmail VARCHAR(45) not null);";

        try {
            PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(creatingUsersTableQuery);
            preparedStatement.execute();
            preparedStatement.close();
            System.out.println("User Table Created and ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
