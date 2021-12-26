package text_editor;
import javax.swing.*;
import java.awt.*;

import static text_editor.TextEditor.frame;

public class StatusBar extends JPanel {
    public StatusBar() {
        super();
        super.setPreferredSize(new Dimension(frame.getWidth()/10, frame.getHeight()/20));
        init();
    }
    private void init() {
        JLabel l = new JLabel();
        l.setText("");
    }
}