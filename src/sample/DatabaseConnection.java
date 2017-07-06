package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static sample.DatabaseConnection.FeedEntry.*;
import static sample.DatabaseConnection.FeedOperation.*;
import static sample.DatabaseConnection.FeedType.*;
import static sample.DatabaseConnection.FeedUser.*;

/**
 * Created by yunusbora on 17.11.2016.
 */
class DatabaseConnection {
    // JDBC driver name and database URL
    static final String DATABASE_NAME = "wallet";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/";
    //  Database credentials
    private static final String USER = "root";

    private static final String PASS = "264264";


    static class FeedEntry  {


        static final String ENTRIES_TABLE_NAME = "entries";
        static final String ENTRIES_COLUMN_ID = "Eid";
        static final String ENTRIES_COLUMN_QUANTITY = "quantity";
        static final String ENTRIES_COLUMN_TIME = "time";
        static final String ENTRIES_COLUMN_OperationID = "Oid";
        static final String ENTRIES_COLUMN_TypeID = "Tid";
        static final String ENTRIES_COLUMN_UserID = "Uid";
    }
    static class FeedOperation  {

        static final String OPERATION_TABLE_NAME = "operation";
        static final String OPERATION_COLUMN_ID = "Oid";
        static final String OPERATION_COLUMN_OperationNAME = "Oname";
    }
    static class FeedType   {


        static final String TYPE_TABLE_NAME = "type";
        static final String TYPE_COLUMN_ID = "Tid";
        static final String TYPE_COLUMN_TypeNAME = "Tname";
    }
    static class FeedUser   {

        static final String USERS_TABLE_NAME = "users";
        static final String USERS_COLUMN_ID = "userID";
        static final String USERS_COLUMN_NAME= "userName";
        static final String USERS_COLUMN_SIRNAME = "userSirname";
        static final String USERS_COLUMN_PASSWORD = "userPassword" ;
        static final String USERS_COLUMN_BIRTHDATE = "userBirthDate";
        static final String USERS_COLUMN_JOB = "userJob";
        static final String USERS_COLUMN_INCOME = "userIncome";
        static final String USERS_COLUMN_FAVOURITECOLOR = "userFavouriteColor";
        static final String USERS_COLUMN_EMAIL = "userEmail";
    }

    private static final String SQL_DATABASE_CREATE = String.format("CREATE DATABASE IF NOT EXISTS `%s`;", DATABASE_NAME);

    private static final String SQL_CREATE_OPERATION = String.format("CREATE TABLE IF NOT EXISTS `%s`.`%s` (`%s` INT NOT NULL AUTO_INCREMENT, \n`%s` VARCHAR(25), \nPRIMARY KEY (%s));", DATABASE_NAME, OPERATION_TABLE_NAME, OPERATION_COLUMN_ID, OPERATION_COLUMN_OperationNAME, OPERATION_COLUMN_ID);

    private static final String SQL_CREATE_TYPE = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ").append(DATABASE_NAME).append(".").append(TYPE_TABLE_NAME).append(" (").append(TYPE_COLUMN_ID).append(" INT NOT NULL AUTO_INCREMENT, ").append(TYPE_COLUMN_TypeNAME).append(" VARCHAR(25), ").append("PRIMARY KEY (").append(TYPE_COLUMN_ID).append("));").toString();

    private static final String SQL_CREATE_USERS = String.format("CREATE TABLE IF NOT EXISTS `%s`.`%s` (\n" +
            "`%s` INT NOT NULL AUTO_INCREMENT, \n" +
            "`%s` VARCHAR(20) NOT NULL, \n" +
            "`%s` VARCHAR(20) NOT NULL, \n" +
            "`%s` CHAR(32) NOT NULL,\n" +
            " %s DATE, \n" +
            "`%s` VARCHAR(40), \n" +
            "`%s` FLOAT, %s VARCHAR(25), \n" +
            "`%s` VARCHAR(45), \n" +
            "PRIMARY KEY(%s));", DATABASE_NAME, USERS_TABLE_NAME, USERS_COLUMN_ID, USERS_COLUMN_NAME, USERS_COLUMN_SIRNAME, USERS_COLUMN_PASSWORD, USERS_COLUMN_BIRTHDATE, USERS_COLUMN_JOB, USERS_COLUMN_INCOME, USERS_COLUMN_FAVOURITECOLOR, USERS_COLUMN_EMAIL, USERS_COLUMN_ID);

    private static final String SQL_CREATE_ENTRIES = String.format("CREATE TABLE IF NOT EXISTS %s.%s (\n" +
            "%s INT NOT NULL AUTO_INCREMENT, \n" +
            "%s DOUBLE NULL, \n" +
            "%s DATETIME NULL DEFAULT CURRENT_TIMESTAMP, \n" +
            "%s INT(11) NULL, \n" +
            "%s INT(11) NULL, \n" +
            "%s INT(11) NULL, \n" +
            "PRIMARY KEY (%s), \n" +
            "FOREIGN KEY (%s) REFERENCES %s.%s (%s), \n" +
            "FOREIGN KEY (%s) REFERENCES %s.%s (%s), \n" +
            "FOREIGN KEY (%s) REFERENCES %s.%s (%s));",
            DATABASE_NAME, ENTRIES_TABLE_NAME, ENTRIES_COLUMN_ID,
            ENTRIES_COLUMN_QUANTITY, ENTRIES_COLUMN_TIME, ENTRIES_COLUMN_TypeID,
            ENTRIES_COLUMN_OperationID, ENTRIES_COLUMN_UserID, ENTRIES_COLUMN_ID,
            ENTRIES_COLUMN_TypeID, DATABASE_NAME, TYPE_TABLE_NAME, TYPE_COLUMN_ID,
            ENTRIES_COLUMN_OperationID, DATABASE_NAME, OPERATION_TABLE_NAME, OPERATION_COLUMN_ID,
            ENTRIES_COLUMN_UserID, DATABASE_NAME, USERS_TABLE_NAME, USERS_COLUMN_ID);

    DatabaseConnection() throws Exception {
        connectionControl();
        createDatabase();
    }

    Connection getConnection()   throws Exception    {
        try {
        Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(URL, USER, PASS);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    void connectionControl() {
        try {
            if (getConnection() == null) {
                System.out.println("Database can not be found. It is currently being Created...");
            }
            else
                System.out.println("Status: DATABASE CONNECTED!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createDatabase() {
        String url = "jdbc:mysql://localhost/";
        Connection conn;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(url, USER, PASS);

            PreparedStatement preparedStatement = conn.prepareStatement(SQL_DATABASE_CREATE);
            preparedStatement.executeUpdate();

            preparedStatement = conn.prepareStatement(SQL_CREATE_USERS);
            preparedStatement.executeUpdate();


            preparedStatement = conn.prepareStatement(SQL_CREATE_OPERATION);
            preparedStatement.executeUpdate();


            preparedStatement = conn.prepareStatement(SQL_CREATE_TYPE);
            preparedStatement.executeUpdate();


            preparedStatement = conn.prepareStatement(SQL_CREATE_ENTRIES);
            preparedStatement.executeUpdate();

            conn.close();
            preparedStatement.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
