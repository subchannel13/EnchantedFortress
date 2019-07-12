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

package hr.kravarscan.enchantedfortress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import hr.kravarscan.enchantedfortress.logic.Difficulty;

public class NewGameActivity extends Activity {

    private static final String LOG_TAG = "NewGameActivity";

    private Spinner difficultySelector;

    private TextView startPopText;
    private TextView demonSpawnText;
    private TextView demonGrowthText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_new_game);

        this.startPopText = this.findViewById(R.id.difficultyStartPop);
        this.demonSpawnText = this.findViewById(R.id.difficultyDemonSpawn);
        this.demonGrowthText = this.findViewById(R.id.difficultyDemonGrowth);
        final View[] infoTexts = new View[]
                {
                        this.demonGrowthText,
                        this.demonSpawnText,
                        this.startPopText
                };

        this.difficultySelector = this.findViewById(R.id.difficultySelection);
        difficultySelector.setAdapter(ArrayAdapter.createFromResource(this, R.array.difficultyLevels, R.layout.research_item));
        difficultySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(LOG_TAG, "difficultySelector.onItemSelected, difficulty: " + i);
                Difficulty data = Difficulty.Levels[i];

                int visibility = (data == Difficulty.Medium) ? View.GONE : View.VISIBLE;
                for (View infoView : infoTexts)
                    infoView.setVisibility(visibility);

                startPopText.setText(getResources().getString(R.string.difficultyStartingPop, bonusPercent(data.getStartingPop(), Difficulty.Medium.getStartingPop())));
                demonSpawnText.setText(getResources().getString(R.string.difficultyDemonSpawn, bonusPercent(data.getDemonSpawnFactor(), Difficulty.Medium.getDemonSpawnFactor())));
                demonGrowthText.setText(getResources().getString(R.string.difficultyDemonGrowth, bonusPercent(data.getDemonPowerBase() - 1, Difficulty.Medium.getDemonPowerBase() - 1)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(LOG_TAG, "difficultySelector.onNothingSelected");

                //No operation
            }
        });
        difficultySelector.setSelection(Difficulty.Medium.getIndex());

        this.findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewGameStart();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onNewGameStart()
    {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.ContinueGame, false);
        intent.putExtra(GameActivity.StartDifficulty, difficultySelector.getSelectedItemPosition());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private static String bonusPercent(double factor, double normalFactor)
    {
        factor = (factor - normalFactor) / normalFactor;
        String sign = factor >= 0 ? "+" : "";
        return sign + Integer.toString((int)(factor * 100)) + "%";
    }
}
