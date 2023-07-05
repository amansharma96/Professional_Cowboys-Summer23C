package memoranda.ui.trainerUI;

import memoranda.util.training.DuplicateEntryException;
import memoranda.util.training.Member;
import memoranda.util.training.Student;
import memoranda.util.training.Trainer;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

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
        final String[] buttons = {"New Member", "Edit Member", "Remove Member"};

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
        JFrame frame = new JFrame("Member Information");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(4, 2));

        JLabel firstNameLabel = new JLabel("First Name: ");
        JTextField firstNameField = new JTextField();
        if (selectedMember != null) firstNameField.setText(selectedMember.getFirstName());

        JLabel lastNameLabel = new JLabel("Last Name: ");
        JTextField lastNameField = new JTextField();
        if (selectedMember != null) lastNameField.setText(selectedMember.getLastName());

        JLabel membershipLabel = new JLabel("Membership: ");
        JComboBox<String> membershipBox = new JComboBox<>(new String[]{"Yes", "No"});
        if (selectedMember != null) membershipBox.setSelectedItem(selectedMember.getActiveMembership() ? "Yes" : "No");

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            boolean isMember = Objects.equals(membershipBox.getSelectedItem(), "Yes");
            boolean flagDispose = true;
            if(selectedMember==null) {
                Member saveMember = null;
                try {
                    saveMember = new Member(firstName,lastName);
                    saveMember.updateInformation(firstName,lastName,isMember);
                    information.updateComponent();
                } catch (DuplicateEntryException ex) {
                    flagDispose = false;
                    firstNameField.setText("Name");
                    lastNameField.setText("Taken");
                }
            } else {
                selectedMember.updateInformation(firstName,lastName,isMember);
                information.updateComponent();
            }

            if(flagDispose) {
                frame.dispose();
            }
        });

        frame.add(firstNameLabel);
        frame.add(firstNameField);
        frame.add(lastNameLabel);
        frame.add(lastNameField);
        frame.add(membershipLabel);
        frame.add(membershipBox);
        frame.add(submitButton);

        frame.setLocationRelativeTo(null);  // Center the frame
        frame.setVisible(true);
    }
}
