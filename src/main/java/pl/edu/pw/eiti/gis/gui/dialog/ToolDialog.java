package pl.edu.pw.eiti.gis.gui.dialog;

import pl.edu.pw.eiti.gis.gui.MainWindow;

import javax.swing.*;

public abstract class ToolDialog extends JDialog {

    public ToolDialog(MainWindow mainWindow, String title) {
        super(mainWindow, title, true);

        setSize(300, 100);
        setLocation(mainWindow.getX() + 15, mainWindow.getY() + 15);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

}
