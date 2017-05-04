package tk.tikotako.server;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import java.text.Format;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import static tk.tikotako.server.UserInterfaceStuff.changeLookAndFeel;
import static tk.tikotako.server.UserInterfaceStuff.makeMainWindow;
import static tk.tikotako.utils.Utils.errorMessage;
import tk.tikotako.utils.logger.TheLogger;
import tk.tikotako.utils.Utils;

/**
 * Created by ^-_-^ on 27/04/2017 @ 20:37.
 **/

public class MainForm extends ListenerManager
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    final static String chatellaVersion = "1.0";

    // private
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
    private JFrame mainWindow;

    static class StuffToSave
    {
        int [] windowPosition;
        String ip, port, mtod;

        StuffToSave(MainForm who)
        {
            windowPosition =  new int [] { 9001, 9001 };
            if (who.mainWindow.isShowing())
            {
                windowPosition =  new int [] { (int)who.mainWindow.getLocation().getX(), (int)who.mainWindow.getLocation().getY() };
            }
            port = who.portSpinner.getValue().toString();
            ip = who.ipField.getText();
            mtod = who.motdArea.getText();
        }
    }

    StuffToSave getWindowData()
    {
        return new StuffToSave(this);
    }

    void setWindowData(StuffToSave loadedStuff)
    {
        motdArea.setText(loadedStuff.mtod);
        ipField.setText(loadedStuff.ip);
        portSpinner.setValue(Integer.valueOf(loadedStuff.port));

        if ((loadedStuff.windowPosition[0] != 9001) && (loadedStuff.windowPosition[1] != 9001))
        {
            mainWindow.setLocation(loadedStuff.windowPosition[0], loadedStuff.windowPosition[1]);
        } else
        {
            mainWindow.setLocationRelativeTo(null);
        }
    }

    private MainForm()
    {
        TheLogger.start(Utils.CONFILE);
        LOG.log(Utils.L_INF, "Initiating startup sequence.");

        // Show loading image, is set in the build jar option page and the manifest file
        SplashScreen.getSplashScreen();

        mainWindow = makeMainWindow(this, mainPanel, menuBar);
        changeLookAndFeel(mainWindow);
        setupListener(cardContainer, leTree); // ListenerManager
    }

    public static void main(String[] args)
    {

        // javax.swing.SwingUtilities.invokeLater(new Runnable() { @Override public void run() { new MainForm(); } });
        javax.swing.SwingUtilities.invokeLater(MainForm::new);
    }

    void stopServer()
    {
        // TODO check server running (Y)check client connected (Y) sendmessagetoclients - close
        System.out.printf("%s", "stoppin");
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

        // setup the Info panel with an HTML and hyperlink event listener
        InfoEditorPanel = new JEditorPane();
        InfoEditorPanel.setEditable(false);
        InfoEditorPanel.setOpaque(false);
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
