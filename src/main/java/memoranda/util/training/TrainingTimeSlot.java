package memoranda.util.training;

import java.util.Comparator;

public class TrainingTimeSlot {
    private final Member member;
    private final Trainer trainer;
    private final Day day;
    private final int hour;
    private final int minute;
    private final int durationInMinutes;
    private static final int MINUTES_IN_HOUR = 60;


    /**
     * This represents a slot of time
     * @param member The member this timeslot is tied to
     * @param trainer The trainer this timeslot is tied too.
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

    public Day getDay() {
        return day;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }

    public Member getMember() {
        return member;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    @Override
    public String toString() {
        return day.toString() + " " + hour + ":" + minute
                + " duration: " + durationInMinutes + " minutes";
    }

    public int getEndHour() {
        return hour + (int) Math.floor((double) (minute + durationInMinutes) / MINUTES_IN_HOUR);
    }

    public int getEndMinute() {
        return (minute + durationInMinutes) % MINUTES_IN_HOUR;
    }

    public double getStartDoubleView() {
        return hour + ((double) minute /MINUTES_IN_HOUR);
    }
    public double getEndDoubleView() {
        return getEndHour() + ((double) getEndHour() /MINUTES_IN_HOUR);
    }

    public Trainer getTrainer() {
        return trainer;
    }
}
class TimeSlotComparator implements Comparator<TrainingTimeSlot> {
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
