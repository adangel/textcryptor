/*
 * Copyright 2020 Andreas Dangel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.adangel.textcryptor.actions;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.adangel.textcryptor.Crypter;
import org.adangel.textcryptor.Data;
import org.adangel.textcryptor.PasswordDialog;
import org.adangel.textcryptor.WrongPasswordException;
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
        try {
            StorageProvider.getSupported().load(data);
        } catch (UnsupportedOperationException ex) {
            JOptionPane.showMessageDialog(null, "Couldn't load data file!" + System.lineSeparator() + ex.toString()
                    + System.lineSeparator() + "Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }


        if (data.getEncryptedText().length > 0) {
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

            Crypter crypter = new Crypter();
            try {
                crypter.decrypt(data);
            } catch (WrongPasswordException e1) {
                JOptionPane.showMessageDialog(null, "Wrong password - exiting...", "Wrong password",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, data.getFontSize()));
            textArea.setLineWrap(data.isLineWrap());
            textArea.setText(data.getText());
            textArea.setCaretPosition(data.getCursorPosition());
            textArea.requestFocus();
            data.setDirty(false);
        }
    }
}
