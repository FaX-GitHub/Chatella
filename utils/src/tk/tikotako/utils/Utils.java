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

    public static final Level L_ERR = Level.SEVERE;
    public static final Level L_INF = Level.INFO;
    public static final Level L_WAR = Level.WARNING;

    public enum LogType { CONSOLE, FILE, CONFILE };

    public enum ServerSatus { RUNNING, STOPPED };

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

        private String value;

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
        public String toString() {
            return value;
        }
    }

    /**
     *  Simple way to generate an error window
     * @param title
     * @param msg
     */
    public static void errorMessage(String title, String msg)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE, errorIco);
    }

    public static void errorMessage(String title, Exception e)
    {
        JOptionPane.showMessageDialog(null, e.getMessage(), title, JOptionPane.ERROR_MESSAGE, errorIco);
    }

    public static void errorMessage(Exception e)
    {
        errorMessage("ERROR.", e.getMessage());
    }

    public static void errorMessage(String err)
    {
        errorMessage("ERROR.", err);
    }

    public static String formatSockAddr(SocketAddress socketAddress)
    {
        return ((InetSocketAddress)socketAddress).getHostString() + " : " + Integer.toUnsignedString(((InetSocketAddress)socketAddress).getPort());
    }

}
