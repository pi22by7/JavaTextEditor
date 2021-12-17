package text_editor;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TextEditor extends JFrame implements ActionListener {
    private static JFrame frame;
    private static JTextArea area;
    private static JCheckBox item_wrap;
    private static UndoManager manager;

    //    private static JScrollBar
//    private static final int returnValue = 0;
    public TextEditor() {
        run();
    }

    public void run() {
        frame = new JFrame("πpad");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        area = new JTextArea();
        frame.add(area);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setVisible(true);

        JMenuBar menu_main = new JMenuBar();

        JMenu submenu_file = new JMenu("File");
        JMenu submenu_format = new JMenu("Format");
        JMenu submenu_edit = new JMenu("Edit");

        JMenuItem item_new = new JMenuItem("New");
        JMenuItem item_open = new JMenuItem("Open");
        JMenuItem item_save = new JMenuItem("Save");
        JMenuItem item_quit = new JMenuItem("Quit");
        item_wrap = new JCheckBox("Text Wrap", false);
        JMenuItem item_undo = new JMenuItem("Undo");
        JMenuItem item_redo = new JMenuItem("Redo");

        item_new.addActionListener(this);
        item_open.addActionListener(this);
        item_save.addActionListener(this);
        item_quit.addActionListener(this);
        item_wrap.addActionListener(this);
        item_undo.addActionListener(this);
        item_redo.addActionListener(this);

        menu_main.add(submenu_file);
        menu_main.add(submenu_edit);
        menu_main.add(submenu_format);

        submenu_file.add(item_new);
        submenu_file.add(item_open);
        submenu_file.add(item_save);
        submenu_file.add(item_quit);

        submenu_edit.add(item_undo);
        submenu_edit.add(item_redo);

        submenu_format.add(item_wrap);

        frame.setJMenuBar(menu_main);

        JScrollPane scrollPane = new JScrollPane(area);
        frame.add(scrollPane);

        manager = new UndoManager();
        area.getDocument().addUndoableEditListener(manager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        StringBuilder ingest = null;
        JFileChooser filech = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        filech.setDialogTitle("Choose Destination.");
        filech.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        String ae = e.getActionCommand();
        int returnValue;
        switch (ae) {
            case "Open" -> {
                returnValue = filech.showOpenDialog(null);
                File file = null;
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    file = new File(filech.getSelectedFile().getAbsolutePath());
                    try {
                        FileReader read = new FileReader(file);
                        Scanner scan = new Scanner(read);
                        while (scan.hasNextLine()) {
                            String line = scan.nextLine() + "\n";
                            ingest = (ingest == null ? new StringBuilder() : ingest).append(line);
                        }
                        area.setText(ingest == null ? null : ingest.toString());
                    }
                    catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                assert file != null;
                frame.setTitle("πpad | " + file.getName());
            }
            case "Save" -> {
                returnValue = filech.showSaveDialog(null);
                File file = null;
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    file = new File(filech.getSelectedFile().getAbsolutePath());
                    try {
                        FileWriter out = new FileWriter(file);
                        out.write(area.getText());
                        out.close();
                    }
                    catch (FileNotFoundException ex) {
                        Component f = null;
                        JOptionPane.showMessageDialog(f, "File not found.");
                    }
                    catch (IOException ex) {
                        Component f = null;
                        JOptionPane.showMessageDialog(f, "Error.");
                    }
                }
                assert file != null;
                frame.setTitle("πpad | " +file.getName());
            }
            case "New" -> area.setText("");
            case "Text Wrap" -> {
                boolean b = item_wrap.isSelected();
                item_wrap.setSelected(b);
                if(item_wrap.isSelected()) {
                    area.setLineWrap(true);
                    area.setWrapStyleWord(true);
                    area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                }
                else {
                    area.setLineWrap(false);
                    area.setWrapStyleWord(false);
                    area.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
                }
            }
            case "Undo" -> {
                try {
                    if (manager.canUndo()) {
                        manager.undo();
                    }
                }
                catch (CannotUndoException ignore) {}
            }
            case "Redo" -> {
                try {
                    if (manager.canRedo()) {
                        manager.redo();
                    }
                }
                catch (CannotRedoException ignore) {}
            }
            case "Quit" -> System.exit(0);
        }
    }
}