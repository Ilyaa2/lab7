package classesOfCollection;

import java.io.Serializable;

public class Coordinates implements Serializable, Comparable<Coordinates> {
    private double x; //Максимальное значение поля: 398
    private Float y; //Поле не может быть null
    private static final long serialVersionUID = 12435235L;


    public Coordinates(double x, Float y){
        this.x = x;
        this.y=y;
    }
    //public Coordinates(){}

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }


    @Override
    public int hashCode() {
        return getY().hashCode()+Double.valueOf(getX()).hashCode();
    }


    @Override
    public String toString() {
        return "Coordinates x,y: "+x+", "+y;
    }

    @Override
    public int compareTo(Coordinates o) {
        if (0 < Double.valueOf(this.getX()).compareTo(o.getX())) {
            return 1;
        } else if (0 > Double.valueOf(this.getX()).compareTo(o.getX())) {
            return -1;
        } else return getY().compareTo(o.getY());
    }
}