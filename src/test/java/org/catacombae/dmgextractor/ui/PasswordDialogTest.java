/*
 * Copyright (c) 2021 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package org.catacombae.dmgextractor.ui;


class PasswordDialogTest {

    public static void main(String[] args) {
        char[] pwd = PasswordDialog.showDialog(null, "hej", "apa");
//        String pwd = JOptionPane.showInputDialog(null, "apa", "hej");
        if (pwd != null)
            System.out.println("Password: \"" + new String(pwd) + "\"");
        else
            System.out.println("User canceled dialog.");
    }
}
