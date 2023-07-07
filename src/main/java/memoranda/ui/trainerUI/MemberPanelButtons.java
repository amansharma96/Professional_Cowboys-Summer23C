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
        setBackground(Color.WHITE);
    }
    private void addButtons() {
        final String[] buttons = {"New Member", "Edit Member", "Remove Member"};

        JButton addMember = new JButton(buttons[0]);
        JButton editMember = new JButton(buttons[1]);
        JButton removeMember = new JButton(buttons[2]);
        editMember.setFocusable(false);
        removeMember.setFocusable(false);


        addMember.addActionListener(e -> {
            changeMember(new int[1]);
        });

        editMember.addActionListener(e -> {
            if(information.getSelected().length>0)
                changeMember(information.getSelected());
        });

        removeMember.addActionListener(e -> {
            information.removeSelected();
            information.updateComponent();
        });

        add(addMember);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(editMember);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(removeMember);
        add(Box.createRigidArea(new Dimension(10, 0)));

        updateUI();
    }

    private void changeMember(int[] selectedMemberIDs) {
        JFrame frame = new JFrame("Member Information");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(4, 2));


        Member defaultMember = information.getMember(selectedMemberIDs[0]);
        JLabel firstNameLabel = new JLabel("First Name: ");
        JTextField firstNameField = new JTextField();
        if (defaultMember != null) {
            firstNameField.setText(defaultMember.getFirstName());
        }

        JLabel lastNameLabel = new JLabel("Last Name: ");
        JTextField lastNameField = new JTextField();
        if (defaultMember != null) {
            lastNameField.setText(defaultMember.getLastName());
        }

        JLabel membershipLabel = new JLabel("Membership: ");
        JComboBox<String> membershipBox = new JComboBox<>(new String[]{"Yes", "No"});
        if (defaultMember != null) {
            membershipBox.setSelectedItem(defaultMember.getActiveMembership() ? "Yes" : "No");
        }

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            boolean isMember = Objects.equals(membershipBox.getSelectedItem(), "Yes");
            boolean flagDispose = true;
            if(defaultMember==null) {
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
                defaultMember.updateInformation(firstName,lastName,isMember);
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
