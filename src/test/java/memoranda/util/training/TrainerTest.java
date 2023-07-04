package memoranda.util.training;

import org.junit.jupiter.api.*;

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
        boolean result = trainer.addAvailableTime(new TrainingTimeSlot(
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
        boolean result = trainer.addAvailableTime(new TrainingTimeSlot(
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
        boolean result = trainer.addAvailableTime(new TrainingTimeSlot(
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
        boolean result = trainer.addAvailableTime(new TrainingTimeSlot(
                trainer,null, testDay,testHour,testMinute,duration));
        boolean result2 = trainer.addAvailableTime(new TrainingTimeSlot(
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
        final TrainingTimeSlot trainingTimeSlot = new TrainingTimeSlot(
                trainer,null, testDay,testHour,testMinute,duration);
        boolean result = trainer.addAvailableTime(trainingTimeSlot);
        boolean expectedResult1 = true;
        assertEquals(expectedResult1,result);
        boolean result2 = trainer.fillTrainingSlot(trainingTimeSlot,student);
        boolean expectedResult2 = true;
        assertEquals(expectedResult2,result2);
    }

    @Test
    void overlappingScheduleTimes() {
        final Day testDay = Day.MONDAY;
        final int testHour = 0;
        final int testMinute = 0;
        final int duration = 1440;
        final TrainingTimeSlot trainingTimeSlot = new TrainingTimeSlot(
                trainer,null, testDay,testHour,testMinute,duration);
        boolean result = trainer.addAvailableTime(trainingTimeSlot);
        boolean expectedResult1 = true;
        assertEquals(expectedResult1,result);
        boolean result2 = trainer.fillTrainingSlot(trainingTimeSlot,student);
        boolean expectedResult2 = true;
        assertEquals(expectedResult2,result2);
        boolean result3 = trainer.fillTrainingSlot(trainingTimeSlot,student);
        boolean expectedResult3 = false;
        assertEquals(expectedResult3,result3);
    }

    @Test
    void removeStudent_studentWithNonMergableTimeSlot() {
        // Set up a time slot that cannot be merged with any other time slot
        TrainingTimeSlot ts = new TrainingTimeSlot(student, trainer, Day.MONDAY, 10, 0, 60);
        trainer.addAvailableTime(ts);
        student.setTrainingTimeSlot(ts,trainer);

        assertTrue(trainer.removeStudent(student));
        // Assert that the student's training time slot, current trainer are null after removal
        assertNull(student.getTrainingTimeSlot());
        // Assert that the trainer's available times contain the student's previous time slot
        assertTrue(trainer.getAvailableTimes().contains(ts));
    }

    @Test
    void removeStudent_studentWithOneMergableTimeSlot() {
        // Set up time slots that can be merged
        TrainingTimeSlot ts1 = new TrainingTimeSlot(student, trainer, Day.TUESDAY, 9, 0, 60);
        TrainingTimeSlot ts2 = new TrainingTimeSlot(null, trainer, Day.TUESDAY, 10, 0, 60);
        trainer.addAvailableTime(ts1);
        trainer.addAvailableTime(ts2);
        student.setTrainingTimeSlot(ts1, trainer);

        assertTrue(trainer.removeStudent(student));
        // Assert that the student's training time slot, current trainer are null after removal
        assertNull(student.getTrainingTimeSlot());
        // Assert that the trainer's available times contain the merged time slot
        TrainingTimeSlot merged = new TrainingTimeSlot(null, trainer, Day.TUESDAY, 9, 0, 120);
        assertTrue(trainer.getAvailableTimes().contains(merged));
    }

    @Test
    void removeStudent_studentWithTwoMergableTimeSlots() {
        // Set up time slots that can be merged
        TrainingTimeSlot ts1 = new TrainingTimeSlot(null, trainer, Day.WEDNESDAY, 9, 0, 60);
        TrainingTimeSlot ts2 = new TrainingTimeSlot(student, trainer, Day.WEDNESDAY, 10, 0, 60);
        TrainingTimeSlot ts3 = new TrainingTimeSlot(null, trainer, Day.WEDNESDAY, 11, 0, 60);
        trainer.addAvailableTime(ts1);
        trainer.addAvailableTime(ts2);
        trainer.addAvailableTime(ts3);
        student.setTrainingTimeSlot(ts2, trainer);

        assertTrue(trainer.removeStudent(student));
        // Assert that the student's training time slot, current trainer are null after removal
        assertNull(student.getTrainingTimeSlot());
        // Assert that the trainer's available times contain the merged time slot
        TrainingTimeSlot merged = new TrainingTimeSlot(null, trainer, Day.WEDNESDAY, 9, 0, 180);
        assertTrue(trainer.getAvailableTimes().contains(merged));
    }


}