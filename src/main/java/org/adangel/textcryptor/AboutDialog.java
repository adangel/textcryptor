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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.adangel.textcryptor.storage.StorageProvider;

public class AboutDialog extends JDialog {
    private static final long serialVersionUID = -1607654815876569039L;

    public AboutDialog(JFrame owner) {
        super(owner, true);

        setTitle("About TextCryptor");

        JLabel header = new JLabel("TextCryptor");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        JTextArea main = new JTextArea();
        main.setEditable(false);
        main.setText(createAboutText());
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton close = new JButton("Close");
        buttons.add(close);
        close.addActionListener(e -> {
            this.setVisible(false);
            this.dispose();
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(main, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private String createAboutText() {
        StringBuilder sb = new StringBuilder();
        sb.append("Version: ").append(determineVersion()).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Storage: ").append(System.lineSeparator()).append(StorageProvider.getSupported().toString())
                .append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("License:").append(System.lineSeparator());
        sb.append("  Copyright 2020 Andreas Dangel\n" + " \n"
                + "  Licensed under the Apache License, Version 2.0 (the \"License\");\n"
                + "  you may not use this file except in compliance with the License.\n"
                + "  You may obtain a copy of the License at\n" + " \n"
                + "      http://www.apache.org/licenses/LICENSE-2.0\n" + " \n"
                + "  Unless required by applicable law or agreed to in writing, software\n"
                + "  distributed under the License is distributed on an \"AS IS\" BASIS,\n"
                + "  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
                + "  See the License for the specific language governing permissions and\n"
                + "  limitations under the License.\n" + "");

        return sb.toString();
    }

    private String determineVersion() {
        Properties properties = new Properties();
        try (InputStream in = AboutDialog.class.getClassLoader()
                .getResourceAsStream("META-INF/maven/org.adangel.textcryptor/textcryptor/pom.properties")) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return properties.getProperty("version", "n/a");
    }
}
