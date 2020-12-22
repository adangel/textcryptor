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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.text.Caret;
import javax.swing.text.Document;

import org.adangel.textcryptor.storage.Storage;
import org.adangel.textcryptor.storage.StorageProvider;

public class StatusBar extends JLabel {
    private static final long serialVersionUID = 6418628972512000073L;

    private final Data data;
    private final JTextArea textArea;
    private final JFrame frame;

    public StatusBar(Data data, JTextArea textArea, JFrame frame) {
        this.data = data;
        this.textArea = textArea;
        this.frame = frame;

        this.data.subscribe(new AbstractSubscriber() {
            @Override
            public void onNext(Data item) {
                updateStatus();
            }
        });
        this.textArea.addCaretListener(e -> updateStatus());
        updateStatus();
    }

    private void updateStatus() {
        Document document = textArea.getDocument();
        Caret caret = textArea.getCaret();
        int lineIndex = document.getDefaultRootElement().getElementIndex(caret.getDot());
        int startLine = document.getDefaultRootElement().getElement(lineIndex).getStartOffset();
        String text = String.format("%d:%d (%d)%s", lineIndex + 1, caret.getDot() - startLine + 1, caret.getDot(),
                data.isDirty() ? " (unsaved changes)" : "");

        Storage storage = StorageProvider.getSupported(data.getFile());
        if (storage != null) {
            text += " | " + storage;
        }

        setText(text);

        String title = frame.getTitle();
        if (data.isDirty() && title.charAt(0) != '*') {
            frame.setTitle("*" + title);
        } else if (!data.isDirty() && title.charAt(0) == '*') {
            frame.setTitle(title.substring(1));
        }
    }
}
