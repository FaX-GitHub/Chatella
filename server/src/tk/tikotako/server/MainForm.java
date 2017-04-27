package tk.tikotako.server;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by ^-_-^ on 27/04/2017 @ 20:37.
 **/

public class MainForm extends WindowAdapter // https://docs.oracle.com/javase/8/docs/api/java/awt/event/WindowAdapter.html
{
    private JPanel mainPanel;
    private JTree leTree;
    private JLabel statusLabel;
    private JToolBar leToolBar;
    private JPanel cardContainer;
    private JPanel cardOne;
    private JPanel cardTwo;
    private JButton button1;
    private JButton button2;
    private JFrame mainWindow;

    private MainForm()
    {
        // Show loading image, is set in the build jar option page and the manifest file
        SplashScreen.getSplashScreen();

        mainWindow = new JFrame("YABBA DABBA DOO");

        mainWindow.addWindowListener(this);

        mainWindow.setContentPane(mainPanel);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon ico = new ImageIcon("Res\\Logo\\16x16.png");
        mainWindow.setIconImage(ico.getImage());
        mainWindow.pack();

        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);

        UIManager.LookAndFeelInfo looks[] = UIManager.getInstalledLookAndFeels();

        String str = "";
        for (UIManager.LookAndFeelInfo nigga : looks) { str += nigga.getClassName() + "\r\n"; }
        JOptionPane.showMessageDialog(mainWindow, str, "Luks und fil", JOptionPane.ERROR_MESSAGE, ico);

        // frame.setState(Frame.ICONIFIED);



        button2.addActionListener(fronk -> ((CardLayout)(cardContainer.getLayout())).show(cardContainer, "Card2"));
        button1.addActionListener(fronk -> ((CardLayout)(cardContainer.getLayout())).show(cardContainer, "Card1"));

        leTree.addTreeSelectionListener(new TreeSelectionListener()
        {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                System.out.println("AAA > " + e);
            }
        });
    }

    static void renderSplashFrame(Graphics2D g, int frame)
    {
        final String[] comps = {"foo", "bar", "baz"};
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120, 140, 200, 40);
        g.setPaintMode();
        g.setColor(Color.RED);
        g.drawString("Loading " + comps[(frame / 5) % 3] + "...", 100, 100);
    }

    private static void startGUI()
    {
        MainForm a = new MainForm();
    }

    public static void main(String[] args)
    {

        // javax.swing.SwingUtilities.invokeLater( new Runnable() { public void run() { startGUI(); } } );
        javax.swing.SwingUtilities.invokeLater(MainForm::startGUI);
    }

    /**
     * Invoked when a window is in the process of being closed.
     * The close operation can be overridden at this point.
     */
    public void windowClosing(WindowEvent e)
    {
        // TODO add question
        // TODO add check if client > 0 then send quit message
        // TODO add save config
        /*
        DO_NOTHING_ON_CLOSE
Do not do anything when the user requests that the window close. Instead, the program should probably use a window listener that performs some other action in its windowClosing method.
HIDE_ON_CLOSE (the default for JDialog and JFrame)
Hide the window when the user closes it. This removes the window from the screen but leaves it displayable.
DISPOSE_ON_CLOSE (the default for JInternalFrame)
Hide and dispose of the window when the user closes it. This removes the window from the screen and frees up any resources used by it.
EXIT_ON_CLOSE (defined in the JFrame class)
Exit the application, using System.exit(0).
    TODO lurkare costruttore per settare quello che mi serve
         */
    }

    /**
     * Invoked when a window is iconified.
     */
    public void windowIconified(WindowEvent e)
    {
        // TODO icon tray stuff
    }

    /**
     * Invoked when a window is de-iconified.
     */
    public void windowDeiconified(WindowEvent e)
    {
        // TODO icon tray stuff
    }

    private void changeLookAndFeel()
    {
        UIManager.LookAndFeelInfo looks[] = UIManager.getInstalledLookAndFeels();

        for (UIManager.LookAndFeelInfo nigga : looks)
        {
            try
            {
                UIManager.setLookAndFeel(nigga.getClassName());
                SwingUtilities.updateComponentTreeUI(mainWindow);
            } catch (Exception exception)
            {
                JOptionPane.showMessageDialog(mainWindow, "Can't change look and feel", "Invalid PLAF", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createUIComponents()
    {
        // TODO: place custom component creation code here

        leTree = new JTree();
    }
}
