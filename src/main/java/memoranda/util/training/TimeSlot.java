package memoranda.util.training;

public class TimeSlot {
    private final Member member;
    private final Day day;
    private final int hour;
    private final int minute;
    private final int durationInMinutes;
    private final int MINUTES_IN_HOUR = 60;


    /**
     * This represents a slot of time
     * @param member The member this timeslot is tied to
     * @param day the day this starts
     * @param hour the hour this starts
     * @param minute the minute this starts
     * @param durationInMinutes the duration in minutes of this slot
     */
    public TimeSlot(Member member, Day day, int hour, int minute, int durationInMinutes) {
        this.member = member;
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
}
