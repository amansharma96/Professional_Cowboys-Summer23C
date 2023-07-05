package memoranda.ui.trainerUI;

import memoranda.util.training.Member;
import memoranda.util.training.Student;
import memoranda.util.training.Trainer;

import javax.swing.*;
import java.awt.*;

public class MemberPanelButtons extends JPanel {
    private final MemberPanelInformation information;

    public MemberPanelButtons(MemberPanelInformation information) {
        super();
        this.information = information;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createHorizontalGlue());
        addButtons();
        add(Box.createHorizontalGlue());
        setFocusable(false);
    }
    private void addButtons() {
        final String[] buttons = {"Add Member", "Edit Member", "Remove Member"};

        JButton addMember = new JButton(buttons[0]);
        JButton editMember = new JButton(buttons[1]);
        JButton removeMember = new JButton(buttons[2]);
        editMember.setFocusable(false);
        removeMember.setFocusable(false);


        addMember.addActionListener(e -> {
            changeMember(null);
        });

        editMember.addActionListener(e -> {
            Member selectedMember  = information.getSelected();
            changeMember(selectedMember);
        });

        removeMember.addActionListener(e -> {
            removeSelectedMember(information.getSelected());
        });

        add(addMember);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(editMember);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(removeMember);
        add(Box.createRigidArea(new Dimension(10, 0)));

        updateUI();
    }

    private void removeSelectedMember(Member selected) {
        if(selected!=null) {
            Member.removeMember(selected, Member.getMemberList(), Member.getFilePath());
            Member.removeMember(selected, Student.getStudentList(), Student.getFilePath());
            Member.removeMember(selected, Trainer.getTrainerList(), Trainer.getFilePath());
            information.removeSelected();
            information.updateComponent();
        }
    }

    private void changeMember(Member selectedMember) {
        new JFrame();
    }
}
