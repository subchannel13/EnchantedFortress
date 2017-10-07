package hr.kravarscan.enchantedfortress.UiUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hr.kravarscan.enchantedfortress.R;
import hr.kravarscan.enchantedfortress.logic.Difficulty;
import hr.kravarscan.enchantedfortress.storage.ScoreEntry;

/**
 * Copyright 2017 Ivan Kravarščan
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

public class ScoresAdapter extends ArrayAdapter<ScoreEntry> {
    public ScoresAdapter(Context context, List<ScoreEntry> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ScoreEntry score = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.score_entry_view, parent, false);
        }

        CharSequence[] difficultyNames = this.getContext().getResources().getTextArray(R.array.difficultyLevels);

        ((TextView)convertView.findViewById(R.id.turnsText)).setText(Integer.toString(score.getTurn()));
        ((TextView)convertView.findViewById(R.id.difficultyText)).setText(difficultyNames[score.getDifficulty()]);

        return convertView;
    }
}
