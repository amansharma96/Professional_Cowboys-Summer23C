package memoranda.ui.trainerUI;

import javax.swing.*;
import java.awt.*;

public class MemberPanel extends JPanel {
    private final MemberPanelInformation information;
    private final MemberPanelButtons buttons;
    public MemberPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        information = new MemberPanelInformation();
        add(Box.createVerticalStrut(10));
        buttons = new MemberPanelButtons(information);
        add(information);
        add(buttons);
        updateUI();
    }
}
