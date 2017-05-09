package tk.tikotako.server;

import java.awt.*;
import java.net.URI;
import javax.swing.*;
import java.util.Locale;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import static tk.tikotako.server.MainForm.*;
import static tk.tikotako.utils.Utils.*;
import tk.tikotako.utils.Utils;

/**
 * Created by ^-_-^ on 30/04/2017 @ 21:40.
 **/

class ListenerManager implements ActionListener, WindowListener, TreeSelectionListener
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static JTree refLeTree;
    private static MainForm mainForm;
    private static JPanel refCardContainer;
    private String qMessage, qTitle, sMessage, sTitle;

    /**
     *  Setup the Listener class.
     *
     * @param cardContainer reference to MainForm.cardContainer
     * @param leTree        reference to MainForm.leTree
     * @param leMainForm    reference to MainForm
     */
    void setup(JPanel cardContainer, JTree leTree, MainForm leMainForm)
    {
        ResourceBundle myResources = ResourceBundle.getBundle("localization", Locale.getDefault());
        // Quit message
        qMessage = myResources.getString("qMessage");
        qTitle = myResources.getString("qTitle");
        // Stop server message
        sMessage = myResources.getString("sMessage");
        sTitle = myResources.getString("sTitle");

        refCardContainer = cardContainer;
        mainForm = leMainForm;
        refLeTree = leTree;
    }

    /**
     * Save options to file.
     * <br>
     * Is private because is used only here.
     */
    private void saveOptions()
    {
        // saving data
        Properties p = new Properties();
        int [] wp = mainForm.getWinPos();
        p.setProperty("windowX", Integer.toString(wp[0]));
        p.setProperty("windowY", Integer.toString(wp[1]));
        p.setProperty("ip", mainForm.getIp());
        p.setProperty("port", mainForm.getPort());
        p.setProperty("mtod", mainForm.getMotd());
        p.setProperty("closeToTray",  Boolean.toString(mainForm.getCloseToTray()));
        p.setProperty("logToFile",  Boolean.toString(mainForm.getLogToFile()));
        try
        {
            p.store(new FileWriter("Server.properties", false), "Chatella Server v " + chatellaVersion);
        } catch (Exception e)
        {
            LOG.log(Utils.L_ERR, ">> FileWriter <<", e);
        }
    }

    /**
     * Load options from file.
     * <br>
     * Is not private because is used in the MainForm class
     */
    void loadOptions()
    {
        // load data
        Properties p = new Properties();
        try
        {
            p.load(new FileReader("Server.properties"));
        } catch (IOException e)
        {
            // FileNotFoundException lets ignore it
        }
        mainForm.setCloseToTray(Boolean.valueOf(p.getProperty("closeToTray", "true")));
        mainForm.setLogToFile(Boolean.valueOf(p.getProperty("logToFile", "true")));
        mainForm.setIp(p.getProperty("ip", "0.0.0.0"));
        mainForm.setPort(p.getProperty("port", "55561"));
        mainForm.setMotd(p.getProperty("mtod", "Chatella Server [v " + MainForm.chatellaVersion + "]\r\nWelcome to Chatella network."));
        mainForm.setWinPos(Integer.parseUnsignedInt(p.getProperty("windowX", "9001")), Integer.parseUnsignedInt(p.getProperty("windowY", "9001")));
    }

    // ****************************  ActionListener ++
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        ActionCommands command = ActionCommands.toCommand(actionEvent.getActionCommand());
        switch (command)
        {
            case FAIL:
            {
                LOG.log(Utils.L_ERR, "ActionCommands.toCommand(String sCommand) >> Error Converting >> actionEvent.getActionCommand() = [" + actionEvent.getActionCommand() +"]");
                break;
            }
            case START:
            {
                mainForm.startServer();
                break;
            }
            case STOP:
            {
                ImageIcon ico = new ImageIcon(UserInterfaceStuff.class.getClass().getResource("/img/stop.png"));
                if (JOptionPane.showConfirmDialog(null, sMessage, sTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, ico) == JOptionPane.YES_OPTION)
                {
                    mainForm.stopServer();
                }
                break;
            }
            case LOGDIR:
            {
                try
                {
                    mainForm.sendMessageToAll("POTATO");
                    //TODO rimettere normale
                    //Desktop.getDesktop().open(new File("Logs/"));
                } catch (Exception eX)
                {
                    LOG.log(Utils.L_ERR, "Opening Logs direcory", eX);
                }
                break;
            }
            case EXIT: case EXITX:
            {
                if ((mainForm.getCloseToTray()) && (command == ActionCommands.EXITX)) // close to tray only if true and the click come from the [X] of the window
                {
                    mainForm.getWindow().setState(Frame.ICONIFIED);
                }   else
                {
                    ImageIcon ico = new ImageIcon(UserInterfaceStuff.class.getClass().getResource("/img/door.png"));
                    if (JOptionPane.showConfirmDialog(null, qMessage, qTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, ico) == JOptionPane.YES_OPTION)
                    {
                        mainForm.stopServer();
                        saveOptions();
                        System.exit(0);
                    }
                }
                break;
            }
            case HELP:
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
            case ABOUT:
            {
                ((CardLayout) (refCardContainer.getLayout())).show(refCardContainer, "infoCard");
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
        // INFO fast debug
        mainForm.startServer();
        ((CardLayout) (mainForm.getCardContainer().getLayout())).show(mainForm.getCardContainer(), "logServerCard");
    }

    /**
     * Invoked when the user attempts to close the window
     * from the window's system menu.
     */
    @Override
    public void windowClosing(WindowEvent e)
    {
        this.actionPerformed(new ActionEvent(this, 0, ActionCommands.EXITX.toString()));
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
    // ****************************  TreeSelectionListener --
}
