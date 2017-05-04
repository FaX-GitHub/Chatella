package tk.tikotako.server;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import javax.swing.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import tk.tikotako.utils.Utils;

import static tk.tikotako.server.MainForm.*;
import static tk.tikotako.utils.Utils.*;

/**
 * Created by ^-_-^ on 30/04/2017 @ 21:40.
 **/

public class ListenerManager implements ActionListener, WindowListener, TreeSelectionListener
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static JPanel refCardContainer;
    private static JTree refLeTree;
    private static boolean initialized = false;
    private String qMessage, qTitle;

    void setupListener(JPanel cardContainer, JTree leTree)
    {
        if (initialized)
            return;

        ResourceBundle myResources = ResourceBundle.getBundle("localization", Locale.getDefault());
        qMessage = myResources.getString("qMessage");
        qTitle = myResources.getString("qTitle");

        refCardContainer = cardContainer;
        refLeTree = leTree;

        initialized = true;
    }

    private void saveOptions()
    {
        // saving data
        Properties p = new Properties();
        MainForm.StuffToSave stuff = ((MainForm)this).getWindowData();
        p.setProperty("windowX", Integer.toString(stuff.windowPosition[0]));
        p.setProperty("windowY", Integer.toString(stuff.windowPosition[1]));
        p.setProperty("ip", stuff.ip);
        p.setProperty("port", stuff.port);
        p.setProperty("mtod", stuff.mtod);
        try
        {
            p.store(new FileWriter("Server.properties", false), "Chatella Server v " + chatellaVersion);
        } catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

    private void loadOptions()
    {
        // load data
        Properties p = new Properties();
        StuffToSave stuff = new StuffToSave(((MainForm)this)); // derp
        try
        {
            p.load(new FileReader("Server.properties"));
        } catch (IOException e)
        {
            // FileNotFoundException lets ignore it
        }
        stuff.windowPosition[0] = Integer.parseUnsignedInt(p.getProperty("windowX", "9001"));
        stuff.windowPosition[1] = Integer.parseUnsignedInt(p.getProperty("windowY", "9001"));
        stuff.ip = p.getProperty("ip", "0.0.0.0");
        stuff.port = p.getProperty("port", "55561");
        stuff.mtod = p.getProperty("mtod", "Chatella Server [v " + MainForm.chatellaVersion + "]\r\nWelcome to Chatella network.");
        ((MainForm)this).setWindowData(stuff);
    }

    // ****************************  ActionListener ++
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        System.out.println("actionPerformed > " + actionEvent.getActionCommand());

        switch (actionEvent.getActionCommand())
        {
            case cSTART:
            {
                // ((JButton) e.getSource()).setEnabled(false);
                // TODO: start server
                break;
            }
            case cSTOP:
            {
                // TODO: ask question, if ok stop server
                break;
            }
            case cLOGDIR:
            {
                try
                {
                    Desktop.getDesktop().open(new File("Logs/"));
                } catch (Exception eX)
                {
                    LOG.log(Utils.L_ERR, "Opening Logs direcory", eX);
                }
                break;
            }
            case cEXIT:
            {
                // TODO: ask qeustion then if ok stop server and exit
                // if close to tray
                // ((MainForm)this).mainWindow.setState(Frame.ICONIFIED);

                // else
                // ask if want to close
                ImageIcon ico = new ImageIcon(UserInterfaceStuff.class.getClass().getResource("/img/door.png"));
                if (JOptionPane.showConfirmDialog(null, qMessage, qTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, ico) == JOptionPane.NO_OPTION)
                {
                    return;
                }
                // want to close
                ((MainForm)this).stopServer();
                saveOptions();
                System.exit(0);
                break;
            }
            case cHELP:
            {
                try
                {
                    Desktop.getDesktop().browse(new URI("http://www.tikotako.tk"));
                } catch (Exception eX)
                {
                    LOG.log(Utils.L_ERR, "Opening browser [help]", eX);
                }
                break;
            }
            case cABOUT:
            {
                // TODO: Generate about
                break;
            }
        }
    }
    // ****************************  ActionListener --


    // ****************************  WindowListener ++

    /**
     * Invoked the first time a window is made visible.
     */
    @Override
    public void windowOpened(WindowEvent e)
    {
        // TODO add load config
        loadOptions();
    }

    /**
     * Invoked when the user attempts to close the window
     * from the window's system menu.
     */
    @Override
    public void windowClosing(WindowEvent e)
    {
        this.actionPerformed(new ActionEvent(this, 0, cEXIT));
    }

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     */
    @Override
    public void windowClosed(WindowEvent e)
    {
    }

    /**
     * Invoked when a window is changed from a normal to a
     * minimized state. For many platforms, a minimized window
     * is displayed as the icon specified in the window's
     * iconImage property.
     *
     * @see java.awt.Frame#setIconImage
     */
    @Override
    public void windowIconified(WindowEvent e)
    {
        //
    }

    /**
     * Invoked when a window is changed from a minimized
     * to a normal state.
     */
    @Override
    public void windowDeiconified(WindowEvent e)
    {

    }

    /**
     * Invoked when the Window is set to be the active Window. Only a Frame or
     * a Dialog can be the active Window. The native windowing system may
     * denote the active Window or its children with special decorations, such
     * as a highlighted title bar. The active Window is always either the
     * focused Window, or the first Frame or Dialog that is an owner of the
     * focused Window.
     */
    @Override
    public void windowActivated(WindowEvent e)
    {
    }

    /**
     * Invoked when a Window is no longer the active Window. Only a Frame or a
     * Dialog can be the active Window. The native windowing system may denote
     * the active Window or its children with special decorations, such as a
     * highlighted title bar. The active Window is always either the focused
     * Window, or the first Frame or Dialog that is an owner of the focused
     * Window.
     */
    @Override
    public void windowDeactivated(WindowEvent e)
    {
    }

    // ****************************  WindowListener --

    // ****************************  TreeSelectionListener ++
    @Override
    public void valueChanged(TreeSelectionEvent e)
    {
        int currentRow = refLeTree.getRowForPath(e.getPath());

        switch (currentRow)
        {
            case 1:
            {
                ((CardLayout) (refCardContainer.getLayout())).show(refCardContainer, "manServerCard");
                break;
            }
            case 2:
            {
                ((CardLayout) (refCardContainer.getLayout())).show(refCardContainer, "manUsersCard");
                break;
            }
            case 3:
            {
                ((CardLayout) (refCardContainer.getLayout())).show(refCardContainer, "manChannelsCard");
                break;
            }
            case 4:
            {
                ((CardLayout) (refCardContainer.getLayout())).show(refCardContainer, "manLogsCard");
                break;
            }
            case 6:
            {

                ((CardLayout) (refCardContainer.getLayout())).show(refCardContainer, "logServerCard");
                break;
            }
            case 7:
            {
                ((CardLayout) (refCardContainer.getLayout())).show(refCardContainer, "infoCard");
                break;
            }
        }
    }
    // ****************************  TreeSelectionListener ++
}
