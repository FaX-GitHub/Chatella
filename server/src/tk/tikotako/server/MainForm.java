package tk.tikotako.server;

import tk.tikotako.utils.Utils;
import tk.tikotako.utils.logger.TheLogger;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.logging.Logger;

import static tk.tikotako.server.UserInterfaceStuff.changeLookAndFeel;
import static tk.tikotako.server.UserInterfaceStuff.makeMainWindow;
import static tk.tikotako.utils.Utils.errorMessage;
import static tk.tikotako.server.ServerLogStream.*;

/**
 * Created by ^-_-^ on 27/04/2017 @ 20:37.
 **/

public class MainForm extends ListenerManager
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    final static String chatellaVersion = "1.0";

    private JPanel manUsersPanel;
    private JPanel manChannelsPanel;
    private JPanel manLogsPanel;
    private JPanel logServerPanel;
    private JPanel infoPanel;
    private JLabel leLabel;
    private JPanel manServerPanel;
    private JPanel centerPanel;
    private JToolBar leToolBar;
    private JEditorPane InfoEditorPanel;
    private JMenuBar menuBar;
    private JPanel mainPanel;
    private JPanel cardContainer;
    private JTree leTree;
    private JPanel statusPanel;
    private JSpinner portSpinner;
    private JTextField ipField;
    private JTextArea motdArea;
    private JCheckBox closeToTrayCheckBox;
    private JTextPane logOutput;
    private JCheckBox logToFileCheckBox;
    private JFrame mainWindow;

    // ****************************  Get & set ++

    boolean getCloseToTray()
    {
        return closeToTrayCheckBox.isSelected();
    }

    boolean getLogToFile()
    {
        return logToFileCheckBox.isSelected();
    }

    JFrame getWindow()
    {
        return mainWindow;
    }

    int [] getWinPos()
    {
        return new int [] { (int)mainWindow.getLocation().getX(), (int)mainWindow.getLocation().getY() };
    }

    String getIp()
    {
        return ipField.getText();
    }

    String getPort()
    {
        return portSpinner.getValue().toString();
    }

    String getMotd()
    {
        return motdArea.getText();
    }

    /////// set

    void setCloseToTray(boolean potato)
    {
        closeToTrayCheckBox.setSelected(potato);
    }

    void setLogToFile(boolean potato)
    {
        logToFileCheckBox.setSelected(potato);
    }

    void setWinPos(int X, int Y)
    {
        if ((X != 9001) && (Y != 9001))
        {
            mainWindow.setLocation(X, Y);
        } else
        {
            mainWindow.setLocationRelativeTo(null);
        }
    }

    void setIp(String iP)
    {
        ipField.setText(iP);
    }

    void setPort(String pORT)
    {
        portSpinner.setValue(Integer.valueOf(pORT));
    }

    void setMotd(String mOTD)
    {
        motdArea.setText(mOTD);
    }

    // ****************************  Get & Set --

    private MainForm()
    {
        TheLogger.start(Utils.LogType.FILE);
        LOG.log(Utils.L_INF, "Initiating startup sequence.");

        // Show loading image, is set in the build jar option page and the manifest file
        SplashScreen.getSplashScreen();

        mainWindow = makeMainWindow(this, mainPanel, menuBar);
        this.setup(cardContainer, leTree, this);
        changeLookAndFeel(mainWindow);
        mainWindow.setVisible(true);
        this.loadOptions();

        // TODO remove
        ((CardLayout) (cardContainer.getLayout())).show(cardContainer, "logServerCard");
    }

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(() ->
        {
            MainForm mf = new MainForm();
        });
    }

    void startServer()
    {
        // TODO check server running (Y)check client connected (Y) sendmessagetoclients - close
        System.out.printf("%s\r\n", "startin");
        leToolBar.getComponent(0).setEnabled(false); // 0 btn start
        leToolBar.getComponent(1).setEnabled(true); // 1 btn stop
    }

    void stopServer()
    {
        // TODO check server running (Y)check client connected (Y) sendmessagetoclients - close
        System.out.printf("%s\r\n", "stoppin");
        leToolBar.getComponent(0).setEnabled(true);
        leToolBar.getComponent(1).setEnabled(false);
    }

    private void createUIComponents()
    {
        // TODO: place custom component creation code here
        UserInterfaceStuff.loadLocalized();
        leTree = UserInterfaceStuff.leTreeSetup(this);
        menuBar = UserInterfaceStuff.standardMenuSetup(this);
        leToolBar = UserInterfaceStuff.leToolBarSetup(this);

        portSpinner = new JSpinner();
        ipField = new JTextField();
        motdArea = new JTextArea();

        System.out.printf("%s", "HURRRRRRRRRRRRR");
        logOutput = new JTextPane();
        logOutput.setEditable(false);

        System.setOut(new PrintStream(new ServerLogStream(logOutput, true)));
        System.setErr(new PrintStream(new ServerLogStream(logOutput, false)));

        doLogToFile(true);
        log("%s %d %s", "PIZZA", 1234, "\r\n");
        err("MANDOLINO\r\n");

        doLogToFile(false);
        err("PIZZA 2\r\n");
        err("MANDOLINO 2\r\n");

        doLogToFile(true);
        log( "PIZZA 3\r\n");
        err("MANDOLINO 3\r\n");

        // setup the Info panel with an HTML and hyperlink event listener
        InfoEditorPanel = new JEditorPane();
        InfoEditorPanel.setEditable(false);
        InfoEditorPanel.setOpaque(false);
        //noinspection Convert2Lambda
        InfoEditorPanel.addHyperlinkListener(new HyperlinkListener()
        {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent hle)
            {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType()))
                {
                    try
                    {
                        Desktop.getDesktop().browse(hle.getURL().toURI());
                    } catch (Exception eX)
                    {
                        LOG.log(Utils.L_ERR, "Info panel, click on hyperlink ", eX);
                        errorMessage(eX);
                    }
                }
            }
        });
        InfoEditorPanel.setContentType("text/html");
        StringBuilder infoHTML = new StringBuilder();
        URL imgURL = MainForm.class.getClassLoader().getResource("img/loading.gif");
        infoHTML.append("<html>");
        infoHTML.append("<h1><img src=\"");
        infoHTML.append(imgURL!=null?imgURL.toString():"");
        infoHTML.append("\" alt=\"LOGO\" height=\"120\" width=\"120\" />");
        infoHTML.append("Chatella server ");
        infoHTML.append(chatellaVersion);
        infoHTML.append("</h1><h2>Project web site <a href=\"http://www.tikotako.tk\" target=\"_blank\">here</a></h2>");
        infoHTML.append("<h2>Project page on GitHub <a href=\"https://github.com/FaX-GitHub/Chatella\" target=\"_blank\">here</a></h2>");
        infoHTML.append("<p>Chatella is a free project started by ^-_-^</p>");
        infoHTML.append("</html>");
        InfoEditorPanel.setText(infoHTML.toString());
    }
}
