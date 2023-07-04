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
    private final TreeSet<TimeSlot> availableTimes;

    private static final int MINUTES_IN_DAYS = 1440;
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
    public boolean addAvailableTime(TimeSlot availableTime) {
        final int startHourAndMinute = 0;
        TimeSlot fullDay = new TimeSlot(this,null, Day.UNDEFINED,
                startHourAndMinute-1,startHourAndMinute+1,MINUTES_IN_DAYS);
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
    public boolean fillTrainingSlot(TimeSlot trainingSlot, Student student) {
        studentList.add(student);
        TimeSlot trainerTime = null;
        for(TimeSlot time : availableTimes) {
            if(checkValidTimeSlot(trainingSlot,time)) {
                trainerTime = time;
                break;
            }
        }
        if(trainerTime==null) {
            return false;
        }

        availableTimes.remove(trainerTime);

        //Separate the before TimeSlot, it is still available
        final double durationBetweenStarts = trainingSlot.getStartDoubleView()
                - trainerTime.getStartDoubleView();
        TimeSlot beforeTime = new TimeSlot(trainerTime.getMember(),trainerTime.getTrainer(),
                trainerTime.getDay(),
                trainerTime.getHour(),trainerTime.getMinute(),(int) durationBetweenStarts);
        addAvailableTime(beforeTime);

        //Separate the student TimeSlot, it is unavailable
        student.setTrainingTimeSlot(trainingSlot,this);
        student.setCurrentTrainer(this);
        FileUtilities.saveList(FILE_PATH, trainerList);
        FileUtilities.saveList(Student.getFilePath(), Student.getStudentList());

        //Separate the after TimeSlot, it is still available
        final double newAfterDuration = trainerTime.getDurationInMinutes()-durationBetweenStarts
                - trainingSlot.getDurationInMinutes();
        TimeSlot afterTime = new TimeSlot(trainerTime.getMember(),trainerTime.getTrainer(),
                trainerTime.getDay(),
                trainingSlot.getEndHour(), trainingSlot.getEndMinute(),(int) newAfterDuration);
        addAvailableTime(afterTime);

        return true;
    }


    /**
     * Checks if a time slot is within the bounds
     * @param timeSlot the timeslot to check
     * @param bounds the time slot that represents the bounds
     * @return true if is a valid inquiry
     */
    private boolean checkValidTimeSlot(TimeSlot timeSlot, TimeSlot bounds) {
        int MINUTES_IN_HOUR = 60;
        final double hoursAvailable = bounds.getEndHour()+
                ((double) bounds.getEndMinute() / MINUTES_IN_HOUR) -
                bounds.getHour() + ((double) bounds.getMinute() / MINUTES_IN_HOUR);
        final double dayStartTime = bounds.getHour() +
                ((double) bounds.getMinute() / MINUTES_IN_HOUR);

        final boolean bellowMinimumDuration =
                timeSlot.getDurationInMinutes()<minimumSessionTime;

        final boolean schedulePastBounds =
                (timeSlot.getEndHour() +
                        ((double) timeSlot.getEndMinute() / MINUTES_IN_HOUR)) > hoursAvailable;

        final boolean scheduleBeforeBounds =
                (timeSlot.getHour() +
                        ((double) timeSlot.getMinute() / MINUTES_IN_HOUR)) < dayStartTime;


        return !bellowMinimumDuration
                && !schedulePastBounds && !scheduleBeforeBounds;
    }

    /**
     * Checks to see if a time slot intersects with any established slots
     * @param newSlot the slot to check
     * @return true if intersects
     */
    private boolean hasOverlap(TimeSlot newSlot) {
        double newStart = newSlot.getStartDoubleView();
        double newEnd = newSlot.getEndDoubleView();
        for (TimeSlot existingSlot : availableTimes) {
            System.out.println(existingSlot);
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
            rejoinTimeSlot(student.getTrainingSlot());
            student.setTrainingTimeSlot(null,null);
            student.setCurrentTrainer(null);
        }
        return true;
    }

    /**
     * Rejoins adjacent time slots in availableTimes List
     * that are less than the minimumSessionTime distance apart into
     * one TimeSlot. Checks if slots are in the same day,
     * then checks that the distance between the
     * time is less than the minimum session time.
     * @param trainingSlot the time slot to check for valid constraints
     */
    private void rejoinTimeSlot(TimeSlot trainingSlot) {

    }
}
