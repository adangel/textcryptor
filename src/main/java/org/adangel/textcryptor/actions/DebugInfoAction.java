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

import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JTextArea;

import org.adangel.textcryptor.App;
import org.adangel.textcryptor.storage.JarStorage;

public class DebugInfoAction extends AbstractAction {
    private static final long serialVersionUID = -7142766637058873694L;

    public DebugInfoAction() {
        super("Debug Info...");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Debug Info");
        dialog.setModalityType(ModalityType.MODELESS);
        dialog.getContentPane().setLayout(new FlowLayout());
        JTextArea info = new JTextArea();
        info.setEditable(false);
        dialog.getContentPane().add(info);
        
        URL url = App.class.getClassLoader().getResource(App.class.getName().replaceAll("\\.", "/") + ".class");
        info.setText("url: " + url);
        
        JarStorage storage = new JarStorage();
        if (storage.isJar()) {
            info.setText(info.getText() + "\n" + "Storage is a JAR file");
        } else {
            info.setText(info.getText() + "\n" + "Storage is NOT a jar file");
        }
        
        dialog.pack();
        dialog.setVisible(true);
    }
}
