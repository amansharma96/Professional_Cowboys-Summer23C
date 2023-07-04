package memoranda.util.training;

public enum Day {

    MONDAY(0, "Monday"), TUESDAY(1, "Tuesday"), WEDNESDAY(2, "Wednesday"),
    THURSDAY(3, "Thursday"), FRIDAY(4, "Friday"), SATURDAY(5, "Saturday"),
    SUNDAY(6, "Sunday"), UNDEFINED(1000, "Default Time");
    private final int dayID;
    private final String strRepresentation;
    Day(int dayID, String strRepresentation) {
        this.dayID = dayID;
        this.strRepresentation = strRepresentation;
    }
    public int getDayID() {
        return dayID;
    }
    @Override
    public String toString() {
        return strRepresentation;
    }
}

