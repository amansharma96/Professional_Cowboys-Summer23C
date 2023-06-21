package memoranda.util.training;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * @Author Ryan Dinaro
 * This class represents a trainer belonging to the gym.
 * It stores the list of students training with them and what times are available
 */
public class Trainer extends Member {
    private final HashSet<Integer> studentList;

    private final ArrayList<TimeSlot> availableTimes;
    private static final int MINUTES_IN_DAYS = 1440;
    private int minimumSessionTime; //how short a session can be booked for


    public Trainer(Member currentMember, int minimumSessionTime) {
        super(currentMember.getFirstName(),currentMember.getLastName(),currentMember.getJoinDate(),
                currentMember.getMemberID(), currentMember.getActiveMembership());
        studentList = new HashSet<Integer>();
        availableTimes = new ArrayList<TimeSlot>();
        this.minimumSessionTime = minimumSessionTime;
    }

    /**
     * @param availableTime the timeslot available
     */
    public void setAvailableTime(TimeSlot availableTime) {
        this.availableTimes.add(availableTime);
    }
    public HashSet<Integer> getStudentList() {
        if(studentList.size()==0)
            populateList();
        return studentList;
    }

    public boolean addStudent(Student student) {
        if(student==null)
            return false;
        if(studentList.size()==0)
            populateList();

        return this.studentList.add(student.getMemberID());
    }
    public boolean removeStudent(Student student) {
        if(student==null)
            return false;
        if(studentList.size()==0)
            populateList();
        return this.studentList.remove(student.getMemberID());
    }

    private void populateList() {
        Scanner scan = new Scanner("logs/trainerDatabase");
        while(scan.hasNextLine()) {
            while (scan.hasNextInt()) {
                studentList.add(scan.nextInt());
            }
            scan.nextLine();
        }
    }

    public int getMinimumSessionTime() {
        return minimumSessionTime;
    }

    public void setMinimumSessionTime(int minutes) {
        this.minimumSessionTime = minutes;
    }

    public HashMap<Point, Boolean> getTimesFree() {
        if(studentList.size()==0)
            populateList();
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

        for(Integer studentID : studentList) {
            Student student = (Student) lookupMember(studentID);
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
