package memoranda.util.training;

import java.awt.Point;
import java.io.*;
import java.util.*;

/**
 * @Author Ryan Dinaro
 * This class represents a trainer belonging to the gym.
 * It stores the list of students training with them and what times are available
 */
public class Trainer extends Member {
    private static final HashMap<Integer, ArrayList<Student>> trainerData = new HashMap<Integer, ArrayList<Student>>();

    private final ArrayList<TimeSlot> availableTimes;
    private static final int MINUTES_IN_DAYS = 1440;
    private int minimumSessionTime; //how short a session can be booked for

    static {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        BufferedReader bufferedReader = null;
        File trainerFile = new File("logs/trainerDatabase");
        try {
            fileInputStream = new FileInputStream(trainerFile);
            objectInputStream = new ObjectInputStream(fileInputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(objectInputStream));

        } catch (FileNotFoundException e) {
            //File not found
            try {
                //Create a file and return no need to populate list
                if(!trainerFile.createNewFile()) {
                    throw new FileNotFoundException("Unable to connect to file system");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String line = "";

        try {
            while((line = bufferedReader.readLine())!=null) {
                //Read Trainer ID
                if(!trainerData.containsKey(Integer.parseInt(line))) {
                    trainerData.put(Integer.parseInt(line), new ArrayList<Student>());
                    trainerData.get(Integer.parseInt(line)).add((Student) objectInputStream.readObject());

                }
                else {
                    trainerData.get(Integer.parseInt(line)).add((Student) objectInputStream.readObject());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            Objects.requireNonNull(fileInputStream).close();
            Objects.requireNonNull(objectInputStream).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Trainer(Member currentMember, int minimumSessionTime) {
        super(currentMember.getFirstName(),currentMember.getLastName(),currentMember.getJoinDate(),
                currentMember.getMemberID(), currentMember.getActiveMembership());
        trainerData.put(getMemberID(),new ArrayList<Student>());
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
        return trainerData.get(getMemberID());
    }

    public boolean addStudent(Student student) {
        if(student==null)
            return false;
        return trainerData.get(getMemberID()).add(student);
    }
    public boolean removeStudent(Student student) {
        if(student==null)
            return false;
        return trainerData.get(getMemberID()).remove(student);
    }

    private static void populateList() {

    }

    public int getMinimumSessionTime() {
        return minimumSessionTime;
    }

    public void setMinimumSessionTime(int minutes) {
        this.minimumSessionTime = minutes;
    }

    public HashMap<Point, Boolean> getTimesFree() {
        HashMap<Point, Boolean> isAvailable = new HashMap<Point, Boolean>();

        for(TimeSlot slot : availableTimes) {
            int x = slot.getDay().getDayID();
            double minutesAvailable = slot.getHourEnd()* 60 + slot.getMinuteEnd() +
                    slot.getHourStart()*60 + slot.getMinuteStart();
            int numberOfSlots = (int) Math.floor(minutesAvailable / ((double) minimumSessionTime) );
            int startSlot = (int) ((MINUTES_IN_DAYS - slot.getHourStart()*60 + slot.getMinuteStart()) / (double) minimumSessionTime);
            for(int y = 0; y<numberOfSlots; y++) {
                isAvailable.put(new Point(x, startSlot+y), true);
            }
        }

        for(Student student : getStudentList()) {
            assert student != null;
            int x = student.getTimeSlot().getDay().getDayID();
            double minutesAvailable = student.getTimeSlot().getHourEnd()* 60 + student.getTimeSlot().getMinuteEnd() +
                    student.getTimeSlot().getHourStart()*60 + student.getTimeSlot().getMinuteStart();
            int numberOfSlots = (int) Math.ceil(minutesAvailable / ((double) minimumSessionTime) );
            int startSlot = (int) ((MINUTES_IN_DAYS - student.getTimeSlot().getHourStart()*60 + student.getTimeSlot().getMinuteStart()) / (double) minimumSessionTime);
            for(int y = 0; y<numberOfSlots; y++) {
                isAvailable.put(new Point(x, startSlot+y), false);
            }

        }
        return isAvailable;
    }
    public boolean timeSlotAvailable(TimeSlot trainingTime) {
        HashMap<Point, Boolean> availableTimes = getTimesFree();
        double minutesRequested = trainingTime.getHourEnd()* 60 + trainingTime.getMinuteEnd() +
                trainingTime.getHourStart()*60 + trainingTime.getMinuteStart();
        int numberOfSlots = (int) Math.ceil(minutesRequested / ((double) minimumSessionTime) );
        int startSlot = (int) ((MINUTES_IN_DAYS - trainingTime.getHourStart()*60 + trainingTime.getMinuteStart()) / (double) minimumSessionTime);
        for(int y = 0; y<numberOfSlots; y++) {
            boolean available = (availableTimes.get(new Point(trainingTime.getDay().getDayID(), startSlot + y)));
            if(!available) {
                return false;
            }
        }
        return true;
    }
}
