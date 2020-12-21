package org.adangel.textcryptor;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Element;

// https://www.javaprogrammingforums.com/java-swing-tutorials/915-how-add-line-numbers-your-jtextarea.html
public class LineNumbers extends JTextArea {
    private static final long serialVersionUID = -9112981310719826908L;

    private final JTextArea textArea;

    private Object currentLineHighlight;

    public LineNumbers(JTextArea textArea) {
        super("");
        this.textArea = textArea;
        setBackground(Color.LIGHT_GRAY);
        setEditable(false);

        this.textArea.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                updateCurrentLine();
            }
        });
        this.textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLinesText();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLinesText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLinesText();
            }
        });
        this.textArea.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLinesText();
            }
        });
    }

    private void updateLinesText() {
        SwingUtilities.invokeLater(() -> {
            int lineHeight = textArea.getFontMetrics(textArea.getFont()).getHeight();
            boolean lineWrap = textArea.getLineWrap();

            StringBuilder text = new StringBuilder();
            Element root = textArea.getDocument().getDefaultRootElement();
            String lines = String.valueOf(root.getElementCount());
            for (int i = 0; i < root.getElementCount(); i++) {
                text.append(formatNumber(i + 1, lines));

                if (lineWrap) {
                    Element line = root.getElement(i);
                    int startOffset = line.getStartOffset();
                    int endOffset = line.getEndOffset();
                    if (endOffset >= textArea.getDocument().getLength()) {
                        endOffset = textArea.getDocument().getLength() - 1;
                    }
                    try {
                        Rectangle2D start = textArea.modelToView2D(startOffset);
                        Rectangle2D end = textArea.modelToView2D(endOffset);
                        int emptyLines = (int) ((end.getY() - start.getY()) / lineHeight) - 1;
                        for (int j = 0; j < emptyLines; j++) {
                            text.append(System.lineSeparator());
                        }
                    } catch (BadLocationException e) {
                        System.out.println("Couldn't determine line wrap for line " + (i + 1) + ": start=" + startOffset
                                + " end=" + endOffset);
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
            setText(text.toString());
            currentLineHighlight = null;
            getHighlighter().removeAllHighlights();
            updateCurrentLine();
        });
    }

    private String formatNumber(int number, String biggestNumber) {
        String num = String.valueOf(number);

        for (int i = num.length(); i < biggestNumber.length(); i++) {
            num = " " + num;
        }
        return " " + num + " " + System.lineSeparator();
    }

    private void updateCurrentLine() {
        Element root = textArea.getDocument().getDefaultRootElement();
        int currentLine = root.getElementIndex(textArea.getCaretPosition());
        int lineNumber = currentLine + 1;
        String number = formatNumber(lineNumber, String.valueOf(root.getElementCount()));
        int start = getText().indexOf(number);
        int end = start + number.length() - 1;

        if (start < 0 || end < 0) {
            return;
        }

        try {
            if (currentLineHighlight == null) {
                currentLineHighlight = getHighlighter().addHighlight(start, end,
                        new DefaultHighlightPainter(Color.YELLOW));
            } else {
                getHighlighter().changeHighlight(currentLineHighlight, start, end);
            }
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        setCaretPosition(start);
    }

}
