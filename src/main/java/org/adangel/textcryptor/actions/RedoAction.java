package org.adangel.textcryptor.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;

public class RedoAction extends AbstractAction {
    private static final long serialVersionUID = 7000187579570010860L;

    private final UndoManager undoManager;

    public RedoAction(UndoManager undoManager) {
        super("Redo");
        this.undoManager = undoManager;
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('Z', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        undoManager.redo();
    }
}
