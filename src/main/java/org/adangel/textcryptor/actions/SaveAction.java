package org.adangel.textcryptor.actions;

import java.awt.event.ActionEvent;
import java.util.function.Supplier;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.adangel.textcryptor.Crypter;
import org.adangel.textcryptor.PasswordDialog;
import org.adangel.textcryptor.storage.StorageProvider;

public class SaveAction extends AbstractAction {
    private static final long serialVersionUID = 7220898984046873384L;

    private final Supplier<String> textSupplier;

    public SaveAction(Supplier<String> textSupplier) {
        super("Save");
        this.textSupplier = textSupplier;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        PasswordDialog dialog = new PasswordDialog(null);
        dialog.dispose();
        if (dialog.getEnteredPassword() != null) {
            System.out.println("after pw dialog: " + new String(dialog.getEnteredPassword()));
            Crypter crypter = new Crypter();
            byte[] data = crypter.encrypt(textSupplier.get(), dialog.getEnteredPassword());
            StorageProvider.getSupported().save(data);
        } else {
            JOptionPane.showMessageDialog(null, "No password given... exiting without saving");
            System.exit(0);
        }

    }
}
