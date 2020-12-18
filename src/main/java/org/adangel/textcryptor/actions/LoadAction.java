package org.adangel.textcryptor.actions;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.adangel.textcryptor.Crypter;
import org.adangel.textcryptor.PasswordDialog;
import org.adangel.textcryptor.storage.StorageProvider;

public class LoadAction extends AbstractAction {
    private static final long serialVersionUID = 7220898984046873384L;

    private final Consumer<String> textConsumer;

    public LoadAction(Consumer<String> textConsumer) {
        super("Load");
        this.textConsumer = textConsumer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PasswordDialog dialog = new PasswordDialog(null);
        dialog.dispose();
        if (dialog.getEnteredPassword() != null) {
            System.out.println("after pw dialog: " + new String(dialog.getEnteredPassword()));
            byte[] data = StorageProvider.getSupported().load();
            if (data.length > 0) {
                Crypter crypter = new Crypter();
                String text = crypter.decrypt(data, dialog.getEnteredPassword());
                textConsumer.accept(text);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No password given... exiting");
            System.exit(0);
        }

    }
}
