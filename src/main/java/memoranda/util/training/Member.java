package memoranda.util.training;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Ryan Dinaro
 * This class represents a member of the gym
 */
public class Member{
    private String firstName, lastName;
    private String joinDate;
    private int memberID;
    private boolean activeMembership;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    public static HashMap<Integer, Member> MemberList = new HashMap<Integer, Member>();

    /*
     * On compile, populates the MemberList from the save file
     */
    static {
        populateMemberList();
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
        for(Member member : MemberList.values()) {
            if(firstName.equals(member.firstName)&&lastName.equals(member.getLastName())) {
                throw new DuplicateEntryException();
            }
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = formatter.format(new Date());
        this.memberID = generateMemberID(); //functions in O(N) time
        this.activeMembership = true;
        MemberList.put(memberID,this);
        saveInformation(true);
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
     * This generates an ID in O(N) time if MemberList is not generated yet.
     * Generates an ID in constant time if MemberList has already been generated
     * @return String representing the memberID
     */
    private int generateMemberID() {
        Random rand = new Random();
        int memberID = 0;
        do {
            //generate an id of length 6
            memberID = 111111111 + rand.nextInt(888888888);
        } while(MemberList.containsKey(memberID));
        return memberID;
    }

    /**
     * Saves the member information to the memberDatabase in Logs
     * @param keepOldFiles if false, erases old file before writing
     */
    private void saveInformation(boolean keepOldFiles) {
        try {
            FileWriter logWriter = new FileWriter("logs/memberDatabase", keepOldFiles);
            logWriter.write(getFirstName() + "\n" + getLastName() + "\n" + getJoinDate() + " "
                    + getMemberID() + " " + getActiveMembership() + "\n");
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
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Scanner memberPopulate = new Scanner(file);
            memberPopulate.useDelimiter(" ");
            if(memberPopulate.hasNextLine()) {
                while (memberPopulate.hasNextLine()) {
                    String firstName = memberPopulate.nextLine();
                    String lastName = memberPopulate.nextLine();
                    String joinDate = memberPopulate.next();
                    int memberID = Integer.parseInt(memberPopulate.next());
                    boolean activeMembership = Boolean.parseBoolean(memberPopulate.nextLine());
                    if(firstName!=null&&lastName!=null&&joinDate!=null) {
                        MemberList.put(memberID, new Member(firstName,lastName,joinDate,memberID,activeMembership));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    /**
     * When called this method will put the contents of the MemberList into the save files,
     * overwriting the old file. Necessary for any information changes that effects the save file
     */
    private void updateSaveFile() {
        MemberList.put(this.memberID, this);
        int count = 0;
        for(Member member : MemberList.values()) {
            member.saveInformation(count != 0);
            count++;
        }
    }


    /**
     * Looks up the member by the memberID
     * @param targetID the ID you are looking for
     * @return  the member that matches that ID
     *          returns null if no such member exists
     */
    public static Member lookupMember(int targetID) {
        return MemberList.get(targetID);
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
        updateSaveFile();
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
        updateSaveFile();
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
        updateSaveFile();
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
