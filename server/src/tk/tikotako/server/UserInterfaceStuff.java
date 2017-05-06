package tk.tikotako.server;

import javax.swing.*;
import java.net.URL;
import java.util.Locale;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import static tk.tikotako.utils.Utils.*;
import tk.tikotako.utils.Utils;

/**
 * Created by ^-_-^ on 01/05/2017 @ 23:54.
 **/

class UserInterfaceStuff
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static boolean Loaded = false;

    // leTree strings
    private static String sRoot, sManage, sServer, sUser, sChannels, sLogs, sLogs2, sServer2, sInfo;
    // Menu strings
    private static String mFile, mExit, mHelp, mAbout;
    // leToolBar strings
    private static String tStartCap, tStopCap, tLogsCap, tExitCap, tStartTip, tStopTip, tLogsTip, tExitTip;

    static void loadLocalized()
    {
        if (Loaded)
        {
            return;
        }
        Loaded = true;
        // Load localized strings
        ResourceBundle myResources = ResourceBundle.getBundle("localization", Locale.getDefault());
        // leTree
        sRoot = myResources.getString("sRoot");
            sManage = myResources.getString("sManage");
                sServer = myResources.getString("sServer");
                sUser = myResources.getString("sUser");
                sChannels = myResources.getString("sChannels");
                sLogs = myResources.getString("sLogs");
            sLogs2 = myResources.getString("sLogs2");
                sServer2 = myResources.getString("sServer2");
            sInfo = myResources.getString("sInfo");
        // menu
        mFile = myResources.getString("M_File");
            mExit = myResources.getString("M_Exit");
        mHelp = myResources.getString("M_Help");
            mAbout = myResources.getString("M_About");
        // leToolBar
        tStartCap = myResources.getString("B_Start");
        tStopCap = myResources.getString("B_Stop");
        tLogsCap = myResources.getString("B_LogDir");
        tExitCap = myResources.getString("B_Exit");
        tStartTip = myResources.getString("B_StartToolTip");
        tStopTip = myResources.getString("B_StopToolTip");
        tLogsTip = myResources.getString("B_LogDirToolTip");
        tExitTip = myResources.getString("B_ExitToolTip");
    }

    static void changeLookAndFeel(JFrame mainWindow)
    {
        UIManager.LookAndFeelInfo looks[] = UIManager.getInstalledLookAndFeels();
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(mainWindow);
        } catch (Exception exception)
        {
            // don't care
        }
    }

    static JFrame makeMainWindow(ListenerManager listenerManager, JPanel mainPanel, JMenuBar menuBar)
    {
        JFrame mainWindow = new JFrame("Chatella server [v " + MainForm.chatellaVersion + "]");

        mainWindow.setJMenuBar(menuBar);

        mainWindow.addWindowListener(listenerManager);

        mainWindow.setContentPane(mainPanel);
        mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        ImageIcon ico = new ImageIcon(UserInterfaceStuff.class.getClass().getResource("/img/logo16.png"));
        mainWindow.setIconImage(ico.getImage());
        mainWindow.pack();

        mainWindow.setResizable(false);

        return mainWindow;
    }

    /** makeButan
     *          generate new JButtons with the respective image for the toolbar
     *          the caption and the toolTip are set in the GUI editor (values in /resource/localization)
     *
     * @param listenerManager   is used to set the ActionCommand of the button
     * @param imageName         the image name without the last two characters (that is "-e" for enabled images and "-d" for disabled images), extension and path
     * @param actionCommand     the ActionCommand that is sent to *listenerManager*  when the button is pressed
     * @param enabled           set the button state (clickable or not)
     * @return                  the new button
     */
    private static JButton makeButan(ListenerManager listenerManager, String caption, String toolTip, String imageName, String actionCommand, Boolean enabled)
    {
        JButton button = new JButton();
        button.setText(caption);
        button.setEnabled(enabled);
        button.addActionListener(listenerManager);
        button.setToolTipText(toolTip);
        button.setActionCommand(actionCommand);

        if (imageName != null)
        {
            URL enImageURL = UserInterfaceStuff.class.getResource("/img/" + imageName + "-e.png");
            URL disImageURL = UserInterfaceStuff.class.getResource("/img/" + imageName + "-d.png");

            if (enImageURL != null)
            {
                button.setIcon(new ImageIcon(enImageURL));
                if (disImageURL != null)
                {
                    button.setDisabledIcon(new ImageIcon(disImageURL));
                }
            } else
            {
                LOG.log(Utils.L_ERR, "Resource not found [" + imageName + "]");
            }
        }

        return button;
    }

    static JToolBar leToolBarSetup(ListenerManager listenerManager)
    {
        JToolBar potato = new JToolBar();
        potato.add(makeButan(listenerManager, tStartCap, tStartTip,"start", ActionCommands.START.toString(), true));
        potato.add(makeButan(listenerManager, tStopCap, tStopTip,"stop", ActionCommands.STOP.toString(), false));
        potato.addSeparator();
        potato.add(makeButan(listenerManager, tLogsCap, tLogsTip,"logdir", ActionCommands.LOGDIR.toString(), true));
        potato.addSeparator();
        potato.add(makeButan(listenerManager, tExitCap, tExitTip,"exit", ActionCommands.EXIT.toString(), true));
        return potato;
    }

    /** fastMenuItem
     *              Faster way to generate a menu item
     *
     * @param capt      Caption
     * @param keyMenu   The underline letter that the menu caption have
     * @param kE        key used to trigger the click on the menu item w/o have to click on it
     * @param aE        key used to trigger the click on the menu item w/o have to click on it
     * @param comm      Command, the command that is sent to the ActionListener
     * @param aL        the ActionListener
     * @return          the new menù item
     */
    private static JMenuItem fastMenuItem (String capt, int keyMenu, int kE, int aE, String comm, ActionListener aL)
    {
        JMenuItem r;
        if (keyMenu > -1)
        {
            r = new JMenuItem(capt, keyMenu);
        }
        else
        {
            r = new JMenuItem(capt);
        }

        if (kE > -1 && kE > -1)
        {
            r.setAccelerator(KeyStroke.getKeyStroke(kE, aE));
        }

        if (comm != null)
        {
            r.setActionCommand(comm);
            r.addActionListener(aL);
        }
        return r;
    }

    static JMenuBar standardMenuSetup(ListenerManager listenerManager)
    {
        JMenu menu;
        JMenuBar menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu(mFile);
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        menu.add(fastMenuItem(mExit, KeyEvent.VK_E, KeyEvent.VK_X, ActionEvent.CTRL_MASK + ActionEvent.ALT_MASK, ActionCommands.EXIT.toString(), listenerManager));

        //Build second menu in the menu bar.
        menu = new JMenu(mHelp);
        menu.setMnemonic(KeyEvent.VK_H);
        menu.add(fastMenuItem(mHelp, KeyEvent.VK_H, KeyEvent.VK_F1, 0, ActionCommands.HELP.toString(), listenerManager));
        menu.addSeparator();
        menu.add(fastMenuItem(mAbout, KeyEvent.VK_A, KeyEvent.VK_F12, 0, ActionCommands.ABOUT.toString(), listenerManager));
        menuBar.add(menu);

        return menuBar;
    }

    /** leTreeSetup
     *              setup the Tree by loading the strings and creating branches and leafs
     *              if add/remove/move a branch or a leaf update the Listener located in
     *              @see ListenerManager#valueChanged(TreeSelectionEvent)
     *              because the switch/case use the creation order to execute the correct code.
     *
     * @param listenerManager   listener of valueChanged event.
     * @return      the new tree
     */
    static JTree leTreeSetup(ListenerManager listenerManager)
    {
        // Creating root, branches 'n' leafs
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(sRoot);
        DefaultMutableTreeNode manageNode = new DefaultMutableTreeNode(sManage);
        manageNode.add(new DefaultMutableTreeNode(sServer));
        manageNode.add(new DefaultMutableTreeNode(sUser));
        manageNode.add(new DefaultMutableTreeNode(sChannels));
        manageNode.add(new DefaultMutableTreeNode(sLogs));
        DefaultMutableTreeNode logNode = new DefaultMutableTreeNode(sLogs2);
        logNode.add(new DefaultMutableTreeNode(sServer2));
        DefaultMutableTreeNode infoNode = new DefaultMutableTreeNode(sInfo);

        // Adding branches to the root
        rootNode.add(manageNode);
        rootNode.add(logNode);
        rootNode.add(infoNode);

        // setup the JTree
        JTree mrTree = new JTree(rootNode);
        mrTree.setRootVisible(false);
        mrTree.setShowsRootHandles(true);
        mrTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        for (int i = 0; i < mrTree.getRowCount(); i++) { mrTree.expandRow(i); }

        mrTree.addTreeSelectionListener(listenerManager);

        return mrTree;
    }
}
