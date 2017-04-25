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

import java.util.Arrays;
import java.util.logging.Logger;

import static tk.tikotako.utils.Utils.*;

/**
 * Created by ^-_-^ on 25/04/2017 @ 18:15.
 **/

public class ChatellaServer
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args)
    {

        TheLogger.start();
        (new ChatellaServerSocket()).doSomeThingAndLog();

        try
        {
            int i = Integer.parseInt("potato");
        } catch (Exception e)
        {
            LOG.log(L_ERR, Arrays.toString(e.getStackTrace()), e);
        }


        TheLogger.stop(true);
        (new ChatellaServerSocket()).doSomeThingAndLog();

        try
        {
            int i = Integer.parseInt("potato");
        } catch (Exception e)
        {
            LOG.log(L_ERR, Arrays.toString(e.getStackTrace()), e);
        }
    }
}
