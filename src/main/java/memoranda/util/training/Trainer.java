package memoranda.util.training;

import java.util.ArrayList;

/**
 * @Author Ryan Dinaro
 * This class represents a trainer belonging to the gym.
 * It stores the list of students training with them and what times are available
 */
public class Trainer extends Member {
    private ArrayList<Student> studentList;
    private TimeSlot trainingTime;

    public Trainer(String firstName, String lastName) {
        super(firstName, lastName);
        studentList = new ArrayList<Student>();
    }

    public ArrayList<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(ArrayList<Student> studentList) {
        this.studentList = studentList;
    }

    public boolean setTimeFree(TimeSlot trainingTime) {
        this.trainingTime = trainingTime;
    }
    public void setTimeUnavailable(TimeSlot trainingTime) {
        this.trainingTime = trainingTime;
    }
    public TimeSlot getTimeFree(TimeSlot trainingTime) {
        this.trainingTime = trainingTime;
    }
    public TimeSlot getTimeUnavailable(TimeSlot trainingTime) {
        this.trainingTime = trainingTime;
    }
    public TimeSlot getTimeSlot() {
        return this.trainingTime;
    }
}
