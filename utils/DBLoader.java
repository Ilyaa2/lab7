package utils;

import classesOfCollection.*;
import dbconnection.DBConnection;
import myCollection.MyCollection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Vector;

public class DBLoader {
    /**
     * ДОПИЛИТЬ TIME OF CREATION
     */
    public static MyCollection loadMyCollection() {

        Vector<StudyGroup> studs = new Vector<>();
        try (
                Connection connection = DBConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * from studygroup"
                )
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Timestamp tmp = resultSet.getTimestamp("creationdate");
                    LocalDateTime lc = LocalDateTime.of(
                            tmp.getYear(),
                            tmp.getMonth() + 1,
                            tmp.getDate(),
                            tmp.getHours(),
                            tmp.getMinutes(),
                            tmp.getSeconds(),
                            tmp.getNanos()
                    );
                    studs.add(
                            new StudyGroup(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    new Coordinates(
                                            resultSet.getDouble("x"),
                                            resultSet.getFloat("y")
                                    ),
                                    lc,
                                    resultSet.getLong("studentscount"),
                                    resultSet.getInt("shouldbeexpelled"),
                                    FormOfEducation.valueOf(resultSet.getString("formOfEducation")),
                                    Semester.valueOf(resultSet.getString("semester")),
                                    loadPerson(resultSet.getString("personspassportid"), connection)
                            )
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("осуждаю");
        }
        return new MyCollection(studs);
    }

    private static Person loadPerson(String id, Connection connection) {
        Person person = null;
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * from person WHERE passportid=?"
                )
        ) {
            preparedStatement.setString(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                Timestamp tmp = resultSet.getTimestamp("birthday");
                LocalDateTime lc = LocalDateTime.of(
                        tmp.getYear(),
                        tmp.getMonth(),
                        tmp.getDate(),
                        tmp.getHours(),
                        tmp.getMinutes(),
                        tmp.getSeconds(),
                        tmp.getNanos()
                );
                person = new Person(
                        resultSet.getString("name"),
                        lc,
                        resultSet.getInt("height"),
                        resultSet.getString("passportid"),
                        Color.valueOf(resultSet.getString("haircolor"))
                );
            }
        } catch (SQLException e) {
            System.out.println("smth happened wrong with db");
        }
        return person;
    }
}


