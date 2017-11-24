package ch.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PacketBuilder {


    private int checkNumber, waitingTime = 20000;
    private Packet newestPacket;

    public PacketBuilder() {
        checkNumber = 0;
    }

    public ByteBuffer createPacket(InetAddress receiver, ByteBuffer buffer, PacketType packetType) throws UnknownHostException {

        Packet packet = new Packet(receiver, buffer, checkNumber, packetType);
        packet.setData(fillPacket(packet));

        checkNumber++;
        return packet.getData();
    }

    public ByteBuffer fillPacket(Packet packet) throws UnknownHostException {

        HashMap<InetAddress, Tail> users = Lobby.getUsers();
        HashMap<InetAddress, Coordinates> heads = Lobby.getHeads();
        ByteBuffer data = packet.getData();

        if (packet.getType() == PacketType.NAME) {
            data = buildNamePacket(data, users);
        } else if (packet.getType() == PacketType.COORDINATES) {
            data = buildCoordinatesPacket(data, heads, users);
        } else if (packet.getType() == PacketType.DIRECTION) {
            data = buildDirectionPacket(data, heads);
        } else if (packet.getType() == PacketType.CONNECTION) {
            data = buildConnectionPacket(data, packet);
        }
        packet.setData(data);
        newestPacket=packet;
        return data;
    }

    private ByteBuffer buildConnectionPacket(ByteBuffer data, Packet packet) {
        long startTime = packet.getTimeCreated() + waitingTime;
        data.putLong(startTime);
        NewNetwork.setStartTime(startTime);
        return data;
    }


    private ByteBuffer buildDirectionPacket(ByteBuffer data, HashMap<InetAddress, Coordinates> heads) throws UnknownHostException {
        //new Direction(char)
        data.putChar(heads.get(InetAddress.getByName(InetAddress.getLocalHost().getHostAddress())).nextDir);

        return data;

    }

    private ByteBuffer buildCoordinatesPacket(ByteBuffer data, HashMap<InetAddress, Coordinates> heads, HashMap<InetAddress, Tail> users) throws UnknownHostException {

        // x, y, alive
        Coordinates you = heads.get(InetAddress.getByName(InetAddress.getLocalHost().getHostAddress()));
        data = addCoordinates(data, you);

        boolean alive = users.get(InetAddress.getByName(InetAddress.getLocalHost().getHostAddress())).isAlive();
        data = addBoolean(data, alive);

        return data;
    }

    private ByteBuffer buildNamePacket(ByteBuffer data, HashMap<InetAddress, Tail> users) throws UnknownHostException {

        /* length, string,*/
        String userName = users.get(InetAddress.getLocalHost().getHostAddress()).getName();
        return addString(data, userName);
    }


    private ByteBuffer addCoordinates(ByteBuffer data, Coordinates coordinates) {

        data.putInt(coordinates.newX);
        data.putInt(coordinates.newY);

        return data;
    }

    private ByteBuffer addBoolean(ByteBuffer data, boolean bool) {

        if (bool) {
            data.putInt(1);
        } else {
            data.putInt(0);
        }
        return data;
    }


    private ByteBuffer addString(ByteBuffer data, String string) {

        byte[] stringAsBytes = string.getBytes();
        data.putInt(stringAsBytes.length);
        data.put(stringAsBytes);

        return data;
    }

    public Packet getNewestPacket() {
        return newestPacket;
    }
}
