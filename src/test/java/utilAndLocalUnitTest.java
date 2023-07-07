import memoranda.util.Local;
import memoranda.util.Util;
import org.junit.jupiter.api.*;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class utilAndLocalUnitTest {
    @Test
    public void testGenerateId() {
        String id1 = Util.generateId();
        String id2 = Util.generateId();

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);
    }

    @Test
    public void testGenerateIdNotNull() {
        String id = Util.generateId();
        assertNotNull(id);
    }

    @Test
    public void testParseDateStamp() {
        String dateString = "4/16/2023";
        int[] expectedDate = new int[]{4, 16, 2023};

        int[] actualDate = Util.parseDateStamp(dateString);

        assertArrayEquals(expectedDate, actualDate);
    }

    @Test
    public void testGetDateStamp() {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.APRIL, 16);

        String expectedDateStamp = "16/3/2023";
        String actualDateStamp = Util.getDateStamp(cal);

        assertEquals(expectedDateStamp, actualDateStamp);
    }

    @Test
    public void testGetDateStampWithDifferentDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.JUNE, 22);

        String expectedDateStamp = "22/5/2023";
        String actualDateStamp = Util.getDateStamp(cal);

        assertEquals(expectedDateStamp, actualDateStamp);
    }

    @Test
    public void testGetDateString() {
        int month = Calendar.APRIL;
        int dayOfMonth = 16;
        int year = 2023;
        int format = 0;

        String expectedOutput = "Sunday, April 16, 2023";
        String actualOutput = Local.getDateString(month, dayOfMonth, year, format);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetDateStringWithDifferentFormat() {
        int month = Calendar.MAY;
        int dayOfMonth = 22;
        int year = 2023;
        int format = 1;

        String expectedOutput = "May 22, 2023";
        String actualOutput = Local.getDateString(month, dayOfMonth, year, format);

        assertEquals(expectedOutput, actualOutput);
    }
}
