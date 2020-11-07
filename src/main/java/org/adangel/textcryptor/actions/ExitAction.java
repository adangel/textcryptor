package org.adangel.textcryptor.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ExitAction extends AbstractAction {
    private static final long serialVersionUID = 662271333192924262L;

    public ExitAction() {
        super("Exit");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Exiting...");
        System.exit(0);
    }
}
