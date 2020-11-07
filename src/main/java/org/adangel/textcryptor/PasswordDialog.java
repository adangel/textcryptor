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
import javax.swing.JPasswordField;

public class PasswordDialog extends JDialog {
    private static final long serialVersionUID = 8709776356773356650L;

    private char[] enteredPassword;

    public PasswordDialog(JFrame owner) {
        super(owner, true);
        setTitle("Password");
        
        JPasswordField passwordField = new JPasswordField(10);
        JLabel label = new JLabel("Enter the password: ");
        label.setLabelFor(passwordField);

        ActionListener ok = (e) -> {
            enteredPassword = passwordField.getPassword();
            setVisible(false);
        };
        passwordField.addActionListener(ok);

        JPanel inputPane = new JPanel();
        inputPane.setLayout(new FlowLayout(FlowLayout.TRAILING));
        inputPane.add(label);
        inputPane.add(passwordField);
        
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("OK");
        okButton.addActionListener(ok);
        buttons.add(okButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener((e) -> {
            enteredPassword = null;
            setVisible(false);
        });
        buttons.add(cancelButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPane, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                passwordField.requestFocusInWindow();
            }
        });
        
        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
    
    public char[] getEnteredPassword() {
        return enteredPassword;
    }
}
