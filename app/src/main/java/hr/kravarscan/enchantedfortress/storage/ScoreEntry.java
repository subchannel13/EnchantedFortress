/*
 * Copyright 2020 Ivan Kravarščan
 *
 * This file is part of Enchanted Fortress.
 *
 * Enchanted Fortress is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Enchanted Fortress is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Enchanted Fortress.  If not, see <http://www.gnu.org/licenses/>.
 */

package hr.kravarscan.enchantedfortress.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import hr.kravarscan.enchantedfortress.logic.Utils;

public class ScoreEntry {
    private final int turn;
    private final int difficulty;
    private final String name;

    ScoreEntry(int turn, int difficulty, String name) {
        this.turn = turn;
        this.difficulty = difficulty;
        this.name = name;
    }

    public boolean isBetter(ScoreEntry scoreEntry) {
        return this.turn < scoreEntry.turn;
    }

    public byte[] saveData() throws UnsupportedEncodingException {
        byte[] nameBytes = this.name.getBytes(SaveLoad.Encoding);

        ByteBuffer wrapper = ByteBuffer.wrap(new byte[3 * Integer.SIZE / Byte.SIZE + nameBytes.length]);
        wrapper.putInt(this.turn);
        wrapper.putInt(this.difficulty);
        wrapper.putInt(nameBytes.length);
        wrapper.put(nameBytes);

        return wrapper.array();
    }

    public int getTurn() {
        return this.turn;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public String getName() {
        return this.name;
    }

    public static ScoreEntry Load(InputStream stream, int version) throws IOException {
        if (version < 15) {
            byte[] byteBuffer = new byte[2 * Double.SIZE / Byte.SIZE];
            Utils.readStream(stream, byteBuffer);
            ByteBuffer wrapper = ByteBuffer.wrap(byteBuffer);
            return new ScoreEntry((int)wrapper.getDouble(), (int)wrapper.getDouble(), "");
        }

        byte[] byteBuffer = new byte[2 * Integer.SIZE / Byte.SIZE];
        Utils.readStream(stream, byteBuffer);
        ByteBuffer wrapper = ByteBuffer.wrap(byteBuffer);
        int turn = wrapper.getInt();
        int difficulty = wrapper.getInt();

        if (version < 16) {
            return new ScoreEntry(turn, difficulty, "");
        }

        byteBuffer = new byte[Integer.SIZE / Byte.SIZE];
        Utils.readStream(stream, byteBuffer);
        int nameLength = (int) ByteBuffer.wrap(byteBuffer).getInt();

        byte[] stringBytes = new byte[nameLength];
        Utils.readStream(stream, stringBytes);
        String name = new String(stringBytes, SaveLoad.Encoding);

        return new ScoreEntry(turn, difficulty, name);
    }
}
