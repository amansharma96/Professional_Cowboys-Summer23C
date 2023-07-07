package memoranda.util.training;

import memoranda.util.training.*;
import org.junit.jupiter.api.Assertions;

import java.util.Random;

class StudentTest {
    private static Student student;
    private static final Random rand = new Random();
    private static Member member1;
    private static Member member2;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        String firstName = "firstName";
        String lastName = "lastName";
        while(true) {
            try {
                member1 = new Member(firstName+rand.nextInt(), lastName+rand.nextInt());
                member2 = new Member(firstName+rand.nextInt(), lastName+rand.nextInt());
                break;
            } catch (RuntimeException | DuplicateEntryException e) {
                firstName += "1";
            }
        }
        final int validStudentID = member1.getMemberID();
        final int validTrainerID = member2.getMemberID();
        final int validMinimumStartTime = 15;
        student = new Student(validStudentID);
    }
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        Member.removeMember(member1,Member.getMemberList(),Member.getFilePath());
        Member.removeMember(member2,Member.getMemberList(),Member.getFilePath());
        Student.removeMember(student,Student.getStudentList(),Student.getFilePath());
        Trainer.removeMember(member1,Trainer.getTrainerList(),Trainer.getFilePath());
    }
}