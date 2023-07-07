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
    private Trainer currentTrainer;
    private static List<Student> studentList;

    static {
        studentList = FileUtilities.populateList(FILE_PATH, Student.class);
    }

    private TrainingTimeSlot trainingSlot;

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
     * sets the time slots student will train in
     * @param trainingSlot the time slot the student will start training
     */
    public void setTrainingTimeSlot(TrainingTimeSlot trainingSlot, Trainer trainer) {
        this.trainingSlot = trainingSlot;
        this.currentTrainer = trainer;
        if(trainer!= null) {
            trainer.addStudent(this);
        }
    }

    /**
     * Returns the training slot
     * @return the training slot
     */
    public TrainingTimeSlot getTrainingTimeSlot() {
        return trainingSlot;
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
