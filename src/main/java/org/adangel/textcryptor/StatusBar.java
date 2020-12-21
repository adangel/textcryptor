package org.adangel.textcryptor;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.text.Caret;
import javax.swing.text.Document;

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
    }

    private void updateStatus() {
        Document document = textArea.getDocument();
        Caret caret = textArea.getCaret();
        int lineIndex = document.getDefaultRootElement().getElementIndex(caret.getDot());
        int startLine = document.getDefaultRootElement().getElement(lineIndex).getStartOffset();
        setText(String.format("%d:%d (%d)%s", lineIndex + 1, caret.getDot() - startLine + 1, caret.getDot(),
                data.isDirty() ? " (unsaved changes)" : ""));

        String title = frame.getTitle();
        if (data.isDirty() && title.charAt(0) != '*') {
            frame.setTitle("*" + title);
        } else if (!data.isDirty() && title.charAt(0) == '*') {
            frame.setTitle(title.substring(1));
        }
    }
}
