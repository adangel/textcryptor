package org.adangel.textcryptor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.adangel.textcryptor.actions.DebugInfoAction;
import org.adangel.textcryptor.actions.ExitAction;
import org.adangel.textcryptor.actions.SaveAction;

public class EditorFrame {

    public EditorFrame() {
        init();
    }

    private void init() {
        JFrame frame = new JFrame("TextCryptor");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ExitAction exitAction = new ExitAction();

        JTextArea textArea = new JTextArea(10, 80);
        SaveAction saveAction = new SaveAction(textArea::getText);
        JScrollPane scrollPane = new JScrollPane(textArea);

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
            }
        });


        JMenuBar menuBar = new JMenuBar();
        JMenu menu;
        JMenuItem menuItem;
        menu = new JMenu("File");
        menuBar.add(menu);
        menuItem = new JMenuItem(saveAction);
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem(exitAction);
        menu.add(menuItem);
        menu = new JMenu("Edit");
        menuBar.add(menu);
        menuItem = new JMenuItem("Undo");
        menu.add(menuItem);
        menuItem = new JMenuItem("Redo");
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Cut");
        menu.add(menuItem);
        menuItem = new JMenuItem("Copy");
        menu.add(menuItem);
        menuItem = new JMenuItem("Paste");
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Find...");
        menu.add(menuItem);
        menu = new JMenu("Settings");
        menuBar.add(menu);
        menuItem = new JMenuItem("Increase Font");
        menu.add(menuItem);
        menuItem = new JMenuItem("Decrease Font");
        menu.add(menuItem);
        menuItem = new JCheckBoxMenuItem("Wrap");
        menuItem.setSelected(true);
        menu.add(menuItem);
        menuItem = new JCheckBoxMenuItem("Show Linenumbers");
        menuItem.setSelected(true);
        menuItem.addActionListener((e) -> {
            JMenuItem it = (JMenuItem)e.getSource();
            scrollPane.setRowHeaderView(it.isSelected() ? new JLabel("numbers") : null);
        });
        menu.add(menuItem);
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
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.addCaretListener(new CaretListener() {
            
            @Override
            public void caretUpdate(CaretEvent e) {
                statusBar.setText("CaretEvent: " + e.getDot());
                
            }
        });
        
        scrollPane.setRowHeaderView(new JLabel("row header"));
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
 
        frame.pack();
        frame.setLocationRelativeTo(null); // center
        frame.setVisible(true);
        
        PasswordDialog dialog = new PasswordDialog(frame);
        dialog.dispose();
        if (dialog.getEnteredPassword() != null) {
            System.out.println("after pw dialog: " + new String(dialog.getEnteredPassword()));
            Storage storage = new Storage();
            textArea.setText(storage.load(dialog.getEnteredPassword()));
            textArea.setCaretPosition(0); // TODO restore last position
        } else {
            JOptionPane.showMessageDialog(frame, "No password given... exiting");
            System.exit(0);
        }
    }
}
