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
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ^-_-^ on 25/04/2017 @ 19:42.
 **/

public class Utils
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final ImageIcon errorIco = new ImageIcon(Utils.class.getClass().getResource("/img/error.png"));
    public static final String huegID = "uFYpRw_y[+tkr?@Wcq'KnOSX36k? X:CB_4yB-mtmQv~,Y07L1M;;/X!cxq`O!weBTa8SXaNy,|EiB#_26Dj)*9xrN%>[0*bC(Ar>$?qb,#4Z:- (U d2`9m)L^G%.&ft:ye7n{qi~`g9L}lCi19zvx?s8*~R^qD:'&fH|9c+s+mNa+/UG85#`;l70rrDQ}*~P' y%wilN!%M2LZOP*7fnjHz;;?R9VZl+vF{N'%.XM?";

    public static final Level L_ERR = Level.SEVERE;
    public static final Level L_INF = Level.INFO;
    public static final Level L_WAR = Level.WARNING;

    public enum LogType { CONSOLE, FILE, CONFILE };

    public enum ServerLogType { NORMAL, ERROR };

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

    public static void errorMessage(Exception e)
    {
        JOptionPane.showMessageDialog(null, e.getMessage(),"ERROR.", JOptionPane.ERROR_MESSAGE, errorIco);
    }
}
