package memoranda.util.training;

/**
 * @Author Ryan Dinaro
 * This class represents a student that belonging to the gym.
 * It stores who they are training with, what time slot they are training in
 */
public class Student extends Member {
    private boolean ActiveMembership;
    private Trainer currentTrainer;
    private TimeSlot trainingTime;
    public static final Trainer UNASSIGNED_TRAINER = new Trainer("NOT ", "ASSIGNED");

    /**
     * Initializes a student as a member of the gym, sets the trainer to unassigned trainer
     * @param firstName Students first name
     * @param lastName Students last name
     */
    public Student(String firstName, String lastName) {
        super(firstName, lastName);
        this.currentTrainer = UNASSIGNED_TRAINER;
    }

    /**
     * retrieves the current Trainer student is training with
     * @return the current Trainer or no trainer with the name NOT ASSIGNED
     */
    public Trainer getCurrentTrainer() {
        return this.currentTrainer;
    }

    /**
     * Sets the trainer to the trainer passed in
     * @param currentTrainer the Trainer to be assigned to student
     */
    public void setCurrentTrainer(Trainer currentTrainer) {
        this.currentTrainer = currentTrainer;
    }

    /**
     * sets the time slots student will train in
     * @param trainingTime the time slot the student will be training in
     */
    public void setTimeSlot(TimeSlot trainingTime) {
        this.trainingTime = trainingTime;
    }

    /**
     * retrieves the time slots student trains in
     * @return the time slots student trains in
     */
    public TimeSlot getTimeSlot() {
        return this.trainingTime;
    }

}
