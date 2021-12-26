package text_editor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class FontChooser extends JDialog implements ActionListener {
    protected static JFrame frame;
////    private int returnValue;
////    private static int size = 20;

    public FontChooser() {
        run();
    }

    public void run() {
        frame = new JFrame("Choose Font...");
        frame.setLayout(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 240);
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton cancel = new JButton("Cancel");
        JButton okay = new JButton("Ok");

        cancel.setBounds(frame.getWidth()/5,frame.getHeight()/5,60,30);
        okay.setBounds(frame.getWidth()/10,frame.getHeight()/10,50,30);

        cancel.addActionListener(this);
        okay.addActionListener(this);
        buttonPanel.add(okay);
        buttonPanel.add(cancel);
        frame.add(buttonPanel,BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void save(){
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String ae = e.getActionCommand();
        switch (ae) {
            case "Ok" -> save();
            case "Cancel" -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }
}