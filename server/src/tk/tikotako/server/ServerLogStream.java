package tk.tikotako.server;

import tk.tikotako.utils.Utils;

import java.awt.*;
import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.BadLocationException;

import static tk.tikotako.utils.Utils.errorMessage;
import static tk.tikotako.utils.Utils.huegID;

/**
 * Created by ^-_-^ on 06/05/2017 @ 20:54.
 **/

class ServerLogStream extends OutputStream
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Style style;
    private JTextPane textPane;
    private StyledDocument doc;
    private static FileWriter log_sama;
    private static boolean logToFile = false; // shared with all instances

    ServerLogStream(JTextPane jTextPanel, boolean normalColor)
    {
        textPane = jTextPanel;
        doc = textPane.getStyledDocument();
        if (normalColor)
        {
            style = textPane.addStyle("BLU", null);
            StyleConstants.setForeground(style, Color.blue);
        }
        else
        {
            style = textPane.addStyle("ROSSO", null);
            StyleConstants.setForeground(style, Color.red);
        }
    }

    static void doLogToFile(boolean doWant)
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

    static void log(String text)
    {
        String naw = (new SimpleDateFormat("HH:MM:SS")).format(new Time(System.currentTimeMillis()));
        System.out.printf("[%s] %s", naw, text);
    }

    static void log(String format, Object ... args)
    {
        String naw = (new SimpleDateFormat("HH:MM:SS")).format(new Time(System.currentTimeMillis()));
        Object [] obei = new Object[args.length + 1];
        obei[0] = naw;
        System.arraycopy(args, 0, obei, 1, args.length);
        System.out.printf("[%s] " + format, obei);
    }

    static void err(String text)
    {
        System.err.printf("LOG > %s", text);
    }

    static void err(String format, Object ... args)
    {
        System.err.printf(format, args);
    }

    @Override
    public void write(int b) throws IOException
    {
        String tmpSTR =  String.valueOf((char) b);

        if (logToFile)
        {
            log_sama.append(tmpSTR);
            log_sama.flush();
        }

        try
        {
            doc.insertString(doc.getLength(), tmpSTR, style);
        } catch (BadLocationException e)
        {
            //
        }
        textPane.setCaretPosition(textPane.getDocument().getLength());
    }
}
