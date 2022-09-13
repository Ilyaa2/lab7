package utils;

import myCollection.MyCollection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;



public class Saver {
    private MyCollection myCollection;

    public MyCollection getMyCollection(){
        return myCollection;
    }
    public Saver(MyCollection myCollection) {
        this.myCollection=myCollection;
    }

    public void execute(String s) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        java.time.LocalDateTime time = getMyCollection().getTimeOfCreation();

        try {
            builder = factory.newDocumentBuilder();

            // создаем пустой объект Document, в котором будем создавать наш xml-файл
            Document doc = builder.newDocument();
            // создаем корневой элемент
            Element rootElement = doc.createElement("myCollection");
            // добавляем корневой элемент в объект Document
            doc.appendChild(rootElement);

            //Добавляем вектор
            Element vector = doc.createElement("vector");
            rootElement.appendChild(vector);

            //задача вначале добавить один стадигруп, а потом сделать цикл.
            for(int i=0; i<getMyCollection().length();i++) {
                vector.appendChild(getStudyGroup(doc, i));
            }

            //добавляем время создания
            //Важно чтоб элемент был не оторванным от корня - самого первого док элемента
            Element timeOfCreation = doc.createElement("timeOfCreation");
            timeOfCreation.appendChild(doc.createTextNode(time.toString()));
            rootElement.appendChild(timeOfCreation);

            // создаем объект TransformerFactory для печати в консоль
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // для красивого вывода в консоль
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            //печатаем в консоль или файл
            OutputStreamWriter ous = new OutputStreamWriter(new FileOutputStream(s));
            StreamResult file = new StreamResult(ous);

            //записываем данные
            transformer.transform(source, file);


        }
        catch (ParserConfigurationException e) {
            //System.out.println("Cannot find or read/write file");
            e.printStackTrace();
        } catch (TransformerException e){
            e.printStackTrace();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }


    // метод для создания нового узла XML-файла
    private Node getStudyGroup(Document doc, int index) {
        //Создаем подэлемент, главного элемента, и у подэлемента есть зависимые поля
        //будет столько же методов, сколько и полей у стадиГруп
        Element studyGroup = doc.createElement("StudyGroup");

        // создаем элемент id
        studyGroup.appendChild(getElementsFromParent(doc, "id",
                getMyCollection().getVector().get(index).getId().toString()));

        //создаем элемент name
        studyGroup.appendChild(getElementsFromParent(doc, "name",
                getMyCollection().getVector().get(index).getName()));
        //создаем coordinate

        studyGroup.appendChild(getStudyGroup(doc,"coordinates",
                Double.toString(getMyCollection().getVector().get(index).getCoordinates().getX()),
                Float.toString(getMyCollection().getVector().get(index).getCoordinates().getY()))
        );
        //creationDate
        studyGroup.appendChild(getElementsFromParent(doc, "creationDate",
                getMyCollection().getVector().get(index).getCreationDate().toString()));
        //studentsCount
        studyGroup.appendChild(getElementsFromParent(doc, "studentsCount",
                getMyCollection().getVector().get(index).getStudentsCount().toString()));
        //shouldBeExpelled
        studyGroup.appendChild(getElementsFromParent(doc, "shouldBeExpelled",
                getMyCollection().getVector().get(index).getShouldBeExpelled().toString()));
        //formOfEducation
        studyGroup.appendChild(getElementsFromParent(doc, "formOfEducation",
                getMyCollection().getVector().get(index).getFormOfEducation().toString()));
        //semesterEnum
        studyGroup.appendChild(getElementsFromParent(doc, "semesterEnum",
                getMyCollection().getVector().get(index).getSemesterEnum().toString()));

        studyGroup.appendChild(getGroupAdmin(doc,index));


        return studyGroup;
    }

    //Используется для координат, т к там есть атрибуты.
    private Node getStudyGroup(Document doc, String name, String value1, String value2) {
        Element node = doc.createElement(name);
        node.setAttribute("x",value1);
        node.setAttribute("y",value2);
        return node;
    }


    // утилитный метод для создание нового узла XML-файла
    private Node getElementsFromParent(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    private Node getGroupAdmin(Document doc, int index){
        Element groupAdmin = doc.createElement("groupAdmin");
        try {
            //name
            groupAdmin.appendChild(getElementsFromParent(doc, "name",
                    getMyCollection().getVector().get(index).getGroupAdmin().getName()));
            //birthday
            groupAdmin.appendChild(getElementsFromParent(doc, "birthday",
                    getMyCollection().getVector().get(index).getGroupAdmin().getBirthday().toString()));
            //height
            groupAdmin.appendChild(getElementsFromParent(doc, "height",
                    getMyCollection().getVector().get(index).getGroupAdmin().getHeight().toString()));
            //passportID
            groupAdmin.appendChild(getElementsFromParent(doc, "passportID",
                    getMyCollection().getVector().get(index).getGroupAdmin().getPassportID()));
            //hairColor
            groupAdmin.appendChild(getElementsFromParent(doc, "hairColor",
                    getMyCollection().getVector().get(index).getGroupAdmin().getHairColor().toString()));
        } catch (NullPointerException e){
            getElementsFromParent(doc,"groupAdmin","null");
        }
        return groupAdmin;
    }


}

