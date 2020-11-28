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
