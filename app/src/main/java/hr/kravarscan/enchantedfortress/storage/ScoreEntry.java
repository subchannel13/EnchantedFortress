/*
 * Copyright 2019 Ivan Kravarščan
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
import java.nio.ByteBuffer;

import hr.kravarscan.enchantedfortress.logic.Utils;

public class ScoreEntry {
    private final int turn;
    private final int difficulty;

    ScoreEntry(int turn, int difficulty) {
        this.turn = turn;
        this.difficulty = difficulty;
    }

    boolean isBetter(ScoreEntry scoreEntry) {
        return this.turn < scoreEntry.turn;
    }

    byte[] saveData() {
        ByteBuffer wrapper = ByteBuffer.wrap(new byte[2 * Integer.SIZE / Byte.SIZE]);
        wrapper.putInt(this.turn);
        wrapper.putInt(this.difficulty);

        return wrapper.array();
    }

    public int getTurn() {
        return turn;
    }

    public int getDifficulty() {
        return difficulty;
    }

    static ScoreEntry Load(InputStream stream, int version) throws IOException {
        if (version < 15) {
            byte[] byteBuffer = new byte[2 * Double.SIZE / Byte.SIZE];
            Utils.readStream(stream, byteBuffer);
            ByteBuffer wrapper = ByteBuffer.wrap(byteBuffer);
            return new ScoreEntry((int)wrapper.getDouble(), (int)wrapper.getDouble());
        }

        byte[] byteBuffer = new byte[2 * Integer.SIZE / Byte.SIZE];
        Utils.readStream(stream, byteBuffer);
        ByteBuffer wrapper = ByteBuffer.wrap(byteBuffer);
        return new ScoreEntry(wrapper.getInt(), wrapper.getInt());
    }
}
