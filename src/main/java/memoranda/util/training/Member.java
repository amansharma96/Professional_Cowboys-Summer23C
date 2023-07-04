package memoranda.util.training;

import memoranda.util.file.FileUtilities;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Ryan Dinaro
 * This class represents a member of the gym
 */
public class Member implements Serializable{
    private String firstName, lastName;
    private String joinDate;
    private int memberID;
    private boolean activeMembership;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    public static List<Member> memberList;

    /*
     * On compile, populates the memberList from the save file
     */
    static {
        File file = new File("logs/memberDatabase");
        memberList = FileUtilities.populateList("logs/memberDatabase", Member.class);
    }

    /**
     * This constructor generates a new gym member with a unique ID,
     * first and last name, and the date
     * they joined the system.
     * @param firstName First name of member
     * @param lastName Last name of member
     */
    public Member(String firstName, String lastName) throws DuplicateEntryException{
        //This prevents duplicate information from being written to the member database when
        //a name that is already created is inputted to the constructor with two arguments
        //this prevents duplicate entries. Functions in O(N) time.
        for(Member member : memberList) {
            if(firstName.equals(member.firstName)&&lastName.equals(member.getLastName())) {
                throw new DuplicateEntryException();
            }
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = formatter.format(new Date());
        this.memberID = generateMemberID(); //functions in O(N) time
        this.activeMembership = true;
        memberList.add(this);
        FileUtilities.saveList("logs/memberDatabase", memberList);
    }

    public Member() {

    }

    /**
     * This constructor generates an already created member as an object
     * @param firstName first name of member
     * @param lastName last name of member
     * @param joinDate date of joining
     * @param memberID the members memberID
     * @param activeMembership if the member has an active membership
     */
    public Member(String firstName, String lastName, String joinDate, int memberID, boolean activeMembership) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = joinDate;
        this.memberID = memberID;
        this.activeMembership = activeMembership;
    }

    /**
     * This constructor generates a member from a MemberID
     * @param memberID the memberId of the member already created
     */
    public Member(int memberID) {
        Member member = Member.lookupMember(memberID);
        this.firstName = member.firstName;
        this.lastName = member.lastName;
        this.joinDate = member.joinDate;
        this.memberID = member.memberID;
        this.activeMembership = member.activeMembership;
    }

    /**
     * This generates an ID in O(N) time if memberList is not generated yet.
     * Generates an ID in constant time if memberList has already been generated
     * @return String representing the memberID
     */
    private int generateMemberID() {
        Random rand = new Random();
        int memberID = 0;
        while(true) {
            boolean restartFlag = false;
            //generate an id of length 6
            memberID = 111111111 + rand.nextInt(888888888);
            for(Member member : memberList) {
                if(member.getMemberID()==memberID) {
                    restartFlag = true;
                    break;
                }
            }
            if(!restartFlag) {
                return memberID;
            }
        }
    }
    /**
     * Looks up the member by the memberID
     * @param targetID the ID you are looking for
     * @return  the member that matches that ID
     *          returns null if no such member exists
     */
    public static Member lookupMember(int targetID) {
        for(Member member : memberList) {
            if(member.getMemberID() == targetID) {
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
        FileUtilities.saveList("logs/memberDatabase", memberList);
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
        FileUtilities.saveList("logs/memberDatabase", memberList);
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
        this.activeMembership = isActive;
        FileUtilities.saveList("logs/memberDatabase", memberList);
    }
    /**
     * returns the members current membership status
     * @return  - true if the member has an active membership
     *          - false if the members membership has expired
     */
    public boolean getActiveMembership() {
        return this.activeMembership;
    }
}
