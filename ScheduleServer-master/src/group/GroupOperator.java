package group;

import java.util.ArrayList;
import java.util.Objects;

public class GroupOperator {
    private int id;
    private ArrayList<Group> groups = new ArrayList<>();

    public void addSubject(String groupName, Student student) {
        boolean isNewGroupNeeded = true;
        for (Group group : groups) {
            if (Objects.equals(group.name, groupName)) {
                isNewGroupNeeded = false;
                group.students.add(student);
                break;
            }
        }
        if (isNewGroupNeeded) {
            ArrayList<Student> tempArrayList = new ArrayList<>();
            tempArrayList.add(student);
            groups.add(new Group(groupName, tempArrayList));
        }
    }

    public void delSubject(int groupId, int examId)
    {
        groups.get(groupId).students.remove(examId);
    }

    public void editSubject(int groupId, int examId, Student newStudent) {
        groups.get(groupId).students.set(examId, newStudent);
    }

    public ArrayList<Group> getGroups()
    {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups)
    {
        this.groups = groups;
    }
}
