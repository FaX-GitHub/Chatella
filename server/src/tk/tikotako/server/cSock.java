package tk.tikotako.server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

import static tk.tikotako.utils.Utils.L_ERR;
import static tk.tikotako.utils.Utils.formatSockAddr;
import tk.tikotako.utils.PacketsManager.DecodedDataFromClient;
import tk.tikotako.utils.PacketsManager.PacketType;
import tk.tikotako.utils.PacketsManager.DataType;
import tk.tikotako.utils.PacketsManager;

/**
 * Created by ^-_-^ on 08/05/2017 @ 16:06.
 **/

public class cSock extends Thread
{
    private final static Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    enum BuffType { BREADER, BRITER }

    private final List<cSock> clientsList;

    private final Socket client;
    private final ServerLog serverLog;
    private final String remoteClientString;
    private DataOutputStream serverToClient;
    private DataInputStream clientToServer;

    private final PacketsManager packetsManager;

    private boolean isFirstPacket = true;
    private boolean isLoggedIn = false;

    cSock(ServerLog serverLog, List<cSock> clientsList, Socket client)
    {
        this.client = client;
        this.serverLog = serverLog;
        setupStream(BuffType.BRITER);
        setupStream(BuffType.BREADER);
        packetsManager = new PacketsManager();
        this.clientsList = clientsList;
        this.remoteClientString = formatSockAddr(client.getRemoteSocketAddress());
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

            1)server parte
            2) tester si connette e manda nick + pwd
            3) server controlla se:
                nick è connesso
                    se è connesso risponde di cambiare nick
                    se non è connesso controlla se è nel db dei nick registrati
                        se è nel db usa la pwd
                            se la pwd è sbagliata avvisa di cambiarla
            4) se loggato (anonimo o registrato) usa setnick su this


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
            request infoForm <- sta sul tester
            emoji bho farei supporto limitato al posto che tutti almeno uso i byte non usati dai char nelle string oppure è meglio usare stringhe unicode bho
        */

        serverLog.data(new String(data));
        DecodedDataFromClient decodedData = packetsManager.decode(PacketType.FROM_CLIENT, data);

        serverLog.data(decodedData.toString());

        if (isFirstPacket && (decodedData.command != DataType.INIT))
        {
            serverLog.err("Wrong INIT, closing [" + remoteClientString + "]");
            this.close(true);
            return;
        }

        switch (decodedData.command)
        {
            case INIT:
            {
                if(isFirstPacket)
                {
                    serverLog.data("INIT [version] " + decodedData.version);
                    isFirstPacket = false;
                } else
                {
                    serverLog.data("NOT FIRST PACKET, DEBUG ONLY - INIT [version] " + decodedData.version);
                }
                break;
            }
            case REGISTER:
                serverLog.data("REGISTER [email] " + decodedData.mail + " [password] " + decodedData.pwd);
                break;
            case LOGIN:
                serverLog.data("LOGIN [password] " + decodedData.pwd);
                break;
            case MESSAGE:
                serverLog.data("MESSAGE [to] " + decodedData.receiver + " [msg] " + decodedData.message);
                break;
            case SEND:
                serverLog.data("SEND [to] " + decodedData.receiver + " [fileName] " + decodedData.fileName + " [fileSize] " + decodedData.fileSize);
                break;
            case NICK:
                serverLog.data("NICK [nick] " + decodedData.nick);
                break;
            case QUIT:
                serverLog.data("QUIT [message] " + decodedData.message);
                break;
        }
    }

    @Override
    public void run()
    {
        synchronized (clientsList)
        {
            clientsList.add(this);
            serverLog.log("New tester connected: [" + remoteClientString + "] Total: (" + clientsList.size() + ")");
        }

        byte[] data = null;
        int totalDataSize = 0;
        int currentDataSize = 0;

        //TODO ping init

        while (!isInterrupted())
        {
            if (totalDataSize == 0)
            {
                try
                {
                    totalDataSize = clientToServer.readInt();
                    serverLog.data("totalDataSize = " + totalDataSize);
                    if (totalDataSize < 0)
                    {
                        throw new IOException("fail");
                    }
                    data = new byte[totalDataSize];
                } catch (IOException e)
                {
                    // EOF, disconnect/wrong data
                    readingLoopFail("r(totalDataSize)");
                }
            } else
            {
                try
                {
                    int bytesRead = clientToServer.read(data, currentDataSize, totalDataSize - currentDataSize);
                    serverLog.data("currentDataSize = " + currentDataSize + "\tbytesRead = " + bytesRead);
                    if ((bytesRead > 0) && ((currentDataSize += bytesRead) == totalDataSize))
                        {
                            serverLog.data("currentDataSize = " + currentDataSize + "\tbytesRead = " + bytesRead);
                            decodeData(data);
                            currentDataSize = 0;
                            totalDataSize = 0;
                    } else if (bytesRead < 0)
                    {
                        throw new IOException("EOF");
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
                        synchronized (clientsList)
                        {
                            clientsList.remove(this);
                        }
                    }
                }
            }
        }
    }
}
