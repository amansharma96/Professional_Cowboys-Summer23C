package memoranda.ui.trainerUI;

import memoranda.util.training.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MemberPanelInformation extends JPanel {
    private final JTable memberTable;
    private final DefaultTableModel memberTableModel;
    private static final String[] tableHeaders = {"First Name", "Last Name",
            "MemberID", "Join Date","Active Membership"};
    public MemberPanelInformation() {
        super();
        memberTableModel = new DefaultTableModel(tableHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        memberTable = new JTable(memberTableModel);

        Dimension dim = new Dimension(500,450);
        setSize(dim);
        setMaximumSize(dim);
        setMinimumSize(dim);
        setPreferredSize(dim);


        memberTable.setPreferredSize(dim);
        memberTable.setBackground(new Color(140,160,185));


        updateMemberListData();
        createMemberListUI();
    }
    private void createMemberListUI() {
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroller = new JScrollPane(memberTable);
        add(Box.createHorizontalGlue());
        add(listScroller, BorderLayout.CENTER);
    }

    private void updateMemberListData() {
        memberTableModel.setRowCount(0);
        for(Member member : Member.getMemberList()) {
            String[] row = new String[5];
            row[0] = member.getFirstName();
            row[1] = member.getLastName();
            row[4] = (member.getActiveMembership()) ? "Yes" : "No";
            row[3] = String.valueOf(member.getMemberID());
            row[2] = member.getJoinDate();
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
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow != -1) {
            memberTableModel.removeRow(selectedRow);
            updateComponent();
        }
    }
}
