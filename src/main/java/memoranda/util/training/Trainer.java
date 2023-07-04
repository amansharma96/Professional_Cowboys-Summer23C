package memoranda.util.training;

import memoranda.util.file.FileUtilities;

import java.io.Serializable;
import java.util.*;

/**
 * @Author Ryan Dinaro
 * This class represents a trainer belonging to the gym.
 * It stores the list of students training with them and what times are available
 */
public class Trainer extends Member implements Serializable {
    private final ArrayList<TimeSlot> availableTimes;
    private static final int MINUTES_IN_DAYS = 1440;
    private int minimumSessionTime; //how short a session can be booked for
    private final ArrayList<Student> studentList;
    private static final List<Trainer> trainerList;

    static {
        trainerList = FileUtilities.populateList("logs/trainerDatabase", Trainer.class);
    }

    public Trainer(int memberID, int minimumSessionTime) {
        super(memberID);
        studentList = new ArrayList<Student>();
        if(!trainerList.contains(this))
            trainerList.add(this);
        availableTimes = new ArrayList<TimeSlot>();
        this.minimumSessionTime = minimumSessionTime;
    }

    /**
     * @param availableTime the timeslot available
     */
    public void addAvailableTime(TimeSlot availableTime) {
        this.availableTimes.add(availableTime);
    }

    public ArrayList<Student> getStudentList() {
        return studentList;
    }

    public boolean addStudent(Student student) {
        if(student==null)
            return false;
        return studentList.add(student);
    }
    public boolean removeStudent(Student student) {
        if(student==null)
            return false;
        return studentList.remove(student);
    }

    public int getMinimumSessionTime() {
        return minimumSessionTime;
    }

    public void setMinimumSessionTime(int minutes) {
        this.minimumSessionTime = minutes;
    }


    public boolean timeSlotAvailable(TimeSlot startTrainingSlot,TimeSlot endTrainingSlot) {

        return true;
    }
    public void saveTrainer() {
        FileUtilities.saveList("logs/trainerDatabase", trainerList);
    }
}
