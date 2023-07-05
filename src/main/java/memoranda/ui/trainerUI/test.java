package memoranda.ui.trainerUI;

import memoranda.util.training.DuplicateEntryException;
import memoranda.util.training.Member;

import javax.swing.*;
import java.awt.*;

public class test {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(1000,800));
        frame.setMinimumSize(new Dimension(1000,800));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MemberPanel memberPanel = new MemberPanel();
        frame.add(memberPanel);
        frame.setVisible(true);
    }

}
