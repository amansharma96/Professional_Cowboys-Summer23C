package memoranda.util.training;

import memoranda.History;
import memoranda.util.file.FileUtilities;

import javax.xml.datatype.Duration;
import java.io.Serializable;
import java.util.*;

/**
 * @Author Ryan Dinaro
 * This class represents a trainer belonging to the gym.
 * It stores the list of students training with them and what times are available
 */
public class Trainer extends Member implements Serializable {
    private static final String FILE_PATH = "logs/trainerDatabase";
    private ArrayList<TimeSlot> availableTimes;
    private static final int MINUTES_IN_DAYS = 1440;
    private int minimumSessionTime; //how short a session can be booked for
    private final ArrayList<Student> studentList;
    private static final List<Trainer> trainerList;

    static {
        trainerList = FileUtilities.populateList(FILE_PATH, Trainer.class);
    }

    public Trainer(int memberID, int minimumSessionTime) {
        super(memberID);
        studentList = new ArrayList<Student>();
        if(!trainerList.contains(this))
            trainerList.add(this);
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

    public int getMinimumSessionTime() {
        return minimumSessionTime;
    }

    public void setMinimumSessionTime(int minutes) {
        this.minimumSessionTime = minutes;
    }


    public boolean timeSlotAvailable(TimeSlot startTrainingSlot, Student student) {
        final int minutesInAnHour = 60;
        final int hoursInADay = 24;
        final int finalHour = startTrainingSlot.getHour() + (int) Math.floor(
                (startTrainingSlot.getMinute() +
                        (double) startTrainingSlot.getDurationInMinutes()) /minutesInAnHour);
        final int convertSolicitStart = startTrainingSlot.getHour()
                + (startTrainingSlot.getMinute()/minutesInAnHour);
        final int convertSolicitEnd = (int) (finalHour + ((startTrainingSlot.getMinute() +
                        (double) startTrainingSlot.getDurationInMinutes()) % minutesInAnHour));

        if(startTrainingSlot.getDurationInMinutes()<this.minimumSessionTime
            || convertSolicitStart > hoursInADay) {
            return false;
        }

        for(TimeSlot time : this.availableTimes) {
            if(time.getDay()==startTrainingSlot.getDay()) {
                final int convertTimeStart = time.getHour()
                        + (time.getMinute()/minutesInAnHour);
                final int finalTimeHour = time.getHour() + (int) Math.floor((time.getMinute() +
                                (double) time.getDurationInMinutes()) /minutesInAnHour);
                final int convertTimeEnd = (int) (finalTimeHour + ((time.getMinute() +
                        (double) time.getDurationInMinutes()) % minutesInAnHour));
                if(convertSolicitStart>=convertTimeStart
                    && convertSolicitEnd<=convertTimeEnd) {
                    addTrainingTime(startTrainingSlot, time, student);
                    return true;
                }
            }
        }

        return false;
    }

    private void addTrainingTime(TimeSlot trainingSlot, TimeSlot trainerTime, Student student) {
        studentList.add(student);
        availableTimes.remove(trainerTime);
        final int minutesInAHour = 60;
        int trainingSlotStartTime = trainingSlot.getHour()*minutesInAHour
                + trainingSlot.getMinute();
        int trainerSlotStartTime = trainerTime.getHour()*minutesInAHour
                + trainerTime.getMinute();
        final int newDuration = trainingSlotStartTime - trainerSlotStartTime;
        TimeSlot beforeTime = new TimeSlot(trainerTime.getMember(),trainerTime.getDay(),
                trainerTime.getHour(),trainerTime.getMinute(),newDuration);
        availableTimes.add(beforeTime);
        final int newHourStart = (int) (trainingSlot.getHour() + Math.floor((trainingSlot.getMinute()
                        + (double) trainingSlot.getDurationInMinutes()) /minutesInAHour));
        final int newMinuteStart = (int) Math.floor((trainingSlot.getMinute()
                + (double) trainingSlot.getDurationInMinutes()) % minutesInAHour);

        final int newAfterDuration = trainerTime.getDurationInMinutes()-newDuration
                - trainingSlot.getDurationInMinutes();

        TimeSlot afterTime = new TimeSlot(trainerTime.getMember(),trainerTime.getDay(),
                newHourStart,newMinuteStart,newAfterDuration);
        availableTimes.add(afterTime);
    }

    public void saveTrainerFile() {
        FileUtilities.saveList(FILE_PATH, trainerList);
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
}
