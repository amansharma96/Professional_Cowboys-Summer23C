package memoranda.util.training;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * @Author Ryan Dinaro
 * This class represents a student that belonging to the gym.
 * It stores who they are training with, what time slot they are training in
 */
public class Student extends Member implements Serializable {
    private Trainer currentTrainer;
    private TimeSlot trainingTime;
    public static ArrayList<Integer> studentList;

    static {
        studentList = new ArrayList<Integer>();
        populateList();
    }

    /**
     * Private constructor for serialization
     */
    private Student() {
        super();
    }
    private static void populateList() {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        File studentFile = new File("logs/studentDatabase.txt");
        try {
            fileInputStream = new FileInputStream(studentFile);
            objectInputStream = new ObjectInputStream(fileInputStream);

        } catch (FileNotFoundException e) {
            //File not found
            try {
                //Create a file and return no need to populate list
                if(studentFile.createNewFile()) {
                    return;
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch(EOFException e1) {
            return; //EmptyFile
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            studentList = (ArrayList<Integer>) Objects.requireNonNull(objectInputStream).readObject();
        } catch (EOFException e) {
            //End of stream
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Initializes a student as a member of the gym, sets the trainer to unassigned trainer
     * @param memberID initializes a member as a student
     */
    public Student(int memberID) {
        super(memberID);
        if(!studentList.contains(memberID)) {
            studentList.add(memberID);
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
     * @param trainingTime the time slot the student will be training in
     * @param currentTrainer the trainer to be training with
     * @return  false - if trainer is unavailable
     *          true - if trainer is available
     */
    public boolean setTimeSlot(TimeSlot trainingTime, Trainer currentTrainer) {
        this.currentTrainer = currentTrainer;
        if(!currentTrainer.timeSlotAvailable(trainingTime)) {
            return false;
        }
        this.trainingTime = trainingTime;
        return true;
    }

    /**
     * retrieves the time slots student trains in
     * @return the time slots student trains in
     */
    public TimeSlot getTimeSlot() {
        return this.trainingTime;
    }

    /**
     * Saves the students. If not called, students will be saved
     * @return true if successful
     */
    public static boolean saveStudents() {
        try {
            FileOutputStream studentWriter = new FileOutputStream("logs/studentDatabase.txt", false);
            ObjectOutputStream obj = new ObjectOutputStream(studentWriter);

            // Write the entire studentList to the file
            obj.writeObject(studentList);

            obj.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
