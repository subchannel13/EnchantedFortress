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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import hr.kravarscan.enchantedfortress.BuildConfig;
import hr.kravarscan.enchantedfortress.logic.Difficulty;
import hr.kravarscan.enchantedfortress.logic.Game;
import hr.kravarscan.enchantedfortress.logic.Utils;

public final class SaveLoad {
    private static final String LOG_TAG = "SaveLoad";

    public static final String Encoding = "UTF-8";
    public static final String SaveKey = "GameState";
    private static final String SaveFileName = "autosave.dat";
    private static final int MaxSaveLength = LatestSaveKeys.KEY_COUNT.ordinal();
    private static SaveLoad instance = null;

    public static SaveLoad get() {
        if (instance == null)
            instance = new SaveLoad();

        return instance;
    }

    private SaveLoad() {
    }

    public boolean hasAutosave(Context context) {
        Log.d(LOG_TAG, "Looking for autosave");
        return context.getFileStreamPath(SaveFileName).exists();
    }

    public void deleteAutosave(Context context)
    {
        Log.d(LOG_TAG, "Clearing autosave");
        File saveFile = context.getFileStreamPath(SaveFileName);

        if (saveFile.exists()) {
            try {
                saveFile.delete();
                Log.d(LOG_TAG, "Successful");
            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception", e);
            }
        }
    }

    public void save(Game game, Context context) {
        Log.d(LOG_TAG, "Saving");
        try {
            FileOutputStream stream = context.openFileOutput(SaveFileName, Context.MODE_PRIVATE);

            stream.write(this.serialize(game));

            stream.close();
            Log.d(LOG_TAG, "Saved");
        } catch (Exception e) {
            Log.e(LOG_TAG, "Autosave failed", e);
        }
    }

    public byte[] load(Context context) {
        byte[] doubleBuffer = new byte[Double.SIZE / Byte.SIZE];
        List<Byte> data;
        byte[] byteBuffer;

        Log.d(LOG_TAG, "Loading");
        try {
            FileInputStream stream = context.openFileInput(SaveFileName);

            Utils.readStream(stream, doubleBuffer);
            int version = (int)ByteBuffer.wrap(doubleBuffer).getDouble();

            if (version < 16) {
                List<Double> legacyData = new ArrayList<>();
                legacyData.add(Double.valueOf(version));

                while (stream.read(doubleBuffer) == doubleBuffer.length && legacyData.size() < MaxSaveLength)
                    legacyData.add(ByteBuffer.wrap(doubleBuffer).getDouble());

                data = upgradeDoubleFormat(legacyData);
            }
            else {
                data = new ArrayList<>(Utils.toBytes((double) version));
                for(int b = stream.read(); b >= 0 && b <256; b = stream.read())
                    data.add((byte)b);
            }

            stream.close();
            Log.d(LOG_TAG, "Reading autosave finished");

            byteBuffer = new byte[data.size()];
            ByteBuffer wrapper =  ByteBuffer.wrap(byteBuffer);
            for (Byte b : data) {
                wrapper.put(b);
            }

            Log.d(LOG_TAG, "Loaded");

        } catch (Exception e) {
            Log.e(LOG_TAG, "Loading autosave failed", e);
            return null;
        }

        return byteBuffer;
    }

    private List<Byte> upgradeDoubleFormat(List<Double> data) {
        Log.d(LOG_TAG, "upgradeSave from " + data.get(LatestSaveKeys.VERSION.ordinal()));

        if (data.get(LatestSaveKeys.VERSION.ordinal()) <= 3) {
            Log.d(LOG_TAG, "upgradeSave to version 3");
            data.add(SaveKeysV7.DIFFICULTY.ordinal(), (double) Difficulty.Medium.getIndex());
        }

        if (data.get(LatestSaveKeys.VERSION.ordinal()) <= 7) {
            Log.d(LOG_TAG, "upgradeSave to version 7");

            data.add(SaveKeysV15.DEMON_LEVEL.ordinal(), Math.floor(0.5 * data.get(SaveKeysV15.TURN.ordinal())));
            data.add(SaveKeysV15.REPORT_HELLGATE_CLOSE.ordinal(), 0.0);
            data.add(SaveKeysV15.REPORT_HELLGATE_OPEN.ordinal(), 0.0);
            data.add(SaveKeysV15.BANISH_COST_GROWTH.ordinal(), 0.0);
        }

        Collection<LatestSaveKeys> doubles = Arrays.asList(
                LatestSaveKeys.VERSION,
                LatestSaveKeys.POPULATION, LatestSaveKeys.WALLS,
                LatestSaveKeys.BUILDING_POINTS, LatestSaveKeys.FARMING_POINTS,
                LatestSaveKeys.SCHOLARSHIP_POINTS, LatestSaveKeys.SOLDIERING_POINTS
        );

        ArrayList<Byte> bytes = new ArrayList<>();
        for (int i = 0; i < LatestSaveKeys.KEY_COUNT.ordinal(); i++) {
            if (doubles.contains(LatestSaveKeys.values()[i]))
                bytes.addAll(Utils.toBytes(data.get(i)));
            else
                bytes.addAll(Utils.toBytes(data.get(i).intValue()));
        }

        return bytes;
    }

    public byte[] serialize(Game game) throws UnsupportedEncodingException {
        List<Byte> gameData = game.save();

        byte[] byteBuffer = new byte[Double.SIZE / Byte.SIZE + gameData.size()];
        ByteBuffer wrapper =  ByteBuffer.wrap(byteBuffer);

        wrapper.putDouble(BuildConfig.VERSION_CODE);
        for (Byte b : gameData) {
            wrapper.put(b);
        }

        return byteBuffer;
    }

    public void deserialize(Game game, Object rawData) {
        Log.d(LOG_TAG, "deserialize");

        byte[] dataArray = (byte[])rawData;

        if (dataArray == null || dataArray.length == 0) {
            return;
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(dataArray);

        int version = (int) byteBuffer.getDouble();
        if (version > BuildConfig.VERSION_CODE)
            return;

        try {
            game.load(byteBuffer);
        }
        catch (UnsupportedEncodingException e)
        {
            Log.d(LOG_TAG, "Exception?", e);
        }
    }
}
