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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ^-_-^ on 25/04/2017 @ 19:42.
 **/

public class Utils
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public final static Level L_ERR = Level.SEVERE;
    public final static Level L_INF = Level.INFO;
    public final static Level L_WAR = Level.WARNING;

    public final static int CONSOLE = 9001;
    public final static int FILE    = 9002;
    public final static int CONFILE = 9003;

    public final static int nullint = -1;

    public final static String cEXIT = "acExit";
    public final static String cSTART= "acStart";
    public final static String cSTOP= "acStop";
    public final static String cLOGDIR = "acLogDir";
    public final static String cNULL = "-";
    public final static String cHELP = "acHalpMiCarPliz";
    public final static String cABOUT = "acAbbabbebebebbebbaechebabachubebbe";

    public static void errorMessage(Exception e)
    {
        ImageIcon ico = new ImageIcon(Utils.class.getClass().getResource("/img/error.png"));
        JOptionPane.showMessageDialog(null, e.getMessage(),"ERROR."+e.getMessage(), JOptionPane.ERROR_MESSAGE, ico);
    }
}
