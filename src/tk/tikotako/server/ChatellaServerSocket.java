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

import tk.tikotako.utils.logger.TheLogger;

import java.util.logging.*;

/**
 * Created by ^-_-^ on 25/04/2017 @ 15:26.
 **/

public class ChatellaServerSocket
{
    // use the classname for the logger, this way you can refactor
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    public void doSomeThingAndLog() {
        // ... more code

        // now we demo the logging


        LOG.severe("Info Logsevere");
        LOG.warning("Info Logwarning");
        LOG.info("Info Loginfo");
        LOG.finest("Really not important");

    }

    public static void main(String[] args)
    {
        TheLogger.setup();
        (new ChatellaServerSocket()).doSomeThingAndLog();

        try
        {
            int i = Integer.parseInt("potato");
        }   catch (Exception e)
        {
            LOG.log(Level.SEVERE, "derp", e);
        }
    }
}
