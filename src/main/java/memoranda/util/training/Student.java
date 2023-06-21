package memoranda.util.training;

/**
 * @Author Ryan Dinaro
 * This class represents a student that belonging to the gym.
 * It stores who they are training with, what time slot they are training in
 */
public class Student extends Member {
    private Trainer currentTrainer;
    private TimeSlot trainingTime;


    /**
     * Initializes a student as a member of the gym, sets the trainer to unassigned trainer
     * @param memberID initializes a member as a student
     */
    public Student(int memberID) {
        super(Member.lookupMember(memberID));
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

}
