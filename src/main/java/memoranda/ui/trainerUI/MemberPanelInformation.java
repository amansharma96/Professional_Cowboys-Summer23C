package memoranda.ui.trainerUI;

import memoranda.util.training.Member;
import memoranda.util.training.Student;
import memoranda.util.training.Trainer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MemberPanelInformation extends JPanel {
    private final JTable memberTable;
    private final DefaultTableModel memberTableModel;
    private static final String[] tableHeaders = {"First Name", "Last Name",
            "MemberID", "Join Date","Active Membership"};

    public MemberPanelInformation() {
        super(new BorderLayout());
        memberTableModel = new DefaultTableModel(tableHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        memberTable = new JTable(memberTableModel);

        Dimension dim = new Dimension(500,450);
        setPreferredSize(dim);

        setBackground(Color.WHITE);

        updateMemberListData();
        createMemberListUI();
    }

    private void createMemberListUI() {
        memberTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane listScroller = new JScrollPane(memberTable);
        add(listScroller, BorderLayout.CENTER);
    }

    private void updateMemberListData() {
        memberTableModel.setRowCount(0);
        for(Member member : Member.getMemberList()) {
            String[] row = new String[5];
            row[0] = member.getFirstName();
            row[1] = member.getLastName();
            row[4] = (member.getActiveMembership()) ? "Yes" : "No";
            row[2] = String.valueOf(member.getMemberID());
            row[3] = member.getJoinDate();
            memberTableModel.addRow(row);
        }
    }

    public void updateComponent() {
        updateMemberListData();
        updateUI();
    }

    public Member getSelected() {
        if(memberTable.getSelectedRow()>-1) {
            return Member.getMemberList().get(memberTable.getSelectedRow());
        } else {
            return null;
        }
    }

    public void removeSelected() {
        int[] selectedRows = memberTable.getSelectedRows();
        for(int i = selectedRows.length-1; i >= 0; i--){
            int memberId = Integer.parseInt((String)memberTableModel.getValueAt(selectedRows[i],2));
            Member member = Member.lookupMember(memberId);
            Member.removeMember(member,Member.getMemberList(),Member.getFilePath());
            Member.removeMember(member, Student.getStudentList(),Student.getFilePath());
            Member.removeMember(member, Trainer.getTrainerList(),Trainer.getFilePath());
            memberTableModel.removeRow(selectedRows[i]);
        }
        updateComponent();
    }
}
