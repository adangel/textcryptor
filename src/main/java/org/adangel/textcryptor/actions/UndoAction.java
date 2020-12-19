package org.adangel.textcryptor.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;

public class UndoAction extends AbstractAction {
    private static final long serialVersionUID = 7000187579570010860L;

    private final UndoManager undoManager;

    public UndoAction(UndoManager undoManager) {
        super("Undo");
        this.undoManager = undoManager;
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        undoManager.undo();
    }
}
