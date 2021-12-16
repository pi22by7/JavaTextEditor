package text_editor;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(area);
        frame.setSize(640, 480);
        frame.setVisible(true);

        JMenuBar menu_main = new JMenuBar();
        JMenu submenu_file = new JMenu("File");
        JMenuItem item_new = new JMenuItem("New");
        JMenuItem item_open = new JMenuItem("Open");
        JMenuItem item_save = new JMenuItem("Save");
        JMenuItem item_quit = new JMenuItem("Quit");

        item_new.addActionListener(this);
        item_open.addActionListener(this);
        item_save.addActionListener(this);
        item_quit.addActionListener(this);

        menu_main.add(submenu_file);
        submenu_file.add(item_new);
        submenu_file.add(item_open);
        submenu_file.add(item_save);
        submenu_file.add(item_quit);

        frame.setJMenuBar(menu_main);
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
                            ingest = (ingest == null ? new StringBuilder("null") : ingest).append(line);
                        }
                        area.setText(ingest == null ? null : ingest.toString());
                    }
                    catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                assert file != null;
                frame.setTitle("πpad | " +file.getName());
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
            case "Quit" -> System.exit(0);
        }
    }
}