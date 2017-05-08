package tk.tikotako.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

import static tk.tikotako.utils.Utils.L_ERR;
import static tk.tikotako.utils.Utils.formatSockAddr;

/**
 * Created by ^-_-^ on 08/05/2017 @ 16:06.
 **/

public class cSock extends Thread
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    enum BuffType { BREADER, BRITER };

    private final List<cSock> clientSocketList;
    private Socket client;
    private ServerLog serverLog;
    private String remoteClientString;
    private DataOutputStream serverToClient;
    private DataInputStream clientToServer;
    cSock(ServerLog serverLog, List<cSock> clientSocketList, Socket client)
    {
        this.client = client;
        this.serverLog = serverLog;
        this.clientSocketList = clientSocketList;
        this.serverLog.log("cSock(ServerLog serverLog, List<Socket> clientSocketList, Socket client);");
        this.remoteClientString = formatSockAddr(client.getRemoteSocketAddress());
        setupStream(BuffType.BREADER);
        setupStream(BuffType.BRITER);
    }

    @Override
    public String toString()
    {
        return remoteClientString;
    }

    private void setupStream(BuffType type)
    {
        try
        {
            if (type.equals(BuffType.BRITER))
            {
                serverToClient = new DataOutputStream(client.getOutputStream());
            } else
            {
                clientToServer = new DataInputStream(client.getInputStream());
            }
        } catch (IOException e)
        {
            serverLog.err("Error while creating the " + (type.equals(BuffType.BRITER) ? "writer" : "reader") + " stream [" + remoteClientString + "]");
            LOG.log(L_ERR, "", e);
            this.close(true);
        }
    }

    void sendMsg(String message)
    {
        try
        {
            serverToClient.writeChars(message);
            serverToClient.flush();
        } catch (IOException e)
        {
            serverLog.err("Error while sending a message [" + message + "] to [" + remoteClientString + "]");
            LOG.log(L_ERR, "", e);
            this.close(true);
        }
    }

    void sendQuit(String reason)
    {
        sendMsg(reason);
        this.close(false);
    }

    private void readingLoopFail(String wich)
    {
        if (!isInterrupted())
        {
            serverLog.err("Client [" + remoteClientString + "] disconnected in a not proper way (" + wich + ").");
            this.close(true);
        }
    }

    private void decodeData(byte[] data)
    {
        /*
            The packet start with an int then there is the data (so here we have data only, the int is read in the reading loop)

            0x01 iD message  <- send a (public/private) message (string) to iD (int)
                lurka se iD è un chan o un nick online
                    se online invia 0x01 0x01 message
                    if not lurka se è un nick registrato offline
                        se registrato stora il messaggio in db poi avvisa iD quando loginna, risponde 0x01 0x02 a messaggiante
                        se non registrato rispode 0x01 0x03

            0x02 iD fileSize fileName <- tell to iD (int) that want to send fileName(string) which is fileSize (int) in byte.
                lurka impostazioni per vedere se è un file consentito
                    se non lo è risponde 0x02 0x01
                     se lo è ma user rifiuta risponde 0x02 0x02
                     se lo è e user accetta risponde 0x02 0x03

            register nick <- sta sul server in un db
            request infoForm <- sta sul client
            emoji bho farei supporto limitato al posto che tutti almeno uso i byte non usati dai char nelle string oppure è meglio usare stringhe unicode bho
        */

        System.out.println("data: " + new String(data));
        switch (data[0])
        {
            case 0x01:
            {
                break;
            }
            case 0x02:
            {
                break;
            }
        }
    }

    @Override
    public void run()
    {
        serverLog.log("run() [" + remoteClientString + "]");
        synchronized (clientSocketList)
        {
            clientSocketList.add(this);
        }
        serverLog.log("New client connected: [" + remoteClientString + "] Total: (" + clientSocketList.size() + ")");

        byte[] data = null;
        int totalDataSize = 0;
        int currentDataSize = 0;

        while (!isInterrupted())
        {
            if (totalDataSize == 0)
            {
                try
                {
                    totalDataSize = clientToServer.readInt();
                    System.out.printf("totalDataSize [%d]\r\n", totalDataSize);
                    data = new byte[totalDataSize];
                } catch (IOException e)
                {
                    // EOF, disconnect/wrong data
                    readingLoopFail("r(int)");
                }
            } else
            {
                int bytesRead = 0;
                try
                {
                    bytesRead = clientToServer.read(data, currentDataSize, totalDataSize - currentDataSize);
                    if (bytesRead < 0)
                    {
                        throw new IOException("EOF");
                    }
                    currentDataSize += bytesRead;
                    if (currentDataSize == totalDataSize)
                    {
                        System.out.printf("Data [%s]\tSize (%d)\tTotalRead(%d)\r\n", new String(data), bytesRead, totalDataSize);
                        decodeData(data);
                        currentDataSize = 0;
                        totalDataSize = 0;
                    }
                } catch (IOException e)
                {
                    // EOF, disconnect/wrong data
                    readingLoopFail("r(data)");
                }
            }
        }
    }

    private void close(boolean forceClose)
    {
        if (!isInterrupted())
        {
            this.interrupt();
            try
            {
                client.close();
            } catch (IOException e)
            {
                // ignore
            } finally
            {
                try
                {
                    serverToClient.close();
                } catch (IOException e)
                {
                    // ignore
                } finally
                {
                    try
                    {
                        clientToServer.close();
                    } catch (IOException e)
                    {
                        // ignore
                    } finally
                    {
                        if (!forceClose)
                        {
                            serverLog.inf("Connection closed [" + remoteClientString + "]");
                        }
                        synchronized (clientSocketList)
                        {
                            clientSocketList.remove(this);
                        }
                    }
                }
            }
        }
    }
}