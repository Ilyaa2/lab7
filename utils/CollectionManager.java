package utils;

import classesOfCollection.Person;
import classesOfCollection.StudyGroup;
import dbconnection.DBConnection;
import dbconnection.DBManager;
import exceptions.*;
import myCollection.MyCollection;
import user.User;

import java.io.*;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.util.*;

public class CollectionManager implements Serializable {
    private static final long serialVersionUID = 100101010101010L;

    public CollectionManager() {
    }
    //syncronised
    public String add(MyCollection myCollection, StudyGroup studyGroup) {
        if (myCollection.length() > 150) {
            return "Sorry, there's a lot of elements here, can't add more";
        }
        //эта команда должна быть в блоке с бд, ниже
        // должна быть атомарность, т к person связан со studygroup
        // также нужно привязать конкретного пользователя к этой команде

        try (
                Connection connection = DBConnection.connect();

                PreparedStatement ps1 = connection.prepareStatement(
                        "INSERT INTO studygroup (name, x, y, creationdate, studentscount, " +
                                "shouldbeexpelled, formOfEducation,semester, personspassportid, uzer)" +
                                "VALUES(?,?,?,?,?,?,?,?,?,?);"

                );
                PreparedStatement ps2 = connection.prepareStatement(
                        "INSERT INTO person " +
                                "VALUES (?,?,?,?,?)"
                );
                PreparedStatement ps3 = connection.prepareStatement(
                        "SELECT id from studygroup where (name =?) and " +
                                "(x = ?) and (y = ?)  and (creationdate = ?)  and (studentscount = ?)" +
                                " and (shouldbeexpelled =?)  and (formOfEducation = ?)  and (semester =?)" +
                                "and (personspassportid = ?) " +
                                " and (uzer = ?)"
                );
        ) {
            connection.setAutoCommit(false);
            DBManager.makeStatementStudygroup(ps1, studyGroup);
            DBManager.makeStatementPerson(ps2, studyGroup.getGroupAdmin());
            DBManager.makeStatementStudygroup(ps3, studyGroup);
            ps2.executeUpdate();
            ps1.executeUpdate();
            ResultSet resultSet = ps3.executeQuery();
            connection.commit();
            resultSet.next();
            studyGroup.setId(resultSet.getInt("id"));
            myCollection.setNewElement(studyGroup);
        } catch (SQLException e) {
            //System.out.println("values inappropriate with db table");
            //System.out.println("Or probably the repeated passportID was entered");
            //e.printStackTrace();
            return "values inappropriate with db table\n" + "Or probably the repeated passportID was entered";
        }
        return "The element has been added:\n" + studyGroup.toString();


    }

    public String clear(MyCollection myCollection) {
        ArrayList<Integer> list = DBManager.selectAccessibleObjects();
        if (list.isEmpty()) return "You don't have elements to remove";
        for (int elem: list ) {
            removeByID(myCollection, elem);
        }
        return "Some elements in the collection were removed";
    }


    public String countLessThanGroupAdmin(MyCollection myCollection, Person groupAdmin) {
        if (myCollection.length() == 0) return "0";
        return Long.toString(myCollection.getVector().stream().filter(x -> x.getGroupAdmin() == null || x.getGroupAdmin().compareTo(groupAdmin) == -1).count());
    }


    public String filterContainsName(MyCollection myCollection, String name) {
        if (myCollection.length() == 0) return "There is not such element";
        String[] strings = myCollection.getVector().stream().filter(x -> x.getName().equals(name) || (x.getGroupAdmin() != null && x.getGroupAdmin().getName().equals(name))).map(StudyGroup::toString).toArray(String[]::new);
        String Return = "";
        for (String r : strings) {
            Return = Return + r;
        }
        if (Return.equals("")) {
            Return = "There is not such element";
        }
        return Return;
    }

    public String info(MyCollection myCollection) {
        return myCollection.getType() + " " + myCollection.getTimeOfCreation() + " " + myCollection.length();
    }

