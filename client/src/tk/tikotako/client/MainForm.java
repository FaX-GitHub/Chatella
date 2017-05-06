/*
 * (c) Copyright 2017 ^-_-^ (http://www.tikotako.tk).
 *
 * Chatella is licensed under the GNU General Public License v3.0.
 *
 *  You may obtain a copy of the License at:
 *
 *       http://www.gnu.org/licenses/gpl-3.0.txt
 *
 *
 */

package tk.tikotako.client;

import javax.swing.*;

/**
 * Created by ^-_-^ on 25/04/2017 @ 23:50.
 **/

public class MainForm
{
    private JPanel panel1;
    private JEditorPane anAwesomeStringWithEditorPane;
    private JTextArea anAwesomeStringWithTextArea;

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().panel1);
        //noinspection MagicConstant
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
