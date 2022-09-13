package classesOfCollection;


import java.io.Serializable;
import java.time.LocalDateTime;

import static java.lang.Math.abs;

public class Person implements Comparable<Person>, Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private java.time.LocalDateTime birthday; //Поле не может быть null
    private Integer height; //Поле не может быть null, Значение поля должно быть больше 0
    private String passportID; //Поле не может быть null
    private Color hairColor; //Поле не может быть null
    private static final long serialVersionUID = 843954134098L;

    public Person(String name, java.time.LocalDateTime dateTime, Integer height, String passportID, Color hairColor) {
        this.name = name;
        birthday = dateTime;
        this.height = height;
        this.passportID = passportID;
        this.hairColor = hairColor;
    }

    private Person() {
    }

    public static Person generateEmptyPerson(){
        return new Person();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }


    @Override
    public boolean equals(Object obj) {
        if (this.compareTo((Person) obj) == 0) {
            return true;
        }
        return false;
    }

    public int compareTo(Person o) {
        /*Comparator.comparing(Person::getBirthday).reversed()
                .thenComparing(Person::getName)
                .thenComparing(Person::getHeight)
                .thenComparing(Person::getPassportID)
                .thenComparing(Person::getHairColor)
                .compare(this, o);*/
        if (o==null) return 1;
        if (0 < this.getBirthday().compareTo(o.getBirthday())) {
            return -1;
        } else {
            if (0 > this.getBirthday().compareTo(o.getBirthday())) {
                return 1;
            } else {
                if (0 < this.getName().compareTo(o.getName())) {
                    return 1;
                } else {
                    if (0 > this.getName().compareTo(o.getName())) {
                        return -1;
                    } else {
                        if (0 < this.getHeight().compareTo(o.getHeight())) {
                            return 1;
                        } else {
                            if (0 > this.getHeight().compareTo(o.getHeight())) {
                                return -1;
                            } else {
                                if (0 > this.getPassportID().compareTo(o.getPassportID())) {
                                    return 1;
                                } else {
                                    if (0 < this.getPassportID().compareTo(o.getPassportID())) {
                                        return -1;
                                    } else {
                                        return this.getHairColor().compareTo(o.getHairColor());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int hashCode() {
        int result;
        result = name.hashCode();
        result = (29 * result + birthday.hashCode()) % 2147483647;
        result = (29 * result + height.hashCode()) % 2147483647;
        result = (29 * result + passportID.hashCode()) % 2147483647;
        result = (29 * result + hairColor.toString().hashCode()) % 2147483647;
        return abs(result);
    }


    @Override
    public String toString() {
        return "Name: " + getName() + ",\n date and time: " + getBirthday() + ",\n height: " + getHeight() +
                ",\n passport ID: " + getPassportID() + ",\n hair color: " + getHairColor() +"\n";
    }


}
