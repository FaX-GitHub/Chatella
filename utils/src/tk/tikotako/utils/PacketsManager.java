package tk.tikotako.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by ^-_-^ on 09/05/2017 @ 01:41.
 **/

public class PacketsManager
{
    public enum PacketType {FROM_CLIENT, FROM_SERVER, TO_CLIENT, TO_SERVER }
    //public enum DataType { INIT, REGISTER, LOGIN, MESSAGE, SEND, NICK, QUIT }

    public enum DataType
    {
            INIT ((byte) 0x01),
        REGISTER ((byte) 0x02),
           LOGIN ((byte) 0x03),
         MESSAGE ((byte) 0x04),
            SEND ((byte) 0x05),
            NICK ((byte) 0x06),
            QUIT ((byte) 0x07);

        private final byte value;

        DataType(byte value)
        {
            this.value = value;
        }
    }

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
        // TODO finish her /(°>°)/  /°.°\
    }

    /**
     *  Generate the packet to send
     * @param packetType    Packet type, output format is decided by this.
     * @param dataType      Data type, the command to send.
     * @param datas         An Object array of data to send.
     * @return              The complete packet that the Socket have to send.
     */
    public byte[] generatePacket(PacketType packetType, DataType dataType, Object...datas)
    {
        if ((datas == null) || (datas.length == 0))
        {
            return null;
        }

        if (packetType == PacketType.TO_SERVER)
        {
            switch (dataType)
            {
                case INIT:
                {
                    // (totSize) "Chatella" (int)
                    // datas[0] -> string (Chatella)
                    // datas[1] -> int  (version)
                    byte[] init = ((String) datas[0]).getBytes(Charset.forName("UTF-16BE"));
                    int totalPacketSize = init.length + 4;
                    return ByteBuffer.allocate(totalPacketSize + 4)
                            .putInt(totalPacketSize)
                            .put(init)
                            .putInt((int)datas[1])
                            .array();
                }
                case REGISTER:
                {
                    // (totSize) [b0x02] [b] "mail" "password" [b]
                    // datas [0] -> string (mail)
                    // datas [1] -> string (pass)
                    byte[] mail = ((String) datas[0]).getBytes(Charset.forName("UTF-16BE"));
                    byte[] pass = ((String) datas[1]).getBytes(Charset.forName("UTF-16BE"));
                    int totalPacketSize = 1 + 1 + mail.length + pass.length + 1;
                    return ByteBuffer.allocate(totalPacketSize + 4)
                            .putInt(totalPacketSize)
                            .put(dataType.value)
                            .put((byte) mail.length)
                            .put(mail)
                            .put(pass)
                            .put((byte) pass.length)
                            .array();
                }
                case LOGIN:
                {
                    // (totSize) [0x03] "password"
                    // datas[0] -> string (password)
                    byte[] pass = ((String) datas[0]).getBytes(Charset.forName("UTF-16BE"));
                    int totalPacketSize = 1 + pass.length;
                    return ByteBuffer.allocate(totalPacketSize + 4)
                            .putInt(totalPacketSize)
                            .put(dataType.value)
                            .put(pass)
                            .array();
                }
                case MESSAGE:
                {
                    // (totSize) [b0x04] [b] "recv" "msg" (int)
                    // datas [0] -> string (receiver)
                    // datas [1] -> string (message)
                    byte[] recv = ((String) datas[0]).getBytes(Charset.forName("UTF-16BE"));
                    byte[] mess = ((String) datas[1]).getBytes(Charset.forName("UTF-16BE"));
                    int totalPacketSize = 1 + 1 + recv.length + mess.length + 4;
                    return ByteBuffer.allocate(totalPacketSize + 4)
                            .putInt(totalPacketSize)
                            .put(dataType.value)
                            .put((byte) recv.length)
                            .put(recv)
                            .put(mess)
                            .putInt(mess.length)
                            .array();
                }
                case SEND:
                    break;
                case NICK:
                {
                    // (totSize) [0x01] "nick"
                    // datas[0] -> string (nick)
                    byte [] nick = ((String)datas[0]).getBytes(Charset.forName("UTF-16BE"));
                    int packetSize = 1 + nick.length;
                    return ByteBuffer.allocate(packetSize + 4)
                            .putInt(packetSize)
                            .put(dataType.value)
                            .put(nick)
                            .array();
                }
                case QUIT:
                    break;
            }
        }

        return new byte[] { 0 };
    }

    @SuppressWarnings("unchecked")
    public <T> T decode(PacketType packetType, byte[] receivedData)
    {
        if (packetType == PacketType.FROM_CLIENT)
        {
            DecodedDataFromClient decodedDataFromClient  = new DecodedDataFromClient();

            // This is the 1st packet sent by a tester
            if (ByteBuffer.wrap(receivedData, 0, receivedData.length -4).asCharBuffer().toString().contains("Chatella"))
            {
                // "Chatella"(int)
                decodedDataFromClient.command = DataType.INIT;
                decodedDataFromClient.version = ByteBuffer.wrap(receivedData, receivedData.length -4, 4).getInt();
                return (T)decodedDataFromClient;
            }

            // Client request to change nick
            if (receivedData[0] == DataType.NICK.value)
            {
                // [0x01]"nick"
                decodedDataFromClient.command = DataType.NICK;
                decodedDataFromClient.nick = ByteBuffer.wrap(receivedData, 1, receivedData.length -1).asCharBuffer().toString();
                return (T)decodedDataFromClient;
            }

            // Client request to register current nick using [password], [email] is used to recover the nick - both stored 1way encoded
            if (receivedData[0] == DataType.REGISTER.value)
            {
                // [0x02][b]"mail""password"[b]
                //0x33 - [0x02][0x22]"admin@tikotako.tk""p@ssw0rd"[0x10]
                decodedDataFromClient.command = DataType.REGISTER;
                decodedDataFromClient.mail = ByteBuffer.wrap(receivedData, 2, receivedData[1]).asCharBuffer().toString();
                decodedDataFromClient.pwd = ByteBuffer.wrap(receivedData, receivedData[1] +2, receivedData[receivedData.length -1]).asCharBuffer().toString();
                return (T)decodedDataFromClient;
            }

            // Client request to login current nick
            if (receivedData[0] == DataType.LOGIN.value)
            {
                // [0x03]"password"
                decodedDataFromClient.command = DataType.LOGIN;
                decodedDataFromClient.pwd = ByteBuffer.wrap(receivedData, 1, receivedData.length -1).asCharBuffer().toString();
                return (T)decodedDataFromClient;
            }

            // Client request to send a message to someone
            if (receivedData[0] == DataType.MESSAGE.value)
            {
                // [0x04][b]"receiver""message"(int)
                decodedDataFromClient.command = DataType.MESSAGE;
                decodedDataFromClient.receiver = ByteBuffer.wrap(receivedData, 2, receivedData[1]).asCharBuffer().toString();
                int messageSize = ByteBuffer.wrap(receivedData, receivedData.length -4, 4).getInt();
                decodedDataFromClient.message = ByteBuffer.wrap(receivedData, receivedData[1] +2, messageSize).asCharBuffer().toString();
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
