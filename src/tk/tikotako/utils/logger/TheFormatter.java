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

package tk.tikotako.utils.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by ^-_-^ on 25/04/2017 @ 16:39.
 **/

/**
 * Modified copypasta from http://www.vogella.com/tutorials/Logging/article.html
 */

class TheFormatter extends Formatter
{
    // this method is called for every log records
    public String format(LogRecord rec)
    {
        StringBuilder buf = new StringBuilder(1000);

        buf.append(rec.getLevel());
        buf.append("\t");
        buf.append(calcDate(rec.getMillis()));
        buf.append("\r\n\t");
        buf.append(rec.getSourceClassName());
        buf.append("\r\n\t");
        buf.append(rec.getSourceMethodName());
        buf.append("\r\n\t");
        buf.append(rec.getMessage());
        buf.append("\r\n\t");
        buf.append(rec.getParameters());
        buf.append("\r\n\t");
        buf.append(rec.getThrown());
        buf.append("\r\n");

        return buf.toString();
    }

    private String calcDate(long millisecs)
    {
        SimpleDateFormat date_format = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }

    // this method is called just after the handler using this
    // formatter is created
    public String getHead(Handler h)
    {
        return "LOGGIN START " + calcDate(System.currentTimeMillis()) + "\r\n";
    }

    // this method is called just after the handler using this
    // formatter is closed
    public String getTail(Handler h)
    {
        return "LOGGIN END " + calcDate(System.currentTimeMillis()) + "\r\n\r\n";
    }
}