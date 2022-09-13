package classesOfCollection;

import java.io.Serializable;
import java.time.LocalDateTime;

import static java.lang.Math.abs;

public class StudyGroup implements Comparable<StudyGroup>, Serializable {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long studentsCount; //Значение поля должно быть больше 0, Поле не может быть null
    private Integer shouldBeExpelled; //Значение поля должно быть больше 0, Поле не может быть null
    private FormOfEducation formOfEducation; //Поле может быть null
    private Semester semesterEnum; //Поле не может быть null
    private Person groupAdmin; //Поле может быть null

    private static final long serialVersionUID = 6575980984L;

    public StudyGroup(String name, Coordinates coordinates,
                      Long studentsCount, Integer shouldBeExpelled, FormOfEducation formOfEducation, Semester semesterEnum,
                      Person groupAdmin) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.studentsCount = studentsCount;
        this.shouldBeExpelled = shouldBeExpelled;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
        //id = generateID();
    }

    public StudyGroup(Integer id, String name, Coordinates coordinates, LocalDateTime creationDate,
                      Long studentsCount, Integer shouldBeExpelled, FormOfEducation formOfEducation, Semester semesterEnum,
                      Person groupAdmin) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.studentsCount = studentsCount;
        this.shouldBeExpelled = shouldBeExpelled;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;

    }

    public StudyGroup(String name, Coordinates coordinates, LocalDateTime creationDate,
                      Long studentsCount, Integer shouldBeExpelled, FormOfEducation formOfEducation, Semester semesterEnum,
                      Person groupAdmin) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.studentsCount = studentsCount;
        this.shouldBeExpelled = shouldBeExpelled;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
    }
    /*
    public StudyGroup(StudyGroup studyGroup) {
        this.id = studyGroup.getId();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.studentsCount = studentsCount;
        this.shouldBeExpelled = shouldBeExpelled;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
    }
    */

    private Integer generateID() {
        int result;
        result = name.hashCode();
        result = (29 * result + coordinates.hashCode()) % 2147483647;
        result = (29 * result + creationDate.hashCode()) % 2147483647;
        result = (29 * result + studentsCount.hashCode()) % 2147483647;
        result = (29 * result + shouldBeExpelled.hashCode()) % 2147483647;
        result = (29 * result + formOfEducation.toString().hashCode()) % 2147483647;
        result = (29 * result + semesterEnum.toString().hashCode()) % 2147483647;
        if (groupAdmin != null) result = (29 * result + groupAdmin.hashCode()) % 2147483647;
        return abs(result);
    }

    public StudyGroup(Integer id) {
        this.id = id;
    }

    private StudyGroup() {
    }

    public static StudyGroup generateEmptyStGroup(){
        return new StudyGroup();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(Long studentsCount) {
        this.studentsCount = studentsCount;
    }

    public Integer getShouldBeExpelled() {
        return shouldBeExpelled;
    }

    public void setShouldBeExpelled(Integer shouldBeExpelled) {
        this.shouldBeExpelled = shouldBeExpelled;
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public void setFormOfEducation(FormOfEducation formOfEducation) {
        this.formOfEducation = formOfEducation;
    }

    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    public void setSemesterEnum(Semester semesterEnum) {
        this.semesterEnum = semesterEnum;
    }

    public void setGroupAdmin(Person groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public String getName() {
        return name;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }

    @Override
    public int compareTo(StudyGroup o) {
        return this.coordinates.compareTo(o.coordinates);
    }

    @Override
    public String toString() {
        return "ID: " + id + "\n name: " + name + ",\n coordinates: " + coordinates + ",\n date and time: " + creationDate +
                ",\n number of students: " + studentsCount + ",\n shouldBeExpelled: " + shouldBeExpelled +
                ",\n form of education: " + formOfEducation + ",\n number of semester: " + semesterEnum + ",\n group admin: " + groupAdmin + "\n";
    }
}

