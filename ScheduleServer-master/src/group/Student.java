package group;

import java.util.ArrayList;

public class Student {
    String name;
    Integer number;
    ArrayList<Exam> exams;
    Float mean;
    Boolean confirmed;

    public Student(String name, Integer number, ArrayList<Exam> exams, Float mean, Boolean confirmed) {
        this.name = name;
        this.number = number;
        this.exams = exams;
        this.mean = mean;
        this.confirmed = confirmed;
    }
}
