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

import tk.tikotako.server.ChatellaServer;
import tk.tikotako.server.ChatellaServerSocket;

import java.io.Console;
import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

    static private void disableLog(Handler _wich)
    {
        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        System.out.println(Arrays.toString(handlers));
        if (_wich.getClass().isInstance(handlers[0]))
        {
            rootLogger.removeHandler(handlers[0]);
            System.out.println("brb " + _wich);
        }   else
        {
            System.out.println("WUT " + _wich);
        }
    }

    private static void enableLogConsole()
    {
        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.ALL);
        logger.addHandler(new ConsoleHandler());
        System.out.println("HALO CONZOL");

    }

    static private void enableLogToFile()
    {
        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        try
        {
            //noinspection ResultOfMethodCallIgnored
            new File("Log").mkdir(); // r ignored (true, false)

            FileHandler fileTxt = new FileHandler("Log\\" + (new SimpleDateFormat("dd-MM-yyyy")).format(new Date(System.currentTimeMillis())) + ".txt", true);

            // create a TXT formatter
            TheFormatter formatterTxt = new TheFormatter();
            fileTxt.setFormatter(formatterTxt);

            logger.setLevel(Level.ALL);
            logger.addHandler(fileTxt);
        }   catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static public void start()
    {

        enableLogToFile();

        Logger rootLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        Handler[] handlers = rootLogger.getHandlers();
        System.out.println(Arrays.toString(handlers));

        disableLog(new ConsoleHandler());

        handlers = rootLogger.getHandlers();
        System.out.println(Arrays.toString(handlers));
    }

    static public void stop(boolean _switchToConsole)
    {
        if (_switchToConsole) enableLogConsole();
        try
        {
            disableLog(new FileHandler());
        }   catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
