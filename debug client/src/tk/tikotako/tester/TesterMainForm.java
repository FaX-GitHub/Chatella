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

package tk.tikotako.tester;

import jdk.nashorn.internal.ir.annotations.Ignore;
import tk.tikotako.utils.PacketsManager;
import tk.tikotako.utils.PacketsManager.DataType;
import tk.tikotako.utils.PacketsManager.PacketType;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.ResourceBundle;

import static tk.tikotako.utils.Utils.validateIP;

/**
 * Created by ^-_-^ on 25/04/2017 @ 23:50.
 **/

public class TesterMainForm
{
    private Socket cliSock = null;
    private DataInputStream ServerToClient;
    private DataOutputStream ClientToServer;

    private JPanel panel1;
    private JButton Connetti;
    private JTextArea textArea1;
    private JTextField hostText;
    private JSpinner portSpinner;
    private JSlider versionSlider;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button10;
    private JTextPane textPane1;

    private String sHost = "192.168.2.5";
    private String tmpHost = null;
    private ActionListener actionListener;
    private PacketsManager packetsManager;

    private TesterMainForm()
    {
        packetsManager = new PacketsManager();

        versionSlider.setMajorTickSpacing(4);
        versionSlider.setMinorTickSpacing(1);
        versionSlider.setPaintTicks(true);
        versionSlider.setPaintLabels(true);

        hostText.setText(sHost);
        hostText.setHorizontalAlignment(SwingConstants.CENTER);
        portSpinner.setValue(9001);

        actionListener = e ->
        {
            if (Connetti.getActionCommand().equals(e.getActionCommand()))
            {
                if (cliSock == null)
                {
                    try
                    {
                        cliSock = new Socket(InetAddress.getByName("192.168.2.5"), 9001);
                        ServerToClient = new DataInputStream(cliSock.getInputStream());
                        ClientToServer = new DataOutputStream(cliSock.getOutputStream());
                        textArea1.append("Connected\r\nSending INIT [v] " + versionSlider.getValue() + "\r\n");
                        Connetti.setText(ResourceBundle.getBundle("localization").getString("B_disconnect"));
                        send(packetsManager.generatePacket(PacketType.TO_SERVER, DataType.INIT, "Chatella", versionSlider.getValue()));
                        new Thread(() ->
                        {
                            try
                            {
                                textArea1.append("Starting reading thread loop\r\n");
                                while (true)
                                {
                                    int x = ServerToClient.read();
                                    if (x < 0) { throw new Exception("Connection lost."); }
                                }
                            } catch (Exception eX)
                            {
                                textArea1.append(eX.toString() + "\r\n");
                                try
                                {
                                    cliSock.close();
                                    cliSock = null;
                                    textArea1.append("Disconnected\r\n");
                                    Connetti.setText(ResourceBundle.getBundle("localization").getString("B_connect"));
                                } catch (Exception eX2)
                                {
                                    //
                                }
                            }
                        }).start();
                    } catch (Exception eX)
                    {
                        JOptionPane.showMessageDialog(null, eX.getMessage());
                    }
                } else
                {
                    try
                    {
                        cliSock.close();
                        cliSock = null;
                        textArea1.append("Disconnected\r\n");
                        Connetti.setText(ResourceBundle.getBundle("localization").getString("B_connect"));
                    } catch (Exception eX)
                    {
                        JOptionPane.showMessageDialog(null, eX.getMessage());
                    }
                }
            } else if (button1.getActionCommand().equals(e.getActionCommand()))
            {
                // SEND NICK
                String nick = JOptionPane.showInputDialog("Nick:", "Fronk\u06de");
                if ((nick != null) && (!nick.equals("")))
                {
                    if(send(packetsManager.generatePacket(PacketType.TO_SERVER, DataType.NICK, nick)))
                    {
                        textArea1.append("Sending NICK [" + nick + "]\r\n");
                    }
                }
            } else if (button2.getActionCommand().equals(e.getActionCommand()))
            {
                // SEND REGISTER
                String mail = JOptionPane.showInputDialog("e-mail:", "pizza@mandolino.it");
                String pass = JOptionPane.showInputDialog("password:", "p@ssw0rd#123@à°_°");
                if ((mail != null) && (pass != null) && (!mail.equals(pass)) && (!pass.equals("")) && (!mail.equals("")))
                {
                    if(send(packetsManager.generatePacket(PacketType.TO_SERVER, DataType.REGISTER, mail, pass)))
                    {
                        textArea1.append("Sending REGISTER [" + mail + "] [" + pass + "] \r\n");
                    }
                }
            } else if (button3.getActionCommand().equals(e.getActionCommand()))
            {
                // SEND LOGIN
                String pass = JOptionPane.showInputDialog("password:", "p@ssw0rd#123@à°_°");
                if ((pass != null) && (!pass.equals("")))
                {
                    if(send(packetsManager.generatePacket(PacketType.TO_SERVER, DataType.LOGIN, pass)))
                    {
                        textArea1.append("Sending LOGIN [" + pass + "] \r\n");
                    }
                }
            } else if (button4.getActionCommand().equals(e.getActionCommand()))
            {
                // SEND MESSAGE
                String receiver = JOptionPane.showInputDialog("send to:", "FRONK");
                String message = JOptionPane.showInputDialog("message:", "ME LIEK POTETOES");
                if ((receiver != null) && (message != null) && (!receiver.equals(message)) && (!receiver.equals("")) && (!message.equals("")))
                {
                    if(send(packetsManager.generatePacket(PacketType.TO_SERVER, DataType.MESSAGE, receiver, message)))
                    {
                        textArea1.append("Sending MESSAGE [" + receiver + "] [" + message + "] \r\n");
                    }
                }
            }
        };

        setupButans();

        hostText.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                super.focusLost(e);
                String currentHost = hostText.getText();
                if (validateIP(currentHost) && (!sHost.equals(currentHost)))
                {
                    sHost = currentHost;
                } else
                {
                    hostText.setText(sHost);
                }
            }
        });

        versionSlider.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting())
                {
                    if (send(packetsManager.generatePacket(PacketType.TO_SERVER, DataType.INIT, "Chatella", source.getValue())))
                    {
                        textArea1.append("Sending INIT [v] " + source.getValue() + "\r\n");
                    }
                }
            }
        });
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("TesterMainForm");
        frame.setContentPane(new TesterMainForm().panel1);
        //noinspection MagicConstant
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setupButans()
    {
        for (Field field : this.getClass().getDeclaredFields())
        {
            field.setAccessible(true);
            try
            {
                if (field.getType().equals(JButton.class))
                {
                    ((JButton) field.get(this)).addActionListener(actionListener);
                    //((JButton) field.get(this)).setEnabled(false);
                }
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean send(byte[] pedro)
    {
        if ((cliSock != null) && (cliSock.isConnected()))
        {
            try
            {
                ClientToServer.write(pedro);
                ClientToServer.flush();
                return true;
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
        return false;
    }
}
