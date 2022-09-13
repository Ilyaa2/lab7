package utils;

import classesOfCollection.*;
import exceptions.*;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;

public interface Collectable {
    Person collectPerson(boolean b) throws IOException, NullArgumentException, IncorrectValueException, UnderZeroValueException,
    DateTimeException, IllegalArgumentException,InputMismatchException;

    StudyGroup collectStudyGroup() throws NullArgumentException, InputMismatchException, NumberFormatException,
            IOException, UnderZeroValueException, MaxValueException, IncorrectValueException;

    Coordinates collectCoordinates() throws IOException, NullArgumentException, MaxValueException,
            InputMismatchException,NumberFormatException;

    LocalDateTime collectDateTime() throws IOException, NullArgumentException, IncorrectValueException,
            NumberFormatException, InputMismatchException, IllegalArgumentException , DateTimeException;
    boolean identify();
}
