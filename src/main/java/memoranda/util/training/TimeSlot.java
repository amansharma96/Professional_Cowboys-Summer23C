package memoranda.util.training;

public class TimeSlot {
    private int memberID;
    private Day day;
    private int hourStart;
    private int minuteStart;
    private int hourEnd;
    private int minuteEnd;

    public TimeSlot(int memberID, Day day, int hourStart, int minuteStart, int hourEnd, int minuteEnd) {

        this.memberID = memberID;
        this.day = day;
        this.hourStart = hourStart;
        this.minuteStart = minuteStart;
        this.hourEnd = hourEnd;
        this.minuteEnd = minuteEnd;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public int getHourStart() {
        return hourStart;
    }

    public boolean setHourStart(int hourStart) {
        if(hourStart > 60 || hourStart < 0)
            return false;
        this.hourStart = hourStart;
        return true;
    }

    public int getMinuteStart() {
        return minuteStart;
    }

    public boolean setMinuteStart(int minuteStart) {
        if(minuteStart > 60 || minuteStart < 0)
            return false;
        this.minuteStart = minuteStart;
        return true;
    }

    public int getHourEnd() {
        return hourEnd;
    }

    public boolean setHourEnd(int hourEnd) {
        if(hourEnd > 60 || hourEnd < 0)
            return false;
        this.hourEnd = hourEnd;
        return true;
    }

    public int getMinuteEnd() {
        return minuteEnd;
    }

    public boolean setMinuteEnd(int minuteEnd) {
        if(minuteEnd > 60 || minuteEnd < 0)
            return false;
        this.minuteEnd = minuteEnd;
        return true;
    }
}
