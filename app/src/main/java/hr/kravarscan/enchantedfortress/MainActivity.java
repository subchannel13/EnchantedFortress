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

package hr.kravarscan.enchantedfortress;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import hr.kravarscan.enchantedfortress.storage.SaveLoad;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_main);
        SettingsActivity.setNightMode(this);

        this.findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onContinue();
            }
        });
        this.checkAutosave();

        this.findViewById(R.id.newGameButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onNewGame();
                    }
                }
        );

        this.findViewById(R.id.scoresButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onScores();
                    }
                }
        );

        this.findViewById(R.id.helpButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onHelp();
                    }
                }
        );

        this.findViewById(R.id.settingsButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSettings();
                    }
                }
        );

        this.findViewById(R.id.aboutButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onAbout();
                    }
                }
        );
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.checkAutosave();
    }

    private void checkAutosave() {
        if (!SaveLoad.get().hasAutosave(this)) {
            Log.d(LOG_TAG, "Doesn't have autosave");
            this.findViewById(R.id.continueButton).setVisibility(View.GONE);
        }
        else {
            Log.d(LOG_TAG, "Has autosave");
            this.findViewById(R.id.continueButton).setVisibility(View.VISIBLE);
        }
    }

    private void onContinue() {
        Log.d(LOG_TAG, "onContinue");

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.ContinueGame, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void onNewGame() {
        Log.d(LOG_TAG, "onNewGame");

        Intent intent = new Intent(this, NewGameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void onScores() {
        Log.d(LOG_TAG, "onScores");

        Intent intent = new Intent(this, ScoresActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void onHelp() {
        Log.d(LOG_TAG, "onHelp");

        Intent intent = new Intent(this, HelpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void onSettings() {
        Log.d(LOG_TAG, "onSettings");

        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void onAbout() {
        Log.d(LOG_TAG, "onAbout");

        Intent intent = new Intent(this, AboutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
