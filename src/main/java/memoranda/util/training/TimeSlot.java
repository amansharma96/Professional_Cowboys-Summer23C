package memoranda.util.training;

public class TimeSlot {
    private final Member member;
    private final Day day;
    private final int hour;
    private final int minute;
    private final int durationInMinutes;


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
}
