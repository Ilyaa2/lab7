package utils;

import classesOfCollection.*;
import exceptions.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.InputMismatchException;



public class FileCollector implements Collectable {
    private BufferedReader reader;

    public FileCollector(BufferedReader reader) throws FileNotFoundException {
        this.reader = reader;
    }

    @Override
    public boolean identify() {
        return false;
    }


    @Override
    public Person collectPerson(boolean b) throws IOException, NullArgumentException, InputMismatchException,
            IllegalArgumentException, IncorrectValueException, DateTimeException, UnderZeroValueException {

        String s;
        String name = "";
        LocalDateTime birthday;
        Integer height = null;
        String passportID = "";
        Color hairColor = null;

        name = reader.readLine();
        if (name.equals("") && b) throw new NullArgumentException();
        if (name.equals("") && !b) return null;
        birthday = collectDateTime();

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        height = Integer.parseInt(s);
        if (height <= 0) throw new UnderZeroValueException();

        passportID = reader.readLine();
        if (passportID.equals("")) throw new NullArgumentException();

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        hairColor = Color.valueOf(s);

        return new Person(name, birthday, height, passportID, hairColor);
    }

    @Override
    public StudyGroup collectStudyGroup() throws NullArgumentException, InputMismatchException,
            NumberFormatException, IOException, UnderZeroValueException, MaxValueException, IncorrectValueException,IllegalArgumentException, DateTimeException {
        String s;
        String name;
        Coordinates coordinates;
        Long studentsCount;
        Integer shouldBeExpelled ;
        FormOfEducation formOfEducation ;
        Semester semesterEnum ;

        name = reader.readLine();
        if (name.equals("")) throw new NullArgumentException();

        coordinates = collectCoordinates();

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        studentsCount = Long.parseLong(s);
        if (studentsCount <= 0) throw new UnderZeroValueException();

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        shouldBeExpelled = Integer.parseInt(s);
        if (studentsCount <= 0) throw new UnderZeroValueException();

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        formOfEducation = FormOfEducation.valueOf(s);

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        semesterEnum = Semester.valueOf(s);

        return new StudyGroup(name, coordinates, studentsCount, shouldBeExpelled,
                formOfEducation, semesterEnum, collectPerson(false));
    }


    @Override
    public Coordinates collectCoordinates() throws IOException, NullArgumentException, MaxValueException,InputMismatchException,NumberFormatException {
        double x = 0.0;
        Float y = null;

        String s= reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        x = Double.parseDouble(s);
        if (x > 398) throw new MaxValueException();

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        y = Float.parseFloat(s);

        return new Coordinates(x, y);
    }

    @Override
    public LocalDateTime collectDateTime() throws IOException, NullArgumentException, NumberFormatException, InputMismatchException, IllegalArgumentException, IncorrectValueException, DateTimeException {

        String s;
        Integer year = null;
        Month month = null;
        Byte day = null;
        Byte hour = null;
        Byte minute = null;
        Short second = null;

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        year = Integer.parseInt(s);
        if (year <= 0 || year >= 2022) throw new IncorrectValueException("year must be less than or equal to 2022 and greater than 0");

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        month = Month.valueOf(s);

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        day = Byte.parseByte(s);
        if (day <= 0 || day >= 32) throw new IncorrectValueException("Parameter must be in range [1;31]");

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        hour = Byte.parseByte(s);
        if (hour < 0 || hour >= 24) throw new IncorrectValueException("Parameter must be in range [0;23]");

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        minute = Byte.parseByte(s);
        if (minute < 0 || minute >= 60) throw new IncorrectValueException("Parameter must be in range [0;59]");

        s = reader.readLine();
        if (s.equals("")) throw new NullArgumentException();
        second = Short.parseShort(s);
        if (second < 0 || second >= 60) throw new IncorrectValueException("Parameter must be in range [0;59]");

        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

}





