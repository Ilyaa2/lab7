package utils;

import classesOfCollection.*;
import exceptions.*;

import java.time.*;
import java.util.InputMismatchException;
import java.util.Scanner;


public class InteractiveCollector implements Collectable {
    private Scanner scan;

    public InteractiveCollector() {
        scan = new Scanner(System.in);
    }

    @Override
    public boolean identify() {
        return true;
    }

    @Override
    public Person collectPerson(boolean b) {
        System.out.println("Enter one field of Object per line");
        String s = null;
        String name = null;
        LocalDateTime birthday = null;
        Integer height = null;
        String passportID = "";
        Color hairColor = null;
        boolean flag = false;

        System.out.println("Enter name");
        while (!flag) {
            try {
                if (name == null || name.equals("")) {
                    name = scan.nextLine();
                }
                if (name.equals("") && b) {
                    throw new NullArgumentException();
                }

                if (name.equals("") && !b) {
                    return null;
                }
                if (birthday == null) birthday = collectDateTime();
                height = collectHeight(height, s);
                passportID = collectPassportID(passportID);
                hairColor = collectColorHair(hairColor, s);
                flag = true;
            } catch (NullArgumentException nae) {
                System.out.println("Parameter can't be null");
            } catch (InputMismatchException | NumberFormatException ime) {
                System.out.println("Parameter is incorrect");
            } catch (IllegalArgumentException iae) {
                System.out.println("Parameter must match with data above");
            } catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                return null;
            }
        }
        return new Person(name, birthday, height, passportID, hairColor);
    }

    private Color collectColorHair(Color hairColor, String s) throws Exception {
        while (hairColor == null) {
            System.out.println("Enter hairColor, it must match with data below");
            for (Color c : Color.values()) {
                System.out.print(c + " ");
            }
            System.out.print("\n");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            hairColor = Color.valueOf(s);
        }
        return hairColor;
    }

    private Integer collectHeight(Integer height, String s) throws Exception {
        while (height == null) {
            System.out.println("Enter height");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            height = Integer.parseInt(s);
            if (height <= 0) {
                height = null;
                throw new IncorrectValueException("height must be greater than 0");
            }
        }
        return height;
    }

    private String collectPassportID(String passportID) throws Exception {
        while ("".equals(passportID)) {
            System.out.println("Enter passportID");
            passportID = scan.nextLine();
            if (passportID.equals("")) throw new NullArgumentException();
        }
        return passportID;
    }

    @Override
    public StudyGroup collectStudyGroup() {
        String s = null;
        System.out.println("Enter one field of Object per line");
        String name = "";
        Coordinates coordinates = null;
        Long studentsCount = null;
        Integer shouldBeExpelled = null;
        FormOfEducation formOfEducation = null;
        Semester semesterEnum = null;
        coordinates = null;
        boolean flag = false;
        while (!flag) {
            try {
                name = collectName(name);
                if (coordinates == null) coordinates = collectCoordinates();
                studentsCount = collectStudentsCount(studentsCount, s);
                shouldBeExpelled = collectShouldBeExpelled(shouldBeExpelled, s);
                formOfEducation = collectFormOfEducation(formOfEducation, s);
                semesterEnum = collectSemester(semesterEnum,s);
                flag = true;
            } catch (IncorrectValueException ive) {
                System.out.println(ive.getMessage());
            } catch (NullArgumentException nae) {
                System.out.println("Parameter can't be null");
            } catch (InputMismatchException | NumberFormatException ime) {
                System.out.println("Parameter is incorrect");
            } catch (IllegalArgumentException iae) {
                System.out.println("Parameter must match with data above");
            } catch (Exception e) {
                return null;
            }
        }
        return new StudyGroup(name, coordinates, studentsCount, shouldBeExpelled,
                formOfEducation, semesterEnum, collectPerson(false));
    }

    private String collectName(String name) throws Exception {
        while ("".equals(name)) {
            System.out.println("Enter name");
            name = scan.nextLine();
            if (name.equals("")) throw new NullArgumentException();
        }
        return name;
    }

    private Semester collectSemester(Semester semesterEnum, String s) throws Exception {
        while (semesterEnum == null) {
            System.out.println("Enter semesterEnum, it must match with data below");
            for (Semester sem : Semester.values()) {
                System.out.print(sem + " ");
            }
            System.out.print("\n");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            semesterEnum = Semester.valueOf(s);
        }
        return semesterEnum;
    }

    private FormOfEducation collectFormOfEducation(FormOfEducation formOfEducation, String s) throws Exception {
        while (formOfEducation == null) {
            System.out.println("Enter formOfEducation, it must match with data below");
            for (FormOfEducation foe : FormOfEducation.values()) {
                System.out.print(foe + " ");
            }
            System.out.print("\n");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            formOfEducation = FormOfEducation.valueOf(s);
        }
        return formOfEducation;
    }

    private Long collectStudentsCount(Long studentsCount, String s) throws Exception {
        while (studentsCount == null) {
            System.out.println("Enter studentsCount");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            studentsCount = Long.parseLong(s);
            if (studentsCount <= 0) {
                studentsCount = null;
                throw new IncorrectValueException("students count must be greater than 0");
            }
        }
        return studentsCount;
    }

    private Integer collectShouldBeExpelled(Integer shouldBeExpelled, String s) throws Exception {
        while (shouldBeExpelled == null) {
            System.out.println("Enter shouldBeExpelled");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            shouldBeExpelled = Integer.parseInt(s);
            if (shouldBeExpelled <= 0) {
                shouldBeExpelled = null;
                throw new IncorrectValueException("this parameter must be greater than 0");
            }
        }
        return shouldBeExpelled;
    }

    @Override
    public Coordinates collectCoordinates() {
        System.out.println("Enter one field of Object per line");
        double x = 0.0;
        Float y = null;
        String s = null;
        boolean flag = false;
        while (!flag) {
            try {
                x = collectX(x, s);
                y = collectY(y, s);
                flag = true;
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Parameter is incorrect");
            } catch (NullArgumentException nae) {
                System.out.println("Parameter can't be null");
            } catch (MaxValueException mve) {
                System.out.println("Parameter can't be more than 398");
            } catch (Exception e) {
                System.out.println("Something happened wrong, please try again");
            }
        }
        return new Coordinates(x, y);
    }

    private double collectX(double x, String s) throws Exception {
        while (x == 0.0) {
            System.out.println("Enter x-coordinate of type double");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            x = Double.parseDouble(s);
            if (x > 398) {
                x = 0.0;
                throw new MaxValueException();
            }
        }
        return x;
    }

    private Float collectY(Float y, String s) throws Exception {
        while (y == null) {
            System.out.println("Enter y-coordinate of type double");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            y = Float.parseFloat(s);
        }
        return y;
    }


    @Override
    public LocalDateTime collectDateTime() {
        System.out.println("Enter one field of Object per line");
        LocalDateTime randDate;
        String s = null;
        Integer year = null;
        Month month = null;
        Short day = null;
        Short hour = null;
        Short minute = null;
        Short second = null;
        randDate = null;
        boolean flag = false;
        while (!flag) {
            try {
                year = collectYear(year, s);
                month = collectMonth(month, s);
                day = collectDay(day, s);
                hour = collectHour(hour, s);
                minute = collectMinute(minute, s);
                second = collectSecond(second, s);
                randDate = LocalDateTime.of(year, month, day, hour, minute, second);
                flag = true;
            } catch (NullArgumentException | NullPointerException nae) {
                System.out.println("Parameter can't be null");
            } catch (InputMismatchException | NumberFormatException ime) {
                System.out.println("Parameter is incorrect");
            } catch (IncorrectValueException e) {
                System.out.println(e.getMessage());
            } catch (DateTimeException dte) {
                System.out.println("Something happened wrong, you probably entered incorrect parameter somewhere, please try again");
            } catch (Exception e) {
            }
        }
        return randDate;
    }

    private Integer collectYear(Integer year, String s) throws Exception {
        while (year == null) {
            System.out.println("Enter year");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            year = Integer.parseInt(s);
            if (year <= 0 || year >= 2022) {
                year = null;
                throw new IncorrectValueException("year must be less than or equal to 2022 and greater than 0");
            }
        }
        return year;
    }

    private Month collectMonth(Month month, String s) throws Exception {
        while (month == null) {
            System.out.println("Enter month, it must match with data below");
            for (Month mon : Month.values()) {
                System.out.print(mon + " ");
            }
            System.out.print("\n");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            month = Month.valueOf(s);
        }
        return month;
    }

    private Short collectDay(Short day, String s) throws Exception {
        while (day == null) {
            System.out.println("Enter day");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            day = Short.parseShort(s);
            if (day <= 0 || day >= 32) {
                day = null;
                throw new IncorrectValueException("Parameter must be in range [1;31]");
            }
        }
        return day;
    }

    private Short collectHour(Short hour, String s) throws Exception {
        while (hour == null) {
            System.out.println("Enter hour");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            hour = Short.parseShort(s);
            if (hour < 0 || hour >= 24) {
                hour = null;
                throw new IncorrectValueException("Parameter must be in range [0;23]");
            }
        }
        return hour;
    }

    private Short collectMinute(Short minute, String s) throws Exception {
        while (minute == null) {
            System.out.println("Enter minute");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            minute = Short.parseShort(s);
            if (minute < 0 || minute >= 60) {
                minute = null;
                throw new IncorrectValueException("Parameter must be in range [0;59]");
            }
        }
        return minute;
    }

    private Short collectSecond(Short second, String s) throws Exception {
        while (second == null) {
            System.out.println("Enter second");
            s = scan.nextLine();
            if (s.equals("")) throw new NullArgumentException();
            second = Short.parseShort(s);
            if (second < 0 || second >= 60) {
                second = null;
                throw new IncorrectValueException("Parameter must be in range [0;59]");
            }
        }
        return second;
    }

}


