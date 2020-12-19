package org.adangel.textcryptor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.adangel.textcryptor.Crypter;
import org.adangel.textcryptor.Data;
import org.adangel.textcryptor.PasswordDialog;
import org.adangel.textcryptor.storage.StorageProvider;

public class LoadAction extends AbstractAction {
    private static final long serialVersionUID = 7220898984046873384L;

    private final Data data;
    private final JTextArea textArea;

    public LoadAction(Data data, JTextArea textArea) {
        super("Load");
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
                JOptionPane.showMessageDialog(null, "No password given... exiting");
                System.exit(0);
            }
        }
        
        StorageProvider.getSupported().load(data);
        if (data.getEncryptedText().length > 0) {
            Crypter crypter = new Crypter();
            crypter.decrypt(data);
            textArea.setText(data.getText());
            textArea.setCaretPosition(data.getCursorPosition());
            data.setDirty(false);
        }
    }
}
