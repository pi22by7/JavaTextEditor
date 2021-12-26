package text_editor;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextEditor extends JFrame implements ActionListener {
    protected static JFrame frame;
    private static JTextArea area;
    private static JCheckBox item_wrap;
    private static UndoManager manager;
    private JPopupMenu rcontext;
    private int returnValue;
    private static int size = 20;

    public TextEditor() {
        run();
    }

    public void run() {
        frame = new JFrame("πpad");
        frame.setLayout(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        area = new JTextArea();
        Font f = new Font("serif", Font.PLAIN, size);
        area.setFont(f);
        frame.add(area);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(640, 480);
//        frame.setLayout(null);

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
        JMenuItem item_copy = new JMenuItem("Copy");
        JMenuItem item_paste = new JMenuItem("Paste");
        JMenuItem item_fontInc = new JMenuItem("Increase Font Size");
        JMenuItem item_fontDec = new JMenuItem("Decrease Font Size");
        JMenuItem item_font = new JMenuItem("Font...");
        JMenuItem item_date = new JMenuItem("Date and Time");

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
        item_fontInc.addActionListener(this);
        item_fontDec.addActionListener(this);
        item_date.addActionListener(this);
        item_font.addActionListener(this);

        submenu_file.add(item_new);
        submenu_file.add(item_new_window);
        submenu_file.add(item_open);
        submenu_file.add(item_save);
        submenu_file.add(item_quit);

        submenu_edit.add(item_undo);
        submenu_edit.add(item_redo);
        submenu_edit.addSeparator();
        submenu_edit.add(item_cut);
        submenu_edit.add(item_copy);
        submenu_edit.add(item_paste);
        submenu_edit.addSeparator();
        submenu_edit.add(item_fontInc);
        submenu_edit.add(item_fontDec);
        submenu_edit.addSeparator();
        submenu_edit.add(item_date);

        submenu_format.add(item_wrap);
        submenu_format.add(item_font);

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

        KeyStroke keyStrokeFontInc = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK);
        item_fontInc.setAccelerator(keyStrokeFontInc);

        KeyStroke keyStrokeFontDec = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK);
        item_fontDec.setAccelerator(keyStrokeFontDec);

        KeyStroke keyStrokeDate = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        item_date.setAccelerator(keyStrokeDate);

        area.addMouseWheelListener(mouseWheelEvent ->
        {
            if (mouseWheelEvent.isControlDown())
            {
                int scrolled = mouseWheelEvent.getUnitsToScroll();
                Font font = area.getFont();
                int fontSize = font.getSize();
                fontSize -= (scrolled / 3);
                Font newFont = new Font(font.getFontName(), font.getStyle(), fontSize);
                area.setFont(newFont);
            }
            else
            {
                area.getParent().dispatchEvent(mouseWheelEvent);
            }
        });

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

        StatusBar status = new StatusBar();
        frame.add(status, BorderLayout.SOUTH);

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

    private void save(JFileChooser filech){
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
//        assert file != null;
        frame.setTitle("πpad" + (file != null ? " | "+file.getName() : ""));
    }

    private void open(StringBuilder ingest, JFileChooser filech) {
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
//        assert file != null;
        frame.setTitle("πpad" + (file != null ? " | " +file.getName() : ""));
    }

    private void wrap() {
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

    private void newF() {
        area.setText("");
        frame.setTitle("πpad");
    }

    private void FontInc() {
        size+=3;
        area.setFont(new Font("serif", Font.PLAIN, size));
    }

    private void FontDec() {
        size-=3;
        area.setFont(new Font("serif", Font.PLAIN, size));
    }

    private void changeFont() {
        new FontChooser();
    }

    private void DaTime() {
        LocalDateTime daTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        area.append(daTime.format(formatter));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        StringBuilder ingest = null;
        JFileChooser filech = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        filech.setDialogTitle("Choose Destination.");
        filech.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        String ae = e.getActionCommand();
        switch (ae) {
            case "Open" -> open(ingest, filech);
            case "Save" -> save(filech);
            case "New" -> newF();
            case "New Window" -> new TextEditor();
            case "Text Wrap" -> wrap();
            case "Cut" -> area.cut();
            case "Copy" -> area.copy();
            case "Paste" -> area.paste();
            case "Undo" -> undo();
            case "Redo" -> redo();
            case "Delete" -> {}
            case "Select All" -> area.selectAll();
            case "Increase Font Size" -> FontInc();
            case "Decrease Font Size" -> FontDec();
            case "Font..." -> changeFont();
            case "Date and Time" -> DaTime();
            case "Quit" -> System.exit(0);
//            default -> throw new IllegalStateException("Unexpected value: " + ae);
        }
    }
}