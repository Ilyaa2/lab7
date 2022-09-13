package utils;

import classesOfCollection.*;
import exceptions.NoElemException;
import myCollection.MyCollection;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Vector;

public class Loader {
    public static MyCollection load(String s)  {
        try {

                File file = new File(s);
                if (!(file.exists() && file.canWrite() && file.canRead() && file.isFile())) throw new IOException();

            // Получение фабрики, чтобы после получить билдер документов.
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Получили из фабрики билдер, который парсит XML, создает структуру Document в виде иерархического дерева.
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Запарсили XML, создав структуру Document. Теперь у нас есть доступ ко всем элементам, каким нам нужно.
            Document document = builder.parse(s);
            //nodelist - состоит из двух стади груп
            NodeList nodeList = document.getElementsByTagName("StudyGroup");
            if (nodeList.getLength()==0) throw new NoElemException();
            Vector<StudyGroup> studyList = new Vector<StudyGroup>();

            String time = document.getElementsByTagName("timeOfCreation").item(0).getChildNodes().item(0).getNodeValue();
            LocalDateTime timeOfCreation = LocalDateTime.parse(time);

            for(int i=0;i<nodeList.getLength();i++){
                //x - один из StudyGroup
                Node x = nodeList.item(i);
                studyList.add(getStudyGroup(x));
            }

            MyCollection myCollection = new MyCollection(studyList,timeOfCreation);
            return myCollection;
        } catch (FileNotFoundException e) {
            System.out.println("File not found, please try again");
        } catch (NoElemException e) {
            System.out.println("There is no elements in the collection");
        } catch (SAXException | ParserConfigurationException | IOException e){
            System.out.println("either insufficient rights to the file or file not found");
        }
        return null;
    }

    // создаем из узла документа объект StudyGroup
    //на вход метода приходит нода StudyGroup
    //на выход собранный объект StudyGroup
    private static StudyGroup getStudyGroup(Node node) {
        StudyGroup studyGroup = StudyGroup.generateEmptyStGroup();
        //Redundant
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            //Redundant
            Element element = (Element) node;
            //простой сеттер, чтоб присвоить name - строку
            studyGroup.setId(Integer.parseInt(getTagValue("id", element)));
            studyGroup.setName(getTagValue("name", element));
            Node x = ((Element) node).getElementsByTagName("coordinates").item(0);
            NamedNodeMap attributes = x.getAttributes();
            // Добавление Координат. Атрибут - тоже Node, потому нам нужно получить значение атрибута с помощью метода getNodeValue()
            studyGroup.setCoordinates(new Coordinates(Double.parseDouble(attributes.getNamedItem("x").getNodeValue()), Float.parseFloat(attributes.getNamedItem("y").getNodeValue())));
            //studyGroup.setAge(Integer.parseInt(getTagValue("age", element)));
            studyGroup.setCreationDate(java.time.LocalDateTime.parse(getTagValue("creationDate", element)));
            studyGroup.setStudentsCount(Long.parseLong(getTagValue("studentsCount", element)));
            studyGroup.setShouldBeExpelled(Integer.parseInt(getTagValue("shouldBeExpelled", element)));
            studyGroup.setFormOfEducation(FormOfEducation.valueOf(getTagValue("formOfEducation", element)));
            studyGroup.setSemesterEnum(Semester.valueOf(getTagValue("semesterEnum", element)));
            studyGroup.setGroupAdmin(getPerson(node));
        }

        return studyGroup;
    }

    private static Person getPerson(Node node){
        Node x = ((Element) node).getElementsByTagName("groupAdmin").item(0);
        Person person = Person.generateEmptyPerson();
        Element element = (Element) x;
        try {
            person.setName(getTagValue("name", element));
            person.setBirthday(java.time.LocalDateTime.parse(getTagValue("birthday", element)));
            person.setHeight(Integer.parseInt(getTagValue("height", element)));
            person.setPassportID(getTagValue("passportID", element));
            person.setHairColor(Color.valueOf(getTagValue("hairColor", element)));
        } catch(NullPointerException e){
            person = null;
        }
        return person;
    }

    // получаем значение элемента по указанному тегу
    //Element в методе- StudyGroup
    private static String getTagValue(String tag, Element element) {
        //Element - StudyGroup, id, coordinates и т д.
        //tag - строковое представление необходимого подэлемента элемента StudyGroup
        //.item(0) - сделано потому что компилятор ругается, ведь element.getElementsByTagName(tag) возвращает лист элементов, даже если он один, а так мы делаем одну ноду, от которой можно вызывать детей.
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}


