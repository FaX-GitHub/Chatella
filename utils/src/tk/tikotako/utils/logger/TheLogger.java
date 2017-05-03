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

import tk.tikotako.utils.logger.TheFormatter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

import static tk.tikotako.utils.Utils.*;

/**
 * Created by ^-_-^ on 25/04/2017 @ 16:27.
 * <p>
 * <p>
 * Modified copypasta from http://www.vogella.com/tutorials/Logging/article.html
 */

public class TheLogger
{
    static private void disableLog(Class _wich)
    {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        if (logger.getHandlers().length > 0)
        {
            for (Handler _handler : logger.getHandlers())
            {
                try
                {
                    if (_wich.isInstance(_handler))
                    {
                        logger.removeHandler(_handler);
                        return;
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void enableLogConsole()
    {
        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.ALL);
        logger.addHandler(new ConsoleHandler());
    }

    static private void enableLogToFile()
    {
        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        try
        {
            //noinspection ResultOfMethodCallIgnored
            new File("Logs").mkdir(); // r ignored (true, false)

            FileHandler fileTxt = new FileHandler("Logs\\" + (new SimpleDateFormat("dd-MM-yyyy")).format(new Date(System.currentTimeMillis())) + ".txt", true);

            // create a TXT formatter
            TheFormatter formatterTxt = new TheFormatter();
            fileTxt.setFormatter(formatterTxt);

            logger.setLevel(Level.ALL);
            logger.addHandler(fileTxt);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static public void start(int _ConsoleOrFile)
    {
        // TODO: check if there is already the console/file output so don't add another one
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setUseParentHandlers(false);
        switch (_ConsoleOrFile)
        {
            case CONSOLE:
            {
                enableLogConsole();
                break;
            }
            case FILE:
            {
                enableLogToFile();
                break;
            }
            case CONFILE:
            {
                enableLogConsole();
                enableLogToFile();
                break;
            }
        }
    }

    static public void stop(int _ConsoleOrFile)
    {
        switch (_ConsoleOrFile)
        {
            case CONSOLE:
            {
                disableLog(ConsoleHandler.class);
                break;
            }
            case FILE:
            {
                disableLog(FileHandler.class);
                break;
            }
            case CONFILE:
            {
                disableLog(ConsoleHandler.class);
                disableLog(FileHandler.class);
                break;
            }
        }
    }

}
