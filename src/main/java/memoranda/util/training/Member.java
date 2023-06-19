package memoranda.util.training;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

/**
 * @Author Ryan Dinaro
 * This class represents the
 */
public class Member {
    private String firstName, lastName;
    private Date joinDate, birthday;
    private String memberID;
    private boolean ActiveMembership;

    public Member(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = new Date();
        this.memberID = generateMemberID();
    }

    private String generateMemberID() {
        //works as a number implementation as no gym will ever have 888,888,888 members
        //numbers are easier read than a string of random char and numbers
        //able to be
        int memberID = 0;
        Random rand = new Random();
        //populate memberSet
        HashSet<Integer> members = new HashSet<Integer>();
        Scanner scan = new Scanner("logs/memberDatabase");
        while(scan.hasNextLine()) {
            while (scan.hasNextInt()) {
                members.add(scan.nextInt());
            }
            scan.nextLine();
        }
        do {
            //generate an id of length 6
            memberID = 111111111 + rand.nextInt(888888888);
        } while(members.add(memberID)); //until members does not contain the value

        /**
         * save member
         */
        try {
            FileWriter logWriter = new FileWriter("logs/memberDatabase", true);
            logWriter.write(getMemberID() + "\n" + Encode.encode(getFullName())+ "\n" + Encode.encode(getJoinDate())
                    + "\n" + Encode.encode(hasActiveMembership()));
            logWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return String.valueOf(memberID);
    }
    
    private String getMemberID() {
        return memberID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getID() {
        return memberID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    private void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    private Date getJoinDate() {
        return this.joinDate;
    }

    /**
     * Sets the members has an active
     * @param isActive true if the member has an active membership
     */
    public void setActiveMembership(boolean isActive) {
        this.ActiveMembership = isActive;
    }
    public boolean hasActiveMembership() {
        return this.ActiveMembership;
    }
}
