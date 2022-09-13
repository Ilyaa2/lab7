package dbconnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    //private final static String URL = "jdbc:postgresql://pg:5432/studs";
    //private final static String LOGIN = "s335128";
    //private final static String PASSWORD = "wGl8zacAW66ySKKR";

    //private static String LOGIN ="postgres";
    //private static String URL = "jdbc:postgresql://localhost:5432/postgres";
    //private static String PASSWORD = "Tylpa31";

    private static String LOGIN;
    private static String URL;
    private static String PASSWORD;


    public static Connection connect() {
        try {
            parseProperties();
            return DriverManager.getConnection(URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Data base is not available now or the file config.properties was incorrect");
        }
    }

    public static void makeStructure(){
        //ДОБАВИЛ IF NOT EXISTS ЧТОБ НЕ БЫЛО ОШИБКИ
        try(
        Connection connection = connect();
        PreparedStatement ps1 = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Uzer( login VARCHAR(50) NOT NULL PRIMARY KEY, hashedPass VARCHAR(200) NOT NULL, salt VARCHAR(50) NOT NULL);");
        PreparedStatement ps2 = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Person( name VARCHAR(50) NOT NULL, birthday TIMESTAMP NOT NULL, height INTEGER NOT NULL, passportID VARCHAR(50) NOT NULL PRIMARY KEY, hairColor VARCHAR(25) NOT NULL);");
        PreparedStatement ps3 = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS studygroup( id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL, x REAL CHECK (x < 398), y double precision NOT NULL, creationDate TIMESTAMP NOT NULL, studentsCount BIGINT NOT NULL CHECK (studentsCount > 0), shouldBeExpelled INTEGER NOT NULL CHECK (shouldBeExpelled > 0), formOfEducation VARCHAR(25) NOT NULL, semester VARCHAR(25) NOT NULL, personsPassportID VARCHAR(50) NOT NULL UNIQUE REFERENCES Person (passportID) ON DELETE CASCADE ON UPDATE CASCADE, uzer VARCHAR(50) NOT NULL REFERENCES uzer (login));");
        ){
            connection.setAutoCommit(false);
            ps1.executeUpdate();
            ps2.executeUpdate();
            ps3.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void parseProperties(){
        Properties property = new Properties();
        try {
            property.load(new FileInputStream("src/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DBConnection.PASSWORD = property.getProperty("PASS");
        DBConnection.LOGIN = property.getProperty("LOGIN");
        DBConnection.URL = property.getProperty("URL");
    }

}
