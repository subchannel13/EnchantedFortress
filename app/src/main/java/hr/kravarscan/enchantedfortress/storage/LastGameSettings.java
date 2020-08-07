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

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

import hr.kravarscan.enchantedfortress.BuildConfig;
import hr.kravarscan.enchantedfortress.logic.Difficulty;
import hr.kravarscan.enchantedfortress.logic.Utils;

public class LastGameSettings {
    public static final String DefaultName = "King Leopold";

    private static final String LOG_TAG = "LastGameSettings";

    private static final String FileName = "lastgame.dat";
    private static LastGameSettings instance = null;

    private String name;
    private Difficulty difficulty;

    public static void init(Context context) {
        instance = load(context);
    }

    public static LastGameSettings get() {
        return instance;
    }

    public LastGameSettings(String name, Difficulty difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void save(Context context) {
        byte[] intBuffer = new byte[Integer.SIZE / Byte.SIZE];

        Log.d(LOG_TAG, "Saving");
        try {
            FileOutputStream stream = context.openFileOutput(FileName, Context.MODE_PRIVATE);

            ByteBuffer.wrap(intBuffer).putInt(BuildConfig.VERSION_CODE);
            stream.write(intBuffer);

            ByteBuffer.wrap(intBuffer).putInt(this.difficulty.getIndex());
            stream.write(intBuffer);

            byte[] nameBytes = this.name.getBytes(SaveLoad.Encoding);
            ByteBuffer.wrap(intBuffer).putInt(nameBytes.length);
            stream.write(intBuffer);
            stream.write(nameBytes);

            stream.close();
            Log.d(LOG_TAG, "Saved");
        } catch (Exception e) {
            Log.e(LOG_TAG, "Saving last game settings failed", e);
        }
    }

    private static LastGameSettings load(Context context) {
        byte[] intBuffer = new byte[Integer.SIZE / Byte.SIZE];

        Log.d(LOG_TAG, "Loading");
        try {
            FileInputStream stream = context.openFileInput(FileName);

            Utils.readStream(stream, intBuffer);
            int version = (int)ByteBuffer.wrap(intBuffer).getInt();

            Utils.readStream(stream, intBuffer);
            int difficultyIndex = (int) ByteBuffer.wrap(intBuffer).getInt();
            Difficulty difficulty = Difficulty.Levels[difficultyIndex];

            Utils.readStream(stream, intBuffer);
            int nameLength = (int) ByteBuffer.wrap(intBuffer).getInt();

            byte[] stringBytes = new byte[nameLength];
            Utils.readStream(stream, stringBytes);
            String name = new String(stringBytes, SaveLoad.Encoding);

            stream.close();
            Log.i(LOG_TAG, "loaded");

            return new LastGameSettings( name, difficulty);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Loading last game settings failed", e);
        }

        Log.i(LOG_TAG, "Initializing default last game settings");
        return new LastGameSettings(DefaultName, Difficulty.Medium);
    }
}