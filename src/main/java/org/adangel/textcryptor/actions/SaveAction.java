package org.adangel.textcryptor.actions;

import java.awt.event.ActionEvent;
import java.util.function.Supplier;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.adangel.textcryptor.PasswordDialog;
import org.adangel.textcryptor.Storage;

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
            Storage storage = new Storage();
            storage.save(textSupplier.get(), dialog.getEnteredPassword());
        } else {
            JOptionPane.showMessageDialog(null, "No password given... exiting");
            System.exit(0);
        }

    }
}
