package dbconnection;

import classesOfCollection.Person;
import classesOfCollection.StudyGroup;
import org.postgresql.util.PSQLException;
import user.User;
import user.UserStatus;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DBManager {
    private static User user;

    public static void setUser(User us) {
        user = us;
    }

    public static UserStatus verdict() throws SQLException {

        /*
        String s="";
        switch (userStatus) {
            case WRONGPASSWORD:
                //System.out.println("Try again, wrong password");
                //s = "Try again, wrong password";
                s = userStatus.getDescription();
                break;
            case NOTREGISTERED:
                //System.out.println("You have just registered");
                s = "You have just registered";
                break;
            case AUTHORIZED:
                //System.out.println("Authorisation successfully");
                s = "Authorisation successfully";
                break;
        }

         */
        //System.out.println(userStatus.getDescription());
        return checkUser(user.getLogin(), user.getPassword(), User.PEPER);
    }

    public static UserStatus checkUser(String login, String password, String peper) throws SQLException {
        try (
                Connection connection = DBConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT hashedpass, salt from Uzer WHERE login=?"
                )
        ) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                String dbpassword = resultSet.getString("hashedpass");
                String salt = resultSet.getString("salt");
                if (getHash(peper + password + salt).equals(dbpassword)) return UserStatus.AUTHORIZED;
                return UserStatus.WRONGPASSWORD;
            }
        } catch (PSQLException e) {
            register(login, password, User.generateRandomSalt(), peper);
        }
        return UserStatus.NOTREGISTERED;
    }

    public static void register(String login, String password, String salt, String peper) throws SQLException {
        try (
                Connection connection = DBConnection.connect();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO UZER " +
                                "VALUES(?,?,?)"
                )
        ) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, getHash(peper + password + salt));
            preparedStatement.setString(3, salt);
            int answer = preparedStatement.executeUpdate();
        } catch (PSQLException e) {
            e.printStackTrace();
            //System.out.println("Database doesn't respond");
        }
    }

    public static String getHash(String input) {
        String s = "";
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-384");
            byte[] bytes = sha.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02X", b));
            }
            s = builder.toString().toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void makeStatementStudygroup(PreparedStatement ps, StudyGroup studyGroup) throws SQLException {
        generalStatement(ps, studyGroup);
        ps.setString(10, user.getLogin());
    }

    public static void generalStatement(PreparedStatement ps, StudyGroup studyGroup) throws SQLException {
        ps.setString(1, studyGroup.getName());
        ps.setDouble(2, studyGroup.getCoordinates().getX());
        ps.setFloat(3, studyGroup.getCoordinates().getY());
        LocalDateTime ldt = studyGroup.getCreationDate();
        ps.setTimestamp(4, new Timestamp(
                ldt.getYear(),
                ldt.getMonthValue(),
                ldt.getDayOfMonth(),
                ldt.getHour(),
                ldt.getMinute(),
                ldt.getSecond(),
                ldt.getNano()
        ));
        ps.setLong(5, studyGroup.getStudentsCount());
        ps.setInt(6, studyGroup.getShouldBeExpelled());
        ps.setString(7, String.valueOf(studyGroup.getFormOfEducation()));
        ps.setString(8, String.valueOf(studyGroup.getSemesterEnum()));
        ps.setString(9, studyGroup.getGroupAdmin().getPassportID());
    }

    public static void makeStatementPerson(PreparedStatement ps, Person person) throws SQLException {
        ps.setString(1, person.getName());
        LocalDateTime bd = person.getBirthday();
        ps.setTimestamp(2, new Timestamp(
                bd.getYear(),
                bd.getMonthValue(),
                bd.getDayOfMonth(),
                bd.getHour(),
                bd.getMinute(),
                bd.getSecond(),
                bd.getNano()
        ));
        ps.setInt(3, person.getHeight());
        ps.setString(4, person.getPassportID());
        ps.setString(5, String.valueOf(person.getHairColor()));
    }

    public static boolean isEnoughRights(int id) {
        boolean flag = false;
        try (
                Connection connection = DBConnection.connect();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT uzer FROM studygroup WHERE id =?"
                );
        ) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    String str = resultSet.getString("uzer");
                    if (user.getLogin().equals(str)) return true;
                    flag = true;
                }
            }
        } catch (SQLException e) {
            if (!flag) System.out.println("No one was found with this id");
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Integer> selectAccessibleObjects() {
        ArrayList<Integer> list = new ArrayList<>();
        try (
                Connection connection = DBConnection.connect();
                PreparedStatement ps = connection.prepareStatement(
                        "SELECT id FROM studygroup WHERE uzer = ?");
        ) {
            ps.setString(1,user.getLogin());
            try(ResultSet resultSet = ps.executeQuery()){
                while (resultSet.next()){
                    list.add(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}

