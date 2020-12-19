package org.adangel.textcryptor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.adangel.textcryptor.Crypter;
import org.adangel.textcryptor.Data;
import org.adangel.textcryptor.PasswordDialog;
import org.adangel.textcryptor.storage.StorageProvider;

public class SaveAction extends AbstractAction {
    private static final long serialVersionUID = 7220898984046873384L;

    private final Data data;
    private final JTextArea textArea;

    public SaveAction(Data data, JTextArea textArea) {
        super("Save");
        this.data = data;
        this.textArea = textArea;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (data.getPassword().length == 0) {
            PasswordDialog dialog = new PasswordDialog(null);
            dialog.dispose();
            if (dialog.getEnteredPassword() != null) {
                data.setPassword(dialog.getEnteredPassword());
            } else {
                JOptionPane.showMessageDialog(null, "No password given... exiting without saving");
                System.exit(0);
            }
        }
        
        data.setText(textArea.getText());
        data.setCursorPosition(textArea.getCaretPosition());
        Crypter crypter = new Crypter();
        crypter.encrypt(data);
        StorageProvider.getSupported().save(data);
        data.setDirty(false);
    }
}
