package ch.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * SwingSnake
 * Copyright (C) 2017  Manuel Strenge
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

class Lobby {
    private static int snakeSize = 10;
    private static long seed;
    private static HashMap<InetAddress, Tail> users = new HashMap<>();
    private static HashMap<InetAddress, Coordinates> heads = new HashMap<>();

    public Lobby(InetAddress[] ipAdresses, int size) throws UnknownHostException {
        for (int i = 0; i < ipAdresses.length; i++) {
            heads.put(ipAdresses[i], new Coordinates());
            users.put(ipAdresses[i], new Tail("Test"));
        }
        users.get(InetAddress.getByName(InetAddress.getLocalHost().getHostAddress())).setName("TheBromo");
        int ySteps = 0;
        for (InetAddress key : users.keySet()) {
            for (int index = 1; index < users.get(key).getLength(); index++) {
                users.get(key).setupArray(index, size + 50, ySteps);
                ySteps += snakeSize;
            }
        }
        positionSetter(size);

        seed = generateSeed();


    }

    public static long getSeed() {
        return seed;
    }

    public static HashMap<InetAddress, Tail> getUsers() {
        return users;
    }

    public static HashMap<InetAddress, Coordinates> getHeads() {
        return heads;
    }

    public static int getSnakeSize() {
        return snakeSize;
    }

    private long generateSeed() {

        String s = "";
        for (InetAddress key : users.keySet()) {
            s = s.concat(key.getCanonicalHostName());
        }
        long hash = 0;
        for (char c : s.toCharArray()) {
            hash = 31L * hash + c;
        }
        return hash;
    }

    private void positionSetter(int size) {
        int playerCount = users.size();
        if (playerCount == 3) {
            playerCount = 4;
        } else if (playerCount > 3 && playerCount < 10) {
            playerCount = 9;
        } else if (playerCount > 9) {
            playerCount = 16;
        }
        if (playerCount == 2) {
            InetAddress[] keys;
            keys = users.keySet().toArray(new InetAddress[users.size()]);
            Lobby.getHeads().get(keys[0]).setPos(size / 4, size / 2);
            Lobby.getHeads().get(keys[1]).setPos((size / 4) * 3, size / 2);
        } else {
            Iterator<InetAddress> iterator = users.keySet().iterator();
            int steps = size / ((int) Math.sqrt(playerCount));
            for (int x = 0; x < size; x = x + steps) {
                for (int y = 0; y < size; y = y + steps) {
                    if (iterator.hasNext()) {
                        Lobby.getHeads().get(iterator.next()).setPos(x + (steps / 2), x + (steps / 2));
                    }
                }
            }
        }


    }
}