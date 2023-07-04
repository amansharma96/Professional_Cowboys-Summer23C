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
    private static final String FILE_PATH = "logs/trainerDatabase";
    private final TreeSet<TrainingTimeSlot> availableTimes;

    private static final int MINUTES_IN_DAYS = 1440;
    private final int MINUTES_IN_HOUR = 60;
    private int minimumSessionTime; //how short a session can be booked for
    private final ArrayList<Student> studentList;
    private static final List<Trainer> trainerList;

    static {
        trainerList = FileUtilities.populateList(FILE_PATH, Trainer.class);
    }

    /**
     * Constructs a trainer object, needs an already created memberID
     * and member object
     * @param memberID the member who is declared a trainer
     * @param minimumSessionTime the minimum amount of time a session needs to be scheduled for
     */
    public Trainer(int memberID, int minimumSessionTime) {
        super(memberID);
        studentList = new ArrayList<Student>();
        if(!trainerList.contains(this))
            trainerList.add(this);
        this.minimumSessionTime = minimumSessionTime;
        availableTimes = new TreeSet<>(new TimeSlotComparator());
    }

    /**
     * Adds a time slot a trainer is able to train in
     * @param availableTime the timeslot available
     */
    public boolean addAvailableTime(TrainingTimeSlot availableTime) {
        final int startHourAndMinute = 0;
        TrainingTimeSlot fullDay = new TrainingTimeSlot(this, null, Day.UNDEFINED,
                0, 0, MINUTES_IN_DAYS);
        if(checkValidTimeSlot(availableTime,fullDay) && !hasOverlap(availableTime)) {
            this.availableTimes.add(availableTime);
            FileUtilities.saveList(FILE_PATH, trainerList);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Puts a student in a valid time slot
     * @param trainingSlot the requested time slot to fill
     * @param student the student who wants to fill the slot
     * @return true if added to the schedule
     */
    public boolean fillTrainingSlot(TrainingTimeSlot trainingSlot, Student student) {
        studentList.add(student);
        TrainingTimeSlot trainerTime = null;
        for(TrainingTimeSlot time : availableTimes) {
            if(checkValidTimeSlot(trainingSlot,time)) {
                trainerTime = time;
                break;
            }
        }
        if(trainerTime==null) {
            return false;
        }

        availableTimes.remove(trainerTime);

        //Separate the before TrainingTimeSlot, it is still available
        final double durationBetweenStarts = trainingSlot.getStartDoubleView()
                - trainerTime.getStartDoubleView();
        TrainingTimeSlot beforeTime = new TrainingTimeSlot(trainerTime.getMember(),trainerTime.getTrainer(),
                trainerTime.getDay(),
                trainerTime.getHour(),trainerTime.getMinute(),(int) durationBetweenStarts);
        addAvailableTime(beforeTime);

        //Separate the student TrainingTimeSlot, it is unavailable
        student.setTrainingTimeSlot(trainingSlot,this);
        FileUtilities.saveList(FILE_PATH, trainerList);
        FileUtilities.saveList(Student.getFilePath(), Student.getStudentList());

        //Separate the after TrainingTimeSlot, it is still available
        final double newAfterDuration = trainerTime.getDurationInMinutes()-durationBetweenStarts
                - trainingSlot.getDurationInMinutes();
        TrainingTimeSlot afterTime = new TrainingTimeSlot(trainerTime.getMember(),trainerTime.getTrainer(),
                trainerTime.getDay(),
                trainingSlot.getEndHour(), trainingSlot.getEndMinute(),(int) newAfterDuration);
        addAvailableTime(afterTime);

        return true;
    }


    /**
     * Checks if a time slot is within the bounds
     * @param trainingTimeSlot the timeslot to check
     * @param bounds the time slot that represents the bounds
     * @return true if is a valid inquiry
     */
    private boolean checkValidTimeSlot(TrainingTimeSlot trainingTimeSlot, TrainingTimeSlot bounds) {
        final double durationAvailable = bounds.getEndHour()+
                ((double) bounds.getEndMinute() / MINUTES_IN_HOUR) -
                bounds.getHour() + ((double) bounds.getMinute() / MINUTES_IN_HOUR);
        final double dayStartTime = bounds.getHour() +
                ((double) bounds.getMinute() / MINUTES_IN_HOUR);

        final boolean bellowMinimumDuration =
                trainingTimeSlot.getDurationInMinutes()<minimumSessionTime;
        final boolean schedulePastBounds =
                (trainingTimeSlot.getEndHour() +
                        ((double) trainingTimeSlot.getEndMinute() / MINUTES_IN_HOUR)) > durationAvailable;


        final boolean scheduleBeforeBounds =
                (trainingTimeSlot.getHour() +
                        ((double) trainingTimeSlot.getMinute() / MINUTES_IN_HOUR)) < dayStartTime;

        return !bellowMinimumDuration
                && !schedulePastBounds && !scheduleBeforeBounds;
    }

    /**
     * Checks to see if a time slot intersects with any established slots
     * @param newSlot the slot to check
     * @return true if intersects
     */
    private boolean hasOverlap(TrainingTimeSlot newSlot) {
        double newStart = newSlot.getStartDoubleView();
        double newEnd = newSlot.getEndDoubleView();
        for (TrainingTimeSlot existingSlot : availableTimes) {
            double existingStart = existingSlot.getStartDoubleView();
            double existingEnd = existingSlot.getEndDoubleView();

            boolean newStartsWithinExisting = newStart >= existingStart && newStart < existingEnd;
            boolean newEndsWithinExisting = newEnd > existingStart && newEnd <= existingEnd;
            boolean existingStartsWithinNew = existingStart >= newStart && existingStart < newEnd;
            boolean existingEndsWithinNew = existingEnd > newStart && existingEnd <= newEnd;
            if (newStartsWithinExisting || newEndsWithinExisting ||
                    existingStartsWithinNew || existingEndsWithinNew) {
                return true;
            }
        }

        return false;
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
    public static List<Trainer> getTrainerList() {
        return trainerList;
    }

    /**
     * Retrieves a list of Students who are training with the trainer
     * @return list of Students who are training with the trainer
     */
    public List<Student> getStudentList() {
        return studentList;
    }

    /**
     * Returns the minimum session time
     * @return the minimum session time
     */
    public int getMinimumSessionTime() {
        return minimumSessionTime;
    }

    /**
     * Adds a student to the studentList
     * @param student the student to add
     */
    protected void addStudent(Student student) {
        studentList.add(student);
    }

    /**
     * Modify the minimum session time
     * @param durationInMinutes the duration in minutes
     */
    public void setMinimumSessionTime(int durationInMinutes) {
        this.minimumSessionTime = durationInMinutes;
        FileUtilities.saveList(FILE_PATH, trainerList);
    }

    /**
     * Removes a student from a trainer
     * @param student the student to be removed
     * @return true if the student was removed
     */
    public boolean removeStudent(Student student) {
        if(student==null||!studentList.contains(student)) {
            return false;
        } else {
            studentList.remove(student);
            rejoinTimeSlot(student.getTrainingTimeSlot());
            student.setTrainingTimeSlot(null,null);
            FileUtilities.saveList(FILE_PATH, trainerList);
            FileUtilities.saveList(Student.getFilePath(), Student.getStudentList());
        }
        return true;
    }

    /**
     * Rejoins adjacent time slots in availableTimes List
     * that are less than the minimumSessionTime distance apart into
     * one TrainingTimeSlot. Checks if slots are in the same day,
     * then checks that the distance between the
     * time is less than the minimum session time.
     * @param trainingSlot the time slot to check for valid constraints
     */
    private void rejoinTimeSlot(TrainingTimeSlot trainingSlot) {
        TrainingTimeSlot lower = availableTimes.lower(trainingSlot);
        TrainingTimeSlot higher = availableTimes.higher(trainingSlot);

        boolean canMergeWithLower = lower != null
                && lower.getDay() == trainingSlot.getDay()
                && (trainingSlot.getHour() - lower.getEndHour()) * MINUTES_IN_HOUR
                + trainingSlot.getMinute() - lower.getEndMinute() < minimumSessionTime;

        boolean canMergeWithHigher = higher != null
                && higher.getDay() == trainingSlot.getDay()
                && (higher.getHour() - trainingSlot.getEndHour())
                * MINUTES_IN_HOUR + higher.getMinute() - trainingSlot.getEndMinute()
                < minimumSessionTime;

        if (canMergeWithLower && canMergeWithHigher) {
            // remove lower and higher slots, then merge all into one
            availableTimes.remove(lower);
            availableTimes.remove(higher);
            TrainingTimeSlot mergedSlot = new TrainingTimeSlot(
                    trainingSlot.getMember(),
                    trainingSlot.getTrainer(),
                    trainingSlot.getDay(),
                    lower.getHour(),
                    lower.getMinute(),
                    higher.getEndHour() * MINUTES_IN_HOUR
                            + higher.getEndMinute() - lower.getHour() * MINUTES_IN_HOUR
                            - lower.getMinute());
            availableTimes.add(mergedSlot);
        } else if (canMergeWithLower) {
            // remove lower slot, then merge with trainingSlot
            availableTimes.remove(lower);
            TrainingTimeSlot mergedSlot = new TrainingTimeSlot(
                    trainingSlot.getMember(),
                    trainingSlot.getTrainer(),
                    trainingSlot.getDay(),
                    lower.getHour(),
                    lower.getMinute(),
                    trainingSlot.getEndHour() * MINUTES_IN_HOUR
                            + trainingSlot.getEndMinute() - lower.getHour() * MINUTES_IN_HOUR
                            - lower.getMinute());
            availableTimes.add(mergedSlot);
        } else if (canMergeWithHigher) {
            // remove higher slot, then merge with trainingSlot
            availableTimes.remove(higher);
            TrainingTimeSlot mergedSlot = new TrainingTimeSlot(
                    trainingSlot.getMember(),
                    trainingSlot.getTrainer(),
                    trainingSlot.getDay(),
                    trainingSlot.getHour(),
                    trainingSlot.getMinute(),
                    higher.getEndHour() * MINUTES_IN_HOUR + higher.getEndMinute()
                            - trainingSlot.getHour() * MINUTES_IN_HOUR - trainingSlot.getMinute());
            availableTimes.add(mergedSlot);
        } else {
            // add trainingSlot as is
            availableTimes.add(trainingSlot);
        }
    }

    /**
     * Return the TreeSet representing available times for the trainer to train
     * @return TreeSet representing available times for the trainer to train
     */
    public Set<TrainingTimeSlot> getAvailableTimes() {
        return availableTimes;
    }
}
