package de.ur.mi.pluginhelper.ui;

import javax.swing.*;

public class UserDialogManager {


    public static UserResponse showConfirmationDialog(String msg, String title) {
       int result = JOptionPane.showConfirmDialog(null, msg, title,
               JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        return UserResponse.values()[result];
    }
}
