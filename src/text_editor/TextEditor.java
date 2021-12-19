package text_editor;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TextEditor extends JFrame implements ActionListener {
    private static JFrame frame;
    private static JTextArea area;
    private static JCheckBox item_wrap;
    private static UndoManager manager;
    private JPopupMenu rcontext;

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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(640, 480);

        JMenuBar menu_main = new JMenuBar();

        JMenu submenu_file = new JMenu("File");
        submenu_file.setMnemonic(KeyEvent.VK_F);
        JMenu submenu_format = new JMenu("Format");
        submenu_format.setMnemonic(KeyEvent.VK_R);
        JMenu submenu_edit = new JMenu("Edit");
        submenu_edit.setMnemonic(KeyEvent.VK_E);

        JMenuItem item_new = new JMenuItem("New");
        item_new.setMnemonic(KeyEvent.VK_N);
        JMenuItem item_new_window = new JMenuItem("New Window");
        JMenuItem item_open = new JMenuItem("Open");
        item_open.setMnemonic(KeyEvent.VK_O);
        JMenuItem item_save = new JMenuItem("Save");
        item_save.setMnemonic(KeyEvent.VK_S);
        JMenuItem item_quit = new JMenuItem("Quit");
        item_wrap = new JCheckBox("Text Wrap", false);
        item_wrap.setMnemonic(KeyEvent.VK_T);
        JMenuItem item_undo = new JMenuItem("Undo");
        JMenuItem item_redo = new JMenuItem("Redo");
        JMenuItem item_cut = new JMenuItem("Cut");
        item_cut.addActionListener(this);
        JMenuItem item_copy = new JMenuItem("Copy");
        item_copy.addActionListener(this);
        JMenuItem item_paste = new JMenuItem("Paste");
        item_paste.addActionListener(this);

        item_new.addActionListener(this);
        item_new_window.addActionListener(this);
        item_open.addActionListener(this);
        item_save.addActionListener(this);
        item_quit.addActionListener(this);
        item_wrap.addActionListener(this);
        item_undo.addActionListener(this);
        item_redo.addActionListener(this);
        item_copy.addActionListener(this);
        item_cut.addActionListener(this);
        item_paste.addActionListener(this);

        submenu_file.add(item_new);
        submenu_file.add(item_new_window);
        submenu_file.add(item_open);
        submenu_file.add(item_save);
        submenu_file.add(item_quit);

        submenu_edit.add(item_undo);
        submenu_edit.add(item_redo);
        submenu_edit.add(item_cut);
        submenu_edit.add(item_copy);
        submenu_edit.add(item_paste);

        submenu_format.add(item_wrap);

        menu_main.add(submenu_file);
        menu_main.add(submenu_edit);
        menu_main.add(submenu_format);

        frame.setJMenuBar(menu_main);

        JScrollPane scrollPane = new JScrollPane(area);
        frame.add(scrollPane);

        manager = new UndoManager();
        area.getDocument().addUndoableEditListener(manager);

        KeyStroke keyStrokeOpen = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
        item_open.setAccelerator(keyStrokeOpen);

        KeyStroke keyStrokeNew = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        item_new.setAccelerator(keyStrokeNew);

        KeyStroke keyStrokeNewWin = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
        item_new_window.setAccelerator(keyStrokeNewWin);

        KeyStroke keyStrokeSave = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        item_save.setAccelerator(keyStrokeSave);

        KeyStroke keyStrokeUndo = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
        item_undo.setAccelerator(keyStrokeUndo);

        KeyStroke keyStrokeRedo = KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK);
        item_redo.setAccelerator(keyStrokeRedo);

        KeyStroke keyStrokeCopy = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);
        item_copy.setAccelerator(keyStrokeCopy);

        KeyStroke keyStrokeCut = KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK);
        item_cut.setAccelerator(keyStrokeCut);

        KeyStroke keyStrokePaste = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK);
        item_paste.setAccelerator(keyStrokePaste);


        rcontext = new JPopupMenu();

        JMenuItem item_cUndo = new JMenuItem("Undo");
        item_cUndo.addActionListener(this);
        rcontext.add(item_cUndo);

        rcontext.addSeparator();

        JMenuItem item_cCut = new JMenuItem("Cut");
        item_cCut.addActionListener(this);
        rcontext.add(item_cCut);

        JMenuItem item_cCopy = new JMenuItem("Copy");
        item_cCopy.addActionListener(this);
        rcontext.add(item_cCopy);

        JMenuItem item_cPaste = new JMenuItem("Paste");
        item_cPaste.addActionListener(this);
        rcontext.add(item_cPaste);

        rcontext.addSeparator();

        JMenuItem item_selAll = new JMenuItem("Select All");
        item_selAll.addActionListener(this);
        rcontext.add(item_selAll);

        MouseListener popupListener = new PopupListener();
        area.addMouseListener(popupListener);

        area.add(rcontext);

        frame.setVisible(true);
    }

    private void undo() {
        try {
            if (manager.canUndo()) {
                manager.undo();
            }
        }
        catch (CannotUndoException ignore) {}
    }

    private void redo() {
        try {
            if (manager.canRedo()) {
                manager.redo();
            }
        }
        catch (CannotRedoException ignore) {}
    }

    class PopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                rcontext.show(e.getComponent(),
                        e.getX(), e.getY());
            }
        }
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
                    catch (NullPointerException ex) {
                        Component f = null;
                        JOptionPane.showMessageDialog(f, "No file was selected.");
                    }
                }
//                assert file != null;
                frame.setTitle("πpad" + (file != null ? " | " +file.getName() : ""));
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
                    catch (NullPointerException ex) {
                        Component f = null;
                        JOptionPane.showMessageDialog(f, "No file was selected.");
                    }
                }
//                assert file != null;
                frame.setTitle("πpad" + (file != null ? " | "+file.getName() : ""));
            }
            case "New" -> {
                area.setText("");
                frame.setTitle("πpad");
            }
            case "New Window" -> new TextEditor();
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
            case "Cut" -> area.cut();
            case "Copy" -> area.copy();
            case "Paste" -> area.paste();
            case "Undo" -> undo();
            case "Redo" -> redo();
            case "Select All" -> area.selectAll();
            case "Quit" -> System.exit(0);
        }
    }
}