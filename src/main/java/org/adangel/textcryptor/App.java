package org.adangel.textcryptor;

import javax.swing.SwingUtilities;

public class App {
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(EditorFrame::new);
    }
}
