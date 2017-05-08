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

package tk.tikotako.server;

import java.awt.*;
import javax.swing.*;
import java.sql.Time;
import java.util.Date;
import java.io.FileWriter;
import javax.swing.text.Style;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import static tk.tikotako.utils.Utils.errorMessage;
import static tk.tikotako.utils.Utils.*;
import tk.tikotako.utils.Utils;

/**
 * Created by ^-_-^ on 06/05/2017 @ 20:54.
 * <br>
 *              Class that output the logs on a given JTextPane and on a file (if is set to).<br>
 *              The file output is located in the /Logs/ directory and the filename is in this format:<br>
 *              "Server [DD-MM-YYYY].txt"
 *
 **/

class ServerLog
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Style nStyle, tStyle, iStyle, eStyle;
    private JTextPane textPane;
    private StyledDocument doc;
    private FileWriter log_sama;
    private boolean logToFile = false;

    /**
     *  Create the ServerLog Object that manage the log outputs.
     *
     * @param jTextPanel    the JTextPane that will be used to show the log.
     */
    ServerLog(JTextPane jTextPanel)
    {
        textPane = jTextPanel;
        doc = textPane.getStyledDocument();

        nStyle = textPane.addStyle("normal", null);
        StyleConstants.setForeground(nStyle, Color.blue);

        eStyle= textPane.addStyle("error", null);
        StyleConstants.setForeground(eStyle, Color.red);
        StyleConstants.setBold(eStyle, true);

        iStyle = textPane.addStyle("info", null);
        StyleConstants.setForeground(iStyle, Color.pink);
        StyleConstants.setBold(iStyle, true);

        tStyle = textPane.addStyle("time", null);
        StyleConstants.setForeground(tStyle, Color.black);
        StyleConstants.setBold(tStyle, true);
    }

    /**
     *  Allow or disallow the file output.
     *
     * @param doWant    If set to <b>false</b> the log output only in the JTextPane, if set to <b>true</b> the output will be also saved in a text file.
     */
    void logToFile(boolean doWant)
    {
        if ((logToFile != doWant) && (doWant))
        {
            String fileName = "Logs\\Server [" + (new SimpleDateFormat("dd-MM-yyyy")).format(new Date(System.currentTimeMillis())) + "].txt";
            try
            {
                log_sama = new FileWriter(fileName,true);
                logToFile = true;
            } catch (Exception e)
            {
                LOG.log(Utils.L_ERR, "log_sama = new FileWriter(fileName,true); >>> " + fileName, e);
                errorMessage(e);
            }
        } else if (logToFile != doWant)
        {
            try
            {
                log_sama.close();
                logToFile = false;
            } catch (Exception e)
            {
                LOG.log(Utils.L_ERR, "log_sama.close();", e);
                errorMessage(e);
            }
        }
    }

    /**
     *  Output the given text with normal style.
     * @param text text to log
     */
    void log(String text)
    {
        javax.swing.SwingUtilities.invokeLater(()->write(getNaw(), ServerLogType.NORMAL,  text + "\r\n"));
    }

    /**
     *  Output the given text with error style.
     * @param text text to log
     */
    void err(String text)
    {
        javax.swing.SwingUtilities.invokeLater(()->write(getNaw(), ServerLogType.ERROR,  text + "\r\n"));
    }

    /**
     *  Output the given text with info style.
     * @param text text to log
     */
    void inf(String text)
    {
        javax.swing.SwingUtilities.invokeLater(()->write(getNaw(), ServerLogType.INFO,  text + "\r\n"));
    }

    /**
     *  Get current time.
     * @return  Time formatted output as string.
     */
    private String getNaw()
    {
        return  (new SimpleDateFormat("[HH:MM:ss]")).format(new Time(System.currentTimeMillis()));
    }

    /**
     *  The real method that output the given text.
     * @param naw       time string in this format: [HH:MM:ss]
     * @param type      the output style type (normal, info or error)
     * @param tmpSTR    the text to output.
     */
    private void write(String naw, ServerLogType type, String tmpSTR)
    {
        Style style = nStyle;

        if (type.equals(ServerLogType.ERROR))
        {
            style = eStyle;
        } else if (type.equals(ServerLogType.INFO))
        {
            style = iStyle;
        }

        try
        {
            doc.insertString(doc.getLength(), naw + " > ", tStyle);
            doc.insertString(doc.getLength(), tmpSTR, style);
            textPane.setCaretPosition(textPane.getDocument().getLength());
        } catch (Exception e)
        {
            LOG.log(Utils.L_ERR, " doc.insertString(doc.getLength(), tmpSTR, style);", e);
            errorMessage(e);
        }

        if (logToFile)
        {
            try
            {
                log_sama.append(naw);
                log_sama.append(type.equals(ServerLogType.NORMAL)?"   > ":" ! > ");
                log_sama.append(tmpSTR);
                log_sama.flush();
            } catch (Exception e)
            {
                LOG.log(Utils.L_ERR, "log_sama.append(tmpSTR);", e);
                errorMessage(e);
            }
        }
    }

    @Override
    public String toString()
    {
        return "ServerLog {" + this.hashCode() + '}';
    }
}
