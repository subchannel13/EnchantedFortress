package hr.kravarscan.enchantedfortress;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hr.kravarscan.enchantedfortress.storage.HighScores;
import hr.kravarscan.enchantedfortress.storage.ScoreEntry;

/**
 * Copyright 2018 Ivan Kravarščan
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

public class ScoresFragment extends AAttachableFragment {

    private static final String LOG_TAG = "ScoresFragment";

    public ScoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_scores, container, false);

        CharSequence[] difficultyNames = this.getActivity().getResources().getTextArray(R.array.difficultyLevels);
        ViewGroup listView = view.findViewById(R.id.scoreList);

        for (ScoreEntry score : HighScores.get().getAll()) {
            View itemView = LayoutInflater.from(this.getActivity()).inflate(R.layout.score_entry_view, listView, false);

            ((TextView)itemView.findViewById(R.id.turnsText)).setText(Integer.toString(score.getTurn()));
            ((TextView)itemView.findViewById(R.id.difficultyText)).setText(difficultyNames[score.getDifficulty()]);

            listView.addView(itemView);
        }

        return view;
    }

    @Override
    public void attach(Object listener) {
        Log.d(LOG_TAG, "attach");
    }
}
