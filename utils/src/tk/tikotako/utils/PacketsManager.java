package tk.tikotako.utils;

import java.nio.ByteBuffer;

/**
 * Created by ^-_-^ on 09/05/2017 @ 01:41.
 **/

public class PacketsManager
{
    public enum PacketType { FROMCLIENT, FROMSERVER }
    public enum DataType { INIT, REGISTER, MESSAGE, SEND, NICK, QUIT }

    public class DecodedDataFromClient
    {
        public DataType command;
        //
        public int version;
        //
        public String mail;
        public String nick;
        public String pwd;
        //
        public String receiver;
        public String message;
        //
        public String fileName;
        public int fileSize;

        @Override
        public String toString()
        {
            return "DecodedDataFromClient{" + "command=" + command + ", version=" + version + ", mail='" + mail + '\'' + ", nick='" + nick + '\'' + ", pwd='" + pwd + '\'' + ", receiver='" + receiver + '\'' + ", message='" + message + '\'' + ", fileName='" + fileName + '\'' + ", fileSize=" + fileSize + '}';
        }
    }

    public class DecodedDataFromServer
    {
        public DataType command;
    }

    @SuppressWarnings("unchecked")
    public <T> T decode(PacketType type, byte[] data)
    {
        if (type == PacketType.FROMCLIENT)
        {
            DecodedDataFromClient decodedDataFromClient  = new DecodedDataFromClient();

            // This is the 1st packet sent by a client
            if (ByteBuffer.wrap(data, 0, data.length -4).asCharBuffer().toString().contains("Chatella"))
            {
                // "Chatella"(int)
                decodedDataFromClient.command = DataType.INIT;
                decodedDataFromClient.version = ByteBuffer.wrap(data, data.length -4, 4).getInt();
                return (T)decodedDataFromClient;
            }

            // Client request to change nick
            if (data[0] == 0x01)
            {
                // [0x01]"nick"
                decodedDataFromClient.command = DataType.NICK;
                decodedDataFromClient.nick = ByteBuffer.wrap(data, 1, data.length -1).asCharBuffer().toString();
                return (T)decodedDataFromClient;
            }

            // Client request to register current nick using [password], [email] is used to recover the nick and is stored one-way encoded
            if (data[0] == 0x02)
            {
                // [0x02][b]"mail""password"[b]
                //0x33 - [0x02][0x22]"admin@tikotako.tk""p@ssw0rd"[0x10]
                decodedDataFromClient.command = DataType.REGISTER;
                decodedDataFromClient.mail = ByteBuffer.wrap(data, 2, data[1]).asCharBuffer().toString();
                decodedDataFromClient.pwd = ByteBuffer.wrap(data, data[1] +2, data[data.length -1]).asCharBuffer().toString();
                return (T)decodedDataFromClient;
            }

            return (T)decodedDataFromClient;
        } else
        {
            DecodedDataFromServer decodedDataFromServer  = new DecodedDataFromServer();
            return (T)decodedDataFromServer;
        }
    }
}
