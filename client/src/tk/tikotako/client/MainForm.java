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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by ^-_-^ on 25/04/2017 @ 23:50.
 **/

public class MainForm
{
    Socket potato_chan = null;
    DataOutputStream serverToClient;

    private JPanel panel1;
    private JButton Connetti;
    private JButton data_1;
    private JButton data_2;
    private JButton data_3;
    private JTextArea textArea1;
    private JTextPane textPane1;

    private void send(String pedro)
    {
        try
        {
            int padro = pedro.length() * 2;
            textArea1.append(pedro + " -> " + padro + "\r\n");
            serverToClient.writeInt(padro);
            serverToClient.writeChars(pedro);
            serverToClient.flush();

        } catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

    private MainForm()
    {
        data_1.setEnabled(false);
        data_2.setEnabled(false);
        data_3.setEnabled(false);

        ActionListener listener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (Connetti.getActionCommand().equals(e.getActionCommand()))
                {
                    if (potato_chan == null)
                    {
                        try
                        {
                            potato_chan = new Socket(InetAddress.getByName("192.168.2.5"), 9001);
                            serverToClient = new DataOutputStream(potato_chan.getOutputStream());
                            Connetti.setText("Disconnetti");
                            data_1.setEnabled(true);
                            data_2.setEnabled(true);
                            data_3.setEnabled(true);
                        } catch (Exception eX)
                        {
                            JOptionPane.showMessageDialog(null, eX.getMessage());
                        }
                    } else
                    {
                        try
                        {
                            potato_chan.close();
                            potato_chan = null;
                            Connetti.setText("Connetti");
                            data_1.setEnabled(false);
                            data_2.setEnabled(false);
                            data_3.setEnabled(false);
                        } catch (Exception eX)
                        {
                            JOptionPane.showMessageDialog(null, eX.getMessage());
                        }
                    }
                } else if (data_1.getActionCommand().equals(e.getActionCommand()))
                {
                    send("﴾ﺭﺮﺯﺰﺱﺲﺳﺴﺵﺶ﴿");
                } else if (data_2.getActionCommand().equals(e.getActionCommand()))
                {
                    send("Penis Vagina Buttsex Nigger Lolwut Xaxaxa Kekeke 123 Mwa");
                } else if (data_3.getActionCommand().equals(e.getActionCommand()))
                {
                    send("i like turtles 2: Il ritorno della vendetta assassina.");
                }
            }
        };

        Connetti.addActionListener(listener);
        data_1.addActionListener(listener);
        data_2.addActionListener(listener);
        data_3.addActionListener(listener);
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().panel1);
        //noinspection MagicConstant
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
