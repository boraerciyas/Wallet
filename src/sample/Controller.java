package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Controller implements Initializable {

    private static final String WRITE_TYPE_SQL = "INSERT INTO wallet.type(Tname) VALUES (?)";
    private static final String WRITE_ENTRIES_SQL = "INSERT INTO wallet.entries(quantity, Tid,Oid) VALUES (?,(SELECT Tid FROM type WHERE Tname= ?), ?)";


    @FXML
    public FlowPane firstFlowPane;
    public Label choose_label;
    public RadioButton outgoing_rb;
    public RadioButton incoming_rb;
    public RadioButton deferred_rb;
    public ComboBox<String> outgoingType_cb;
    public Label quantity_label;
    public TextField quantity_tf;
    public Button enter_button;
    public TextField paymentDate_tf;
    private int actionType;

    private TextInputDialog dialogPane = new TextInputDialog();
    private File typeCBContent = new File("src\\typeCBContent.txt");
    private File firstDayOfMonth = new File("src\\firstDayOfMonth.txt");
    private DatabaseConnection databaseConnection = new DatabaseConnection();


//  Yapılacaklar: ComboBOX DialogPane'e (Para Girişi-Harcama-Vadeli Harcama) Radio Buttons Ekle
//                Program Penceresine bir kontrol uyarı yazısı ekle
//                ✓ Ekle button'ına basıldığında kullanımın devamı için quantity_tf'yi clear et(Deferred için paymentDate_tf'yi de unutma)
//                Giriş için isim Soyisim alan bir açılış penceresi tasarla.Alınan isim ve bilgileri kararlaştır ve nerede kullanılacaklarını düşün. Alınan kişiye her seferinde sorulmamasını sağla
//                Sağ Click ve MenuBar'ı hallet, alt kısma grafik eklemeyi öğren
//                Bir commend_tf oluştur ve onu da db içinde sakla

    public Controller() throws Exception {
    }

    private void actionDefault() {
        outgoingType_cb.setVisible(true);
        quantity_label.setVisible(true);
        quantity_tf.setVisible(true);
        enter_button.setVisible(true);
        outgoingType_cb.setVisibleRowCount(3);
        paymentDate_tf.setVisible(false);
    }

    private void incomingAction() {
        actionDefault();
        actionType = 1;
        outgoingType_cb.setPromptText("Giriş Tipini Belirtiniz");
    }

    private void outgoingAction() {
        actionDefault();
        actionType = 2;
        outgoingType_cb.setPromptText("Lütfen harcama türünüzü seçiniz");
    }

    private void deferredAction() {
        actionDefault();
        actionType = 3;
        outgoingType_cb.setPromptText("Lütfen harcama türünüzü seçiniz");
        paymentDate_tf.setVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*********
         Database Connection
         */

        databaseConnection.connectionControl();

        /********
         Design Part
         */

        final ToggleGroup rb_group = new ToggleGroup();
        outgoing_rb.setToggleGroup(rb_group);
        incoming_rb.setToggleGroup(rb_group);
        deferred_rb.setToggleGroup(rb_group);
        VBox typeVbox = new VBox(10);
        typeVbox.getChildren().addAll(choose_label, outgoing_rb, incoming_rb, deferred_rb);
        VBox processVbox = new VBox(10);
        processVbox.getChildren().addAll(outgoingType_cb, quantity_label, quantity_tf, paymentDate_tf, enter_button);
        HBox firstPartHbox = new HBox(10);
        firstPartHbox.getChildren().addAll(typeVbox, processVbox);
        firstFlowPane.getChildren().add(firstPartHbox);
        firstPartHbox.setAlignment(Pos.BASELINE_CENTER);

        outgoingType_cb.getItems().add(0, "Enter a New Type...");

        /******************
         *
         * Radio Buttons Listener
         *
         ******************/

        rb_group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                    if (rb_group.getSelectedToggle() == null) {
                        outgoingType_cb.setVisible(false);
                        quantity_label.setVisible(false);
                        quantity_tf.setVisible(false);

                    } else if (rb_group.getSelectedToggle() == incoming_rb) {
                        incomingAction();
                    } else if (rb_group.getSelectedToggle() == outgoing_rb) {
                        outgoingAction();
                    } else if (rb_group.getSelectedToggle() == deferred_rb) {
                        deferredAction();
                    }
                }
        );
        /***************
         *   Put the ComboBox values
         ***************/
        try {
            outgoingType_cb.getItems().addAll(readTypesfromDataBase());
            writeTypeCBtoFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*************
         *   Setting the ComboBox's action
         *************/

        outgoingType_cb.setOnAction(event -> {

            if (Objects.equals(outgoingType_cb.getValue(), outgoingType_cb.getItems().get(0))) {
                Optional<String> a = dialogPaneCreator();
                if (a.isPresent()) {
                    outgoingType_cb.getItems().add(a.get());
                    outgoingType_cb.setValue(outgoingType_cb.itemsProperty().getValue().get(outgoingType_cb.itemsProperty().getValue().size() - 1));
                    try {
                        writeTypeCBtoFile();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    try {
                        PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(WRITE_TYPE_SQL);
                        preparedStatement.setString(1, a.get());
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /********
         *Setting the Enter Button Action
         ********/

        enter_button.setOnAction(event -> {
            try {
                enterButtonActions();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    /*************
     *
     *DialogPane has been Created!
     *
     **********/

    private Optional<String> dialogPaneCreator() {
        dialogPane.setContentText("Harcama/Girdi tipi : ");
        dialogPane.setTitle("Yeni Harcama/Girdi Tipi");
        dialogPane.setHeaderText("Lütfen harcama/Girdi tipinizi giriniz!");
        return dialogPane.showAndWait();
    }

    /***************
     *
     * Writing 'Types' into a txt file
     *
     ****************/

    private void writeTypeCBtoFile() throws FileNotFoundException {
        String comboBoxItems = outgoingType_cb.itemsProperty().getValue().toString();
        if (!typeCBContent.exists()) {
            throw new FileNotFoundException("File does not exist" + typeCBContent);
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(typeCBContent));
            bw.write(comboBoxItems.replace('[', '\0').replace(']', '\0').replace("\\s+", "").trim());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*******
     *
     * Reading types from Databese
     *
     ********/

    private String[] readTypesfromDataBase() throws Exception {
        final String query = "SELECT Tname FROM " + DatabaseConnection.DATABASE_NAME + "." + "type";

        Statement stm = databaseConnection.getConnection().createStatement();
        ResultSet resultSet = stm.executeQuery(query);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();



        List<String> typeNames = new ArrayList<>(columnCount);

        while (resultSet.next()) {
            int i = 1;
            while (i <= columnCount) typeNames.add(resultSet.getString(i++));
        }

        System.out.println(typeNames.toString());

        resultSet.close();
        stm.close();

        return typeNames.toArray(new String[typeNames.size()]);
    }

    private void serializationDefault(EnterAction enterAction) {
        enterAction.date = LocalDate.now();
        enterAction.time = LocalTime.now();

        try {
            FileOutputStream fileOut = new FileOutputStream("C:\\Users\\yunusbora\\Desktop\\Wallet\\src\\enterAction.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(enterAction);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /src/enterAction.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    private void startMonth() throws IOException {
        FileWriter writeFirstDate = new FileWriter(firstDayOfMonth);
        writeFirstDate.write(LocalDate.now().toString());

//        if (LocalDate.now().plusMonths(1).toString().equals(LocalDate.now().toString())) {
//            monthly.clear();
//        }
    }

    private void enterButtonActions() throws Exception {

        PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement(WRITE_ENTRIES_SQL);
        preparedStatement.setDouble(1, Double.valueOf(quantity_tf.getText().replace(',', '.')));
        preparedStatement.setString(2, outgoingType_cb.getValue().trim());
        preparedStatement.setInt(3, actionType);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        quantity_tf.clear();
        paymentDate_tf.clear();
    }
}