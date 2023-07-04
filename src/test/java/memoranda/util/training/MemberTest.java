package memoranda.util.training;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    private static Trainer trainer;
    private static final Random rand = new Random();
    private static Member member1;
    private static Member member2;
    private static int memberId1;
    private static int memberId2;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        String firstName = "firstName";
        String lastName = "lastName";
        while(true) {
            try {
                member1 = new Member(firstName+rand.nextInt(), lastName+rand.nextInt());
                member2 = new Member(firstName+rand.nextInt(), lastName+rand.nextInt());
                memberId1 = member1.getMemberID();
                break;
            } catch (RuntimeException | DuplicateEntryException e) {
                //regenerate a name
            }
        }
    }
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        Member.removeMember(member1,Member.getMemberList(),Member.getFilePath());
        Member.removeMember(member2,Member.getMemberList(),Member.getFilePath());
    }
    @Test
    void lookupMember() {
        Member testMember = Member.lookupMember(memberId1);
        assertEquals(testMember, member1);
    }
    @Test
    void getAndSetFirstName() {
        String testName = "Bob";
        member1.setFirstName(testName);
        assertEquals(member1.getFirstName(), testName);
    }

    @Test
    void getAndSetLastName() {
        String testName = "The Builder";
        member1.setLastName(testName);
        assertEquals(member1.getLastName(), testName);
    }
    @Test
    void getAndSetActiveMembership() {
        boolean isActive = true;
        member1.setActiveMembership(isActive);
        assertEquals(member1.getActiveMembership(), isActive);
    }
}