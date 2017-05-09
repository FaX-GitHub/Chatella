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

package tk.tikotako.utils;

import javax.swing.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by ^-_-^ on 25/04/2017 @ 19:42.
 *  <br>
 *  <br>
 *  Utility class.
 **/

public class Utils
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static final ImageIcon errorIco = new ImageIcon(Utils.class.getClass().getResource("/img/error.png"));

    private static final String regex = "\\b((25[0–5]|2[0–4]\\d|[01]?\\d\\d?)(\\.)){3}(25[0–5]|2[0–4]\\d|[01]?\\d\\d?)\\b";

    public static final Level L_ERR = Level.SEVERE;
    public static final Level L_INF = Level.INFO;
    public static final Level L_WAR = Level.WARNING;

    public enum LogType { CONSOLE, FILE, CONFILE }

    public enum ServerSatus { RUNNING, STOPPED }

    public enum ActionCommands
    {
        FAIL ("acFAIL"),
        EXIT("acExit"),
        EXITX("acExit2lavendetta"),
        START("acStart"),
        STOP("acStop"),
        LOGDIR("acLogDir"),
        HELP("acHalpMiCarPliz"),
        ABOUT("acAbbabbebebebbebbaechebabachubebbe");

        private final String value;

        ActionCommands(String value)
        {
            this.value = value;
        }

        public static ActionCommands toCommand(String sCommand)
        {
            for (ActionCommands actionCommand : ActionCommands.values())
            {
                if (actionCommand.value.equalsIgnoreCase(sCommand))
                {
                    return actionCommand;
                }
            }
            return FAIL;
        }

        @Override
        public String toString()
        {
            return value;
        }
    }

    /**
     *  Simple way to generate an error window
     * @param title Dialog title
     * @param msg   Dialog message
     */
    public static void errorMessage(String title, String msg)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE, errorIco);
    }

    /**
     *  Simple way to generate an error window
     * @param title Dialog title
     * @param e     Exception
     */
    public static void errorMessage(String title, Exception e)
    {
        JOptionPane.showMessageDialog(null, e.getMessage(), title, JOptionPane.ERROR_MESSAGE, errorIco);
    }

    /**
     *  Simple way to generate an error window with "ERROR." title
     * @param e     Exception
     */
    public static void errorMessage(Exception e)
    {
        errorMessage("ERROR.", e.getMessage());
    }

    /**
     *  Simple way to generate an error window with "ERROR." title
     * @param msg   Dialog message
     */
    public static void errorMessage(String msg)
    {
        errorMessage("ERROR.", msg);
    }

    /**
     *  Output a socketAddress in IP : PORT format
     *
     * @param socketAddress socketAddress
     * @return              String
     */
    public static String formatSockAddr(SocketAddress socketAddress)
    {
        return ((InetSocketAddress)socketAddress).getHostString() + " : " + Integer.toUnsignedString(((InetSocketAddress)socketAddress).getPort());
    }

    /**
     *  Validate an ipv4 address
     *
     * @param ipAddress The IP 0.0.0.0 to 255.255.255.255
     * @return          true if is valid false if not
     */
    public static boolean validateIP(String ipAddress)
    {
        return Pattern.matches(regex, ipAddress);
    }

}
