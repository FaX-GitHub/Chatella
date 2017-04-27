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

import tk.tikotako.utils.Utils;
import tk.tikotako.utils.logger.TheLogger;

import java.util.logging.Logger;


/**
 * Created by ^-_-^ on 25/04/2017 @ 18:15.
 **/

public class Console
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args)
    {
        System.out.println("CONSOLE ON");
        TheLogger.start(Utils.CONSOLE);
        LOG.log(Utils.L_INF, "Y HALO THAR CONSOLE");
        (new ChtlServSock()).doSomeThingAndLog();
        try { int i = Integer.parseInt("potato"); } catch (Exception e) { LOG.log(Utils.L_ERR, null, e); }


        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        System.out.println("FILE ON");
        TheLogger.start(Utils.FILE);
        LOG.log(Utils.L_INF, "Y HALO THAR CONSOLE + FILE");
        (new ChtlServSock()).doSomeThingAndLog();
        try { int i = Integer.parseInt("potato"); } catch (Exception e) { LOG.log(Utils.L_ERR, null, e); }

        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        System.out.println("CONSOLE OFF");
        TheLogger.stop(Utils.CONSOLE);
        LOG.log(Utils.L_INF, "Y HALO THAR FILE");
        (new ChtlServSock()).doSomeThingAndLog();
        try { int i = Integer.parseInt("potato"); } catch (Exception e) { LOG.log(Utils.L_ERR, null, e); }
    }
}
