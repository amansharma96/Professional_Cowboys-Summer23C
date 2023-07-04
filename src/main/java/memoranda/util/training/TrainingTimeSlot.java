package memoranda.util.training;

import java.util.Comparator;

/**
 * This represents a slot of time for a training day
 * @author Ryan Dinaro
 * @version 7/4/2023
 */
public class TrainingTimeSlot {
    private final Member member;
    private final Trainer trainer;
    private final Day day;
    private final int hour;
    private final int minute;
    private final int durationInMinutes;
    private static final int MINUTES_IN_HOUR = 60;


    /**
     * This represents a slot of time for a training day
     * @param member The member this timeslot is tied to
     * @param trainer The trainer this timeslot is tied too.
     *                Null when trainers available time
     * @param day the day this starts
     * @param hour the hour this starts
     * @param minute the minute this starts
     * @param durationInMinutes the duration in minutes of this slot
     *
     */
    public TrainingTimeSlot(Member member, Trainer trainer, Day day, int hour, int minute, int durationInMinutes) {
        this.member = member;
        this.trainer = trainer;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.durationInMinutes = durationInMinutes;
    }

    /**
     * Represents a training day
     * @return the day this occupies
     */
    public Day getDay() {
        return day;
    }

    /**
     * Represents a training day
     * @return the day this occupies
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Represents hour of start
     * @return hour of start
     */
    public int getHour() {
        return hour;
    }

    /**
     * The member occupying the time slot
     * @return member occupying the time slot
     */
    public Member getMember() {
        return member;
    }

    /**
     * The duration of Time Slot in minutes
     * @return duration of Time Slot in minutes
     */
    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    /**
     * A string representation of the Time Slot
     * @return string representation of the Time Slot
     */
    @Override
    public String toString() {
        return day.toString() + " " + hour + ":" + minute
                + " duration: " + durationInMinutes + " minutes";
    }

    /**
     * Calculates the hour this time slot ends on
     * @return the hour this time slot ends on
     */
    public int getEndHour() {
        return hour + (int) Math.floor((double) (minute + durationInMinutes) / MINUTES_IN_HOUR);
    }

    /**
     * Calculates the minute this timeslot ends on
     * @return the minute this timeslot ends on
     */
    public int getEndMinute() {
        return (minute + durationInMinutes) % MINUTES_IN_HOUR;
    }

    /**
     * Calculates the start time in a double view, hour is integer,
     * minutes are represented as the decimals,
     * with the decimal being the proportion of minutes to minutes per hour.
     * standardize the calculation of one value for comparisons
     * @return the time in a double view
     */
    public double getStartDoubleView() {
        return hour + ((double) minute /MINUTES_IN_HOUR);
    }

    /**
     * Calculates the ends time in a double view, hour is integer,
     * minutes are represented as the decimals,
     * with the decimal being the proportion of minutes to minutes per hour.
     * standardize the calculation of one value for comparisons
     * @return the time in a double view
     */
    public double getEndDoubleView() {
        return getEndHour() + ((double) getEndHour() /MINUTES_IN_HOUR);
    }

    /**
     * Returns the trainer in the time slot
     * @return the trainer in the time slot
     */
    public Trainer getTrainer() {
        return trainer;
    }
}

/**
 * Comparator used for tree structure of available times trainer is offering.
 * @author Ryan Dinaro
 * @version 7/4/2023
 */
class TimeSlotComparator implements Comparator<TrainingTimeSlot> {
    /**
     * Compares the times based on day then time of starting
     * @param slot1 the first object to be compared.
     * @param slot2 the second object to be compared.
     * @return which starts sooner
     */
    @Override
    public int compare(TrainingTimeSlot slot1, TrainingTimeSlot slot2) {
        int dayComparison = slot1.getDay().compareTo(slot2.getDay());

        if (dayComparison != 0) {
            return dayComparison;
        } else {
            return Double.compare(slot1.getStartDoubleView(), slot2.getStartDoubleView());
        }
    }
}
