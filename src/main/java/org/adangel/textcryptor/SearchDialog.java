package org.adangel.textcryptor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.Caret;

public class SearchDialog extends JDialog {
    private static final long serialVersionUID = -1626531399053604797L;

    public SearchDialog(JFrame owner, JTextArea textArea) {
        super(owner, false);
        setTitle("Find...");
        
        JTextField input = new JTextField(10);
        JLabel label = new JLabel("Find:");
        label.setLabelFor(input);

        ActionListener findAction = (e) -> {
            Caret caret = textArea.getCaret();
            int pos = caret.getDot();
            String text = textArea.getText();
            int next = text.indexOf(input.getText(), pos);
            if (next == -1) {
                next = text.indexOf(input.getText(), 0);
            }
            if (next > -1) {
                caret.setDot(next);
                caret.moveDot(next + input.getText().length());
            }
        };
        input.addActionListener(findAction);
        
        JPanel inputPane = new JPanel();
        inputPane.setLayout(new FlowLayout(FlowLayout.TRAILING));
        inputPane.add(label);
        inputPane.add(input);
        
        JButton find = new JButton("Find");
        find.addActionListener(findAction);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener((e) -> {
            setVisible(false);
            dispose();
        });
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttons.add(find);
        buttons.add(cancel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPane, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                input.requestFocusInWindow();
            }
        });


        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
