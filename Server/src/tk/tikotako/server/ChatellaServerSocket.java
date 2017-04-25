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

package tk.tikotako.server;import java.util.logging.Logger;

import static tk.tikotako.utils.Utils.*;

/**
 * Created by ^-_-^ on 25/04/2017 @ 15:26.
 **/

public class ChatellaServerSocket
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

   public void doSomeThingAndLog() {
        // ... more code

        // now we demo the logging
       LOG.log(L_INF, "Y HALO THAR");

    }

}
