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


import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static tk.tikotako.server.UserInterfaceStuff.eStart;
import static tk.tikotako.utils.Utils.L_ERR;
import static tk.tikotako.utils.Utils.errorMessage;
import static tk.tikotako.utils.Utils.formatSockAddr;

/**
 * Created by ^-_-^ on 25/04/2017 @ 15:26.
 **/

class ChtlServSock
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private MainForm mainForm;
    private ServerLog serverLog;
    private boolean isKill;
    private ServerSocket serverSocket;
    private final List<cSock> clientSocketList = new ArrayList<cSock>();

    ChtlServSock(MainForm mainForm)
    {
        isKill = true;
        this.mainForm = mainForm;
        serverLog = mainForm.getServerLog();
        serverLog.log("ChtlServSock(MainForm mainForm)");
    }


    // creare classe cSocket
    // implementare ping che autoclosa se non ponga o ce nerrore
    // overridare close in modo che si autodeleti dal list
    // socket devono essere ssl
    // volantino coop

    void start()
    {
        String msg = "Starting server on (IP - PORT): [" + mainForm.getIp() + " - " + mainForm.getPort() + "]";
        serverLog.log(msg);
        System.out.println("clientSocketList.isEmpty -> " + clientSocketList.isEmpty());
        System.out.println("clientSocketList.size -> " + clientSocketList.size());

        try
        {
            serverSocket = new ServerSocket();
            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(mainForm.getIp()), Integer.parseUnsignedInt(mainForm.getPort()));
            serverSocket.bind(socketAddress, 9001);
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
            LOG.log(L_ERR, "", e);
            errorMessage(eStart, "UnknownHostException > " + e.getMessage());
        } catch (Exception e)
        {
            e.printStackTrace();
            LOG.log(L_ERR, "", e);
            errorMessage(eStart, e);
        }

        isKill = false;
        new Thread(() ->
        {
            try
            {
                while (!isKill)
                {
                    new cSock(serverLog, clientSocketList, serverSocket.accept()).start();
                }
            } catch (IOException e)
            {
                // ignore, is caused by the stop.
            }
        }).start();
    }

    void stop()
    {
        serverLog.log("Stopping server.");
        if (serverSocket != null)
        {
            try
            {
                serverSocket.close();
            } catch (Exception e)
            {
                e.printStackTrace();
                LOG.log(L_ERR, "", e);
                errorMessage(eStart, e);
            }
        }

        synchronized (clientSocketList)
        {
            while (clientSocketList.iterator().hasNext())
            {
                clientSocketList.iterator().next().sendQuit("STOP");
            }
        }

        serverSocket = null;
        isKill = true;
    }

    void msgToAll(String msg)
    {
        synchronized (clientSocketList)
        {
            serverLog.log("msgToAll(String msg) > clients connected: " + clientSocketList.size());

            for (cSock aClientSocket : clientSocketList)
            {
                serverLog.log("msg to -> " + aClientSocket.toString());
                aClientSocket.sendMsg(msg);
            }
        }
    }

    boolean isRunning()
    {
        return !isKill;
    }

}
