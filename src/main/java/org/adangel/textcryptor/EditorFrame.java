package org.adangel.textcryptor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.PlainDocument;
import javax.swing.undo.UndoManager;

import org.adangel.textcryptor.actions.DebugInfoAction;
import org.adangel.textcryptor.actions.ExitAction;
import org.adangel.textcryptor.actions.LoadAction;
import org.adangel.textcryptor.actions.RedoAction;
import org.adangel.textcryptor.actions.SaveAction;
import org.adangel.textcryptor.actions.UndoAction;

public class EditorFrame {

    public EditorFrame() {
        init();
    }

    private void init() {
        Data data = new Data();

        JFrame frame = new JFrame("TextCryptor");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ExitAction exitAction = new ExitAction();

        PlainDocument document = new PlainDocument();
        UndoManager undoManager = new UndoManager();
        UndoAction undoAction = new UndoAction(undoManager);
        RedoAction redoAction = new RedoAction(undoManager);
        JTextArea textArea = new JTextArea(document, null, 10, 80);
        SaveAction saveAction = new SaveAction(data, textArea);
        JScrollPane scrollPane = new JScrollPane(textArea);

        LineNumbers lines = new LineNumbers(textArea);

        document.addUndoableEditListener(undoManager);
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                undoAction.setEnabled(undoManager.canUndo());
                redoAction.setEnabled(undoManager.canRedo());
                data.setDirty(true);
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                undoAction.setEnabled(undoManager.canUndo());
                redoAction.setEnabled(undoManager.canRedo());
                data.setDirty(true);
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                undoAction.setEnabled(undoManager.canUndo());
                redoAction.setEnabled(undoManager.canRedo());
                data.setDirty(true);
            }
        });
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ActionEvent event = new ActionEvent(frame, ActionEvent.ACTION_FIRST, null);
                saveAction.actionPerformed(event);
                exitAction.actionPerformed(event);
            }
            @Override
            public void windowIconified(WindowEvent e) {
                // TODO save and lock?
                super.windowIconified(e);
                System.out.println("Minimized");
            }
            
            @Override
            public void windowDeiconified(WindowEvent e) {
                super.windowDeiconified(e);
                System.out.println("Restored");
            }
        });


        JMenuBar menuBar = new JMenuBar();
        JMenu menu;
        JMenuItem menuItem;
        menu = new JMenu("File");
        menuBar.add(menu);
        menuItem = new JMenuItem(saveAction);
        menuItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(e -> {
            saveAction.actionPerformed(e);
            exitAction.actionPerformed(e);
        });
        menu.add(menuItem);
        menu = new JMenu("Edit");
        menuBar.add(menu);
        menuItem = new JMenuItem(undoAction);
        menu.add(menuItem);
        menuItem = new JMenuItem(redoAction);
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Cut");
        menuItem.addActionListener(new DefaultEditorKit.CutAction());
        menuItem.setAccelerator(KeyStroke.getKeyStroke('X', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menu.add(menuItem);
        menuItem = new JMenuItem("Copy");
        menuItem.addActionListener(new DefaultEditorKit.CopyAction());
        menuItem.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menu.add(menuItem);
        menuItem = new JMenuItem("Paste");
        menuItem.addActionListener(new DefaultEditorKit.PasteAction());
        menuItem.setAccelerator(KeyStroke.getKeyStroke('V', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Find...");
        menuItem.setAccelerator(KeyStroke.getKeyStroke('F', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menuItem.addActionListener((e) -> {
            new SearchDialog(frame, textArea);
        });
        menu.add(menuItem);
        menu = new JMenu("Settings");
        menuBar.add(menu);
        menuItem = new JMenuItem("Increase Font");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menuItem.addActionListener((e) -> {
            int size = textArea.getFont().getSize();
            Font bigger = new Font(Font.MONOSPACED, Font.PLAIN, size + 2);
            textArea.setFont(bigger);
            lines.setFont(bigger);
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Decrease Font");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menuItem.addActionListener((e) -> {
            int size = textArea.getFont().getSize();
            Font smaller = new Font(Font.MONOSPACED, Font.PLAIN, size - 2);
            textArea.setFont(smaller);
            lines.setFont(smaller);
        });
        menu.add(menuItem);
        menuItem = new JCheckBoxMenuItem("Wrap");
        menuItem.setSelected(true);
        menuItem.addActionListener((e) -> {
            JCheckBoxMenuItem m = (JCheckBoxMenuItem) e.getSource();
            textArea.setLineWrap(m.isSelected());
        });
        menu.add(menuItem);
        menuItem = new JCheckBoxMenuItem("Show Linenumbers");
        menuItem.setSelected(true);
        menuItem.addActionListener((e) -> {
            JMenuItem it = (JMenuItem) e.getSource();
            scrollPane.setRowHeaderView(it.isSelected() ? lines : null);
        });
        menu.add(menuItem);

        JMenu submenu = new JMenu("Look & Feel");
        LookAndFeel currentLaf = UIManager.getLookAndFeel();
        ButtonGroup group = new ButtonGroup();
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(info.getName());
            group.add(rbMenuItem);
            if (currentLaf != null && currentLaf.getName().equals(info.getName())) {
                rbMenuItem.setSelected(true);
            }
            submenu.add(rbMenuItem);
            rbMenuItem.addActionListener(e -> {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                    SwingUtilities.updateComponentTreeUI(frame);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
        menu.add(submenu);

        menu = new JMenu("Help");
        menuBar.add(menu);
        menuItem = new JMenuItem(new DebugInfoAction());
        menu.add(menuItem);
        menuItem = new JMenuItem("About...");
        menu.add(menuItem);
        
        frame.setJMenuBar(menuBar);
        
        frame.getContentPane().setLayout(new BorderLayout());

        JLabel statusBar = new JLabel("Statusbar...");
        frame.getContentPane().add(statusBar, BorderLayout.SOUTH);

        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        lines.setFont(textArea.getFont());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                int lineIndex = document.getDefaultRootElement().getElementIndex(e.getDot());
                int startLine = document.getDefaultRootElement().getElement(lineIndex).getStartOffset();
                statusBar.setText(String.format("%d:%d (%d)%s", lineIndex + 1, e.getDot() - startLine + 1, e.getDot(),
                        data.isDirty() ? " dirty" : ""));
            }
        });
        
        scrollPane.setRowHeaderView(lines);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
 
        frame.pack();
        frame.setLocationRelativeTo(null); // center
        frame.setVisible(true);
        
        LoadAction loadAction = new LoadAction(data, textArea);
        loadAction.actionPerformed(null);
        textArea.setCaretPosition(data.getCursorPosition());
    }
}
