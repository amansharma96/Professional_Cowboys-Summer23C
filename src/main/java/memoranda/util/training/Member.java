package memoranda.util.training;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Ryan Dinaro
 * This class represents a member of the gym
 */
public class Member {
    private String firstName, lastName;
    private final String joinDate;
    private final int memberID;
    private boolean ActiveMembership;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    public static ArrayList<Member> MemberList = new ArrayList<Member>();

    /**
     * This constructor generates a gym member with a unique ID, first and last name, and the date
     * they joined the system.
     * @param firstName First name of member
     * @param lastName Last name of member
     */
    public Member(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = formatter.format(new Date());
        this.memberID = generateMemberID();
        System.out.println("GOT HERE 1");
        saveInformation();
        System.out.println("GOT HERE 2");
    }

    /**
     * A constructor with already generated join date and memberID
     * @param firstName
     * @param lastName
     */
    public Member(String firstName, String lastName, String joinDate, int memberID, boolean activeMembership) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = joinDate;
        this.memberID = memberID;
        this.setActiveMembership(activeMembership);
    }

    /**
     * This generates an ID in O(N) time if MemberList is not generated yet.
     * Generates an ID in constant time if MemberList has already been generated
     * @return String representing the memberID
     */
    private int generateMemberID() {
        if(MemberList.size()==0)
            populateMemberList();
        Random rand = new Random();
        int memberID = 0;
        do {
            //generate an id of length 6
            memberID = 111111111 + rand.nextInt(888888888);
        } while(MemberList.add(new Member(this.firstName, this.lastName, this.joinDate, this.memberID, this.ActiveMembership))); //until members does not contain the value
        return memberID;
    }

    /**
     * Saves the member information to the memberDatabase in Logs
     */
    private void saveInformation() {
        try {
            FileWriter logWriter = new FileWriter("logs/memberDatabase", true);
            logWriter.write(getMemberID() + " " + getFullName() + " " +
                    getJoinDate() + " " + hasActiveMembership());
            logWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * populates the MemberList with all the memberIDs in the database
     */
    private static void populateMemberList() {
        File file = new File("logs/memberDatabase");
        try {
            Scanner scan = new Scanner(file);
            while(scan.hasNext()) {
                String firstName = scan.next();
                String lastName = scan.next();
                String joinDate = scan.next();
                int id = scan.nextInt();
                boolean someBoolean = scan.nextBoolean();

                MemberList.add(new Member(firstName, lastName, joinDate, id, someBoolean));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    /**
     * Looks up the member by the memberID
     * @param targetID the ID you are looking for
     * @return  the member that matches that ID
     *          returns null if no such member exists
     */
    public static Member lookupMember(int targetID) {
        populateMemberList();
        for(Member member : MemberList) {
            if(member.getMemberID()==targetID) {
                return member;
            }
        }
        return null;
    }

    /**
     * returns the member ID of the member
     * @return member ID of the member
     */
    public int getMemberID() {
        return memberID;
    }

    /**
     * Returns the first name of the member
     * @return first name of the member
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * change the first name - allows for name changes
     * @param firstName the desired first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the member
     * @return last name of the member
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * change the last name - allows for name changes
     * @param lastName the desired last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * returns the full name of the member
     * @return full name of the member
     */
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    /**
     * Returns the join date of the member
     * @return join date of the member
     */
    public String getJoinDate() {
        return this.joinDate;
    }

    /**
     * Sets the members current membership status
     * @param isActive
     *      - true if the member has an active membership
     *      - false if the members membership has expired
     */
    public void setActiveMembership(boolean isActive) {
        this.ActiveMembership = isActive;
    }
    /**
     * returns the members current membership status
     * @return  - true if the member has an active membership
     *          - false if the members membership has expired
     */
    public boolean hasActiveMembership() {
        return this.ActiveMembership;
    }
}