    public String removeAnyByGroupAdmin(MyCollection myCollection, Person groupAdmin) {
        //if (myCollection.length() == 0) return "There is not such element";
        Optional<StudyGroup> optional = myCollection.getVector().stream().filter(x -> ((x.getGroupAdmin() != null && x.getGroupAdmin().equals(groupAdmin)) || (x.getGroupAdmin() == null && groupAdmin == null))).findAny();
        StudyGroup studyGroup;
        if (optional.isPresent()) {
            studyGroup = optional.get();
        } else {return "There is not such element";}
        if (!DBManager.isEnoughRights(studyGroup.getId())) {
            return "Sorry, but you don't have rights to change someone else's object";
        }
        return removingFromDB(myCollection,studyGroup);
    }

    public String removeByID(MyCollection myCollection, int index) {
        if (!DBManager.isEnoughRights(index)) return "Sorry, but you don't have rights to change someone else's object";
        //if (myCollection.length() == 0) return "There is no such element";
        Optional<StudyGroup> optional = myCollection.getVector().stream().filter(x -> x.getId() == index).findFirst();
        String response;
        StudyGroup studyGroup;
        if (optional.isPresent()) {
            studyGroup = optional.get();
        } else {return "There is no such element";}
        return removingFromDB(myCollection, studyGroup);
    }

    public String removeGreater(MyCollection myCollection, StudyGroup element) {
        if (myCollection.length() == 0) return "There is no such element";
        boolean flag = false;
        String s = "This element or elements have been removed:\n";
        for (int i = 0; i < myCollection.length(); i++) {
            if (myCollection.getVector().get(i) == null) continue;
            if (1 >=myCollection.getVector().get(i).compareTo(element)) {
                if (DBManager.isEnoughRights(myCollection.getVector().get(i).getId())){
                    removeByID(myCollection,myCollection.getVector().get(i).getId());
                    myCollection.removeElementAt(i);
                    flag = true;
                    s += myCollection.getVector().get(i).toString() + "\n";
                }
            }
        }
        if (!flag) return "There are not elements to delete";
        return s;

    }

    public String removeLast(MyCollection myCollection) {
        //StudyGroup studyGroup = myCollection.getVector().elementAt(myCollection.length() - 1);
        StudyGroup studyGroup = myCollection.elementAt(myCollection.length() - 1);
        if (!DBManager.isEnoughRights(studyGroup.getId())) {
            return "Sorry, but you don't have rights to change someone else's object";
        }
        return removingFromDB(myCollection, studyGroup);
    }

    private String removingFromDB(MyCollection myCollection, StudyGroup studyGroup){
        if (myCollection.length() == 0) return "There is no such element";
        String response;
        try (
                Connection connection = DBConnection.connect();
                PreparedStatement ps1 = connection.prepareStatement(
                        "DELETE FROM person where passportid = ?;");
                PreparedStatement ps2 = connection.prepareStatement(
                        "DELETE FROM studygroup where id = ?");
        ) {
            ps1.setString(1, studyGroup.getGroupAdmin().getPassportID());
            ps2.setInt(1, studyGroup.getId());
            connection.setAutoCommit(false);
            ps1.executeUpdate();
            ps2.executeUpdate();
            connection.commit();
            myCollection.removeElement(studyGroup);
            response = "This element has been removed:\n" + studyGroup.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error in db";
        }
        return response;
    }

    public String reorder(MyCollection myCollection) {
        if (myCollection.length() == 0) return "There is nothing to reorder";
        Vector<StudyGroup> tmp = new Vector<>(myCollection.getVector().size());
        for (int i = myCollection.getVector().size() - 1; i >= 0; i--) {
            tmp.add(myCollection.getVector().get(i));
        }
        for (int i = 0; i < myCollection.getVector().size(); i++) {
            myCollection.getVector().set(i, tmp.get(i));
        }
        return this.show(myCollection);
    }


    public String show(MyCollection myCollection) {
        Collections.sort(myCollection.getVector());
        return myCollection.print();
    }

    public String updateID(MyCollection myCollection, Integer id, StudyGroup studyGroup) {
        boolean flag = false;
        String s = "";
        int index = 0;
        studyGroup.setId(id);
        for (int i = 0; i < myCollection.length(); i++) {
            if (myCollection.getVector().get(i).getId().equals(id)) {
                index = i;
                flag = true;
            }
        }
        if (!flag) return "There is not this id in collection, so nothing has changed";
        try (
                Connection connection = DBConnection.connect();
                PreparedStatement ps1 = connection.prepareStatement(
                        "UPDATE studygroup SET name =?," +
                                "x=?, y=?, creationdate=?, studentscount =?," +
                                "shouldbeexpelled =?, formofeducation=?," +
                                "semester=?, personspassportid=?" +
                                "WHERE id = ?");
                PreparedStatement ps2 = connection.prepareStatement(
                        "UPDATE person SET name=?," +
                                "birthday=?, height=?, passportid=?, haircolor=?" +
                                "WHERE passportid = ?");
        ) {
            if (!DBManager.isEnoughRights(id))
                return "Sorry, but you don't have rights to change someone else's object";
            connection.setAutoCommit(false);

            DBManager.generalStatement(ps1, studyGroup);
            ps1.setInt(10, id);
            DBManager.makeStatementPerson(ps2, studyGroup.getGroupAdmin());
            ps2.setString(6, myCollection.getVector().get(index).getGroupAdmin().getPassportID());
            ps2.executeUpdate();
            ps1.executeUpdate();
            connection.commit();
            myCollection.getVector().set(index, studyGroup);
            s = "The element has been changed, this is the new object:\n" + myCollection.getVector().get(index).toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error in database, probably there is no such id in the collection or repeated values";
        }
        return s;
    }

    public String executeScript(String path, ArrayList<Command> commands, User user) {
        Command command = null;
        String s="";
        String EnteredCommand = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            File file = new File(path);
            if (!(Files.exists(file.toPath()) && file.canRead() && file.isFile())) throw new IOException();
            Switch sw = new Switch(new FileCollector(reader), this);

            while ((EnteredCommand = reader.readLine()) != null) {
                command = sw.interpret(EnteredCommand);
                command.setUser(user);
                commands.add(command);
            }
            s = "Script was validated successfully";
        } catch (FileNotFoundException e) {
            s = "Can not find the file";
        } catch (IOException e) {
            s = "Can not read the file";
        } catch (NumberFormatException | InputMismatchException e) {
            s = "Parameter is incorrect";
        } catch (IncorrectValueException e) {
            s = e.getMessage();
        } catch (NullArgumentException e) {
            s = "Argument cannot be null";
        } catch (UnderZeroValueException e) {
            s = "Parameter cannot be under zero";
        } catch (IllegalArgumentException e) {
            s = "Parameter must match with allowed values in enum";
        } catch (DateTimeException e) {
            s = "Something happened wrong, you probably entered incorrect parameter in date or time";
        } catch (MaxValueException e) {
            s = "Too large number";
        } catch (AttemptOfRecursionException e) {
            s = "Recursive execution of the script is not allowed";
        } catch (UnknownCommandException e) {
            s = "\""+EnteredCommand+"\" is unknown command, so that's why script wasn't completed fully";
        } catch (Exception e) {
            s = "Too many elements or commands, cannot execute";
        }

        return s;
    }

    public String help() {
        return "help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение своего элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : удалить все свои объекты\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (с сохранением в файл)\n" +
                "remove_last : удалить последний элемент из коллекции\n" +
                "remove_greater {element} : удалить из коллекции все свои элементы, превышающие заданный\n" +
                "reorder : отсортировать коллекцию в порядке, обратном нынешнему\n" +
                "remove_any_by_group_admin groupAdmin : удалить из коллекции один элемент, который принадлежит Вам, значение поля groupAdmin которого эквивалентно заданному\n" +
                "count_less_than_group_admin groupAdmin : вывести количество элементов, значение поля groupAdmin которых меньше заданного\n" +
                "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку";
    }


    public String exit(MyCollection myCollection) {
        return "The collection has been saved and you were disconnected from server";
    }


}
