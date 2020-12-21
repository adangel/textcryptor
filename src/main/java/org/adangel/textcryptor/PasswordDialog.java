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

package org.adangel.textcryptor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

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
        this(owner, false);
    }

    public PasswordDialog(JFrame owner, boolean change) {
        super(owner, true);
        setTitle("Password");

        JPasswordField passwordField = new JPasswordField(10);
        JLabel label = new JLabel("Enter the password: ");
        label.setLabelFor(passwordField);

        JPasswordField repeat = new JPasswordField(10);
        JLabel labelRepeat = new JLabel("Repeat");
        labelRepeat.setLabelFor(repeat);

        ActionListener ok = (e) -> {
            if (change && !Arrays.equals(passwordField.getPassword(), repeat.getPassword())) {
                return;
            }
            enteredPassword = passwordField.getPassword();
            setVisible(false);
        };
        if (change) {
            repeat.addActionListener(ok);
        } else {
            passwordField.addActionListener(ok);
        }

        JPanel inputPane = new JPanel();
        inputPane.setLayout(new GridLayout(change ? 2 : 1, 2));
        inputPane.add(label);
        inputPane.add(passwordField);

        if (change) {
            inputPane.add(labelRepeat);
            inputPane.add(repeat);
        }

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
        final JButton okButton = new JButton("OK");
        okButton.addActionListener(ok);
        buttons.add(okButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener((e) -> {
            enteredPassword = null;
            setVisible(false);
        });
        buttons.add(cancelButton);

        if (change) {
            KeyAdapter keyListener = new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    okButton.setEnabled(Arrays.equals(passwordField.getPassword(), repeat.getPassword())
                            && passwordField.getPassword().length > 0);
                }
            };
            passwordField.addKeyListener(keyListener);
            repeat.addKeyListener(keyListener);
            okButton.setEnabled(false);
        }

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
