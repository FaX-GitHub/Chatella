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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Created by ^-_-^ on 25/04/2017 @ 16:27.
 **/

/**
 *  Modified copypasta from http://www.vogella.com/tutorials/Logging/article.html
 */

public class TheLogger
{
    static private FileHandler fileTxt;
    static private TheFormatter formatterTxt;

    static public void setup()
    {
        try
        {
            // get the global logger to configure it
            Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

            // suppress the logging output to the console
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();

            if (handlers[0] instanceof ConsoleHandler)
            {
                rootLogger.removeHandler(handlers[0]);
            }

            logger.setLevel(Level.ALL);

            new File("Log").mkdir();

            fileTxt = new FileHandler("Log\\" +
                                        (new SimpleDateFormat("dd-MM-yyyy")).format(new Date(System.currentTimeMillis())) +
                                        ".txt", true);

            // create a TXT formatter
            formatterTxt = new TheFormatter();
            fileTxt.setFormatter(formatterTxt);
            logger.addHandler(fileTxt);
        }   catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
