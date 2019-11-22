package de.ur.mi.pluginhelper.ui;

import javax.swing.*;

public class UserDialogManager {


    public static UserResponse showConfirmationDialog(String msg, String title) {
       int result = JOptionPane.showConfirmDialog(null, msg, title,
               JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        return UserResponse.values()[result];
    }

    public static JDialog createStatusDialog(String msg, String title) {
        JFrame frame = new JFrame(title);
        JOptionPane pane = new JOptionPane();
        pane.setMessage(msg);
        JDialog dialog = pane.createDialog(frame, title);
        return  dialog;
    }
}
