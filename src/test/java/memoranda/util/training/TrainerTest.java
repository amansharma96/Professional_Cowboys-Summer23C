package memoranda.util.training;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TrainerTest {
    static Member member1;
    static Member member2;
    static Member member3;
    static Trainer trainer;
    static Student student;

    static Student student2;
    static Random rand = new Random();
    @BeforeEach
    void setUp() {
        String firstName = "firstName";
        String lastName = "lastName";
        while(true) {
            try {
                member1 = new Member(firstName+rand.nextInt(), lastName+rand.nextInt());
                member2 = new Member(firstName+rand.nextInt(), lastName+rand.nextInt());
                member3 = new Member(firstName+rand.nextInt(), lastName+rand.nextInt());
                break;
            } catch (RuntimeException | DuplicateEntryException e) {
                //regenerate a name
            }
        }
        final int validMinimumStartTime = 15;
        student = new Student(member1.getMemberID());
        student2 = new Student(member3.getMemberID());
        trainer = new Trainer(member2.getMemberID(), validMinimumStartTime);
    }
    @AfterEach
    void tearDown() {
        Member.removeMember(member1,Member.getMemberList(),Member.getFilePath());
        Member.removeMember(member2,Member.getMemberList(),Member.getFilePath());
        Student.removeMember(student, Student.getStudentList(), Student.getFilePath());
        Trainer.removeMember(trainer, Trainer.getTrainerList(), Trainer.getFilePath());
    }
    @Test
    void addNegativeStartAvailableTime() {
        final Day testDay = Day.MONDAY;
        final int testHour = -11;
        final int testMinute = -1;
        final int duration = 30;
        boolean result = trainer.addAvailableTime(new TimeSlot(
                trainer,null, testDay,testHour,testMinute,duration));
        boolean expectedResult = false;
        assertEquals(result,expectedResult);
    }
    @Test
    void addToLongOfAvailableTime() {
        final Day testDay = Day.MONDAY;
        final int testHour = 24;
        final int testMinute = 0;
        final int duration = 30;
        boolean result = trainer.addAvailableTime(new TimeSlot(
                trainer,null, testDay,testHour,testMinute,duration));
        boolean expectedResult = false;
        assertEquals(result,expectedResult);
    }

    @Test
    void bellowStatedMinimumAvailableTime() {
        final Day testDay = Day.MONDAY;
        final int testHour = 13;
        final int testMinute = 0;
        final int duration = 1;
        boolean result = trainer.addAvailableTime(new TimeSlot(
                trainer,null, testDay,testHour,testMinute,duration));
        boolean expectedResult = false;
        assertEquals(result,expectedResult);
    }

    @Test
    void overlappingAvailableTimes() {
        final Day testDay = Day.MONDAY;
        final int testHour = 0;
        final int testMinute = 0;
        final int duration = 1440;
        boolean result = trainer.addAvailableTime(new TimeSlot(
                trainer,null, testDay,testHour,testMinute,duration));
        boolean result2 = trainer.addAvailableTime(new TimeSlot(
                trainer,null, testDay,testHour,testMinute,duration));
        boolean expectedResult1 = true;
        boolean expectedResult2 = false;
        assertEquals(expectedResult1,result);
        assertEquals(expectedResult2,result2);
    }

    @Test
    void addStudentToTrainer() {
        final Day testDay = Day.MONDAY;
        final int testHour = 0;
        final int testMinute = 0;
        final int duration = 1440;
        final TimeSlot timeSlot = new TimeSlot(
                trainer,null, testDay,testHour,testMinute,duration);
        boolean result = trainer.addAvailableTime(timeSlot);
        boolean expectedResult1 = true;
        assertEquals(expectedResult1,result);
        boolean result2 = trainer.fillTrainingSlot(timeSlot,student);
        boolean expectedResult2 = true;
        assertEquals(expectedResult2,result2);
    }

    @Test
    void overlappingScheduleTimes() {
        final Day testDay = Day.MONDAY;
        final int testHour = 0;
        final int testMinute = 0;
        final int duration = 1440;
        final TimeSlot timeSlot = new TimeSlot(
                trainer,null, testDay,testHour,testMinute,duration);
        boolean result = trainer.addAvailableTime(timeSlot);
        boolean expectedResult1 = true;
        assertEquals(expectedResult1,result);
        boolean result2 = trainer.fillTrainingSlot(timeSlot,student);
        boolean expectedResult2 = true;
        assertEquals(expectedResult2,result2);
        boolean result3 = trainer.fillTrainingSlot(timeSlot,student);
        boolean expectedResult3 = false;
        assertEquals(expectedResult3,result3);
    }

    @Test
    void removeStudentFromTrainer() {

    }

    @Test
    void rejoiningTimeSlotsAfterRemovingStudent() {

    }

}