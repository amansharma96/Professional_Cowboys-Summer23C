package memoranda.util.training;

import memoranda.util.file.FileUtilities;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Ryan Dinaro
 * This class represents a student that belonging to the gym.
 * It stores who they are training with, what time slot they are training in
 */
public class Student extends Member implements Serializable {
    private static final String FILE_PATH = "logs/studentDatabase";
    private TimeSlot startTrainingSlot;
    private TimeSlot endTrainingSlot;
    private Trainer currentTrainer;
    private static List<Student> studentList;

    static {
        studentList = FileUtilities.populateList(FILE_PATH, Student.class);
    }

    /**
     * Private constructor for serialization
     */
    private Student() {
        super();
    }

    /**
     * Initializes a student as a member of the gym, sets the trainer to unassigned trainer
     * @param memberID initializes a member as a student
     */
    public Student(int memberID) {
        super(memberID);
        if(studentList==null) {
            studentList = new ArrayList<Student>();
            studentList.add(this);
        }
        if(!studentList.contains(this)) {
            studentList.add(this);
        }
    }


    /**
     * retrieves the current Trainer student is training with
     * @return the current Trainer or no trainer with the name NOT ASSIGNED
     */
    public Trainer getCurrentTrainer() {
        return this.currentTrainer;
    }

    /**
     * sets the current Trainer
     * @param trainer the current Trainer
     */
    public void setCurrentTrainer(Trainer trainer) {
        this.currentTrainer = trainer;
    }

    /**
     * sets the time slots student will train in
     * @param startTrainingSlot the time slot the student will start training
     * @param endTrainingSlot the time slot student will stop training
     * @param currentTrainer the trainer to be training with
     * @return  false - if trainer is unavailable
     *          true - if trainer is available
     */
    public boolean setTimeSlot(TimeSlot startTrainingSlot,
                               TimeSlot endTrainingSlot, Trainer currentTrainer) {
        this.currentTrainer = currentTrainer;
        if(!currentTrainer.timeSlotAvailable(startTrainingSlot,endTrainingSlot)) {
            return false;
        } else {
            this.startTrainingSlot = startTrainingSlot;
            this.endTrainingSlot = endTrainingSlot;
            FileUtilities.saveList(FILE_PATH, studentList);
            return true;

        }
    }

    public TimeSlot getStartTrainingSlot() {
        return startTrainingSlot;
    }

    public TimeSlot getEndTrainingSlot() {
        return endTrainingSlot;
    }

    /**
     * Returns the file path
     * @return the file path of saves
     */
    public static String getFilePath(){
        return FILE_PATH;
    }

    /**
     * Returns the List of members
     * @return the List of members
     */
    public static List<Student> getStudentList() {
        return studentList;
    }
}
