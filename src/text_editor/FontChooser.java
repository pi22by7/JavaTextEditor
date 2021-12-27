package text_editor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import static text_editor.TextEditor.size;

public class FontChooser extends JFrame implements ActionListener {
    private static JFrame frame;
    protected String old_font = TextEditor.frame.getFont().getFontName();
    private String newFont;

    public FontChooser() {
        run();
    }

    public void run() {
        frame = new JFrame("Choose Font...");
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontName = ge.getAvailableFontFamilyNames();
        frame.setLayout(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 100);
        int index = 4;
        for (int i = 0; i < fontName.length; i++) {
            if (Objects.equals(fontName[i], old_font)) {
                index = i;
                break;
            }
        }
        JComboBox<String> fontComboBox = new JComboBox<>(fontName);
        fontComboBox.setSelectedIndex(index);
        fontComboBox.addActionListener(evt -> {
            Object source = evt.getSource();
            JComboBox<?> cb = null;
            if (source instanceof JComboBox) {
                cb = (JComboBox<?>) source;
            }
            newFont = (String) Objects.requireNonNull(cb).getSelectedItem();
        });

        frame.add(fontComboBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton cancel = new JButton("Cancel");
        JButton okay = new JButton("Ok");

        cancel.setBounds(frame.getWidth() / 5, frame.getHeight() / 5, 60, 30);
        okay.setBounds(frame.getWidth() / 10, frame.getHeight() / 10, 50, 30);

        cancel.addActionListener(this);
        okay.addActionListener(this);
        buttonPanel.add(okay);
        buttonPanel.add(cancel);
        frame.add(buttonPanel, FlowLayout.CENTER);

        frame.setVisible(true);
    }

    private void save(){
        text_editor.TextEditor.area.setFont(new Font(newFont,Font.PLAIN, size));
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String ae = e.getActionCommand();
        switch (ae) {
            case "Ok" -> save();
            case "Cancel" -> {
                text_editor.TextEditor.area.setFont(new Font(old_font,Font.PLAIN, size));
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }
    }
}