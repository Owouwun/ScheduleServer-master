package group;

import java.util.ArrayList;

public class Group {
    String name;
    ArrayList<Student> students;

    public Group(String name, ArrayList<Student> students) {
        this.name = name;
        this.students = students;
    }
}
