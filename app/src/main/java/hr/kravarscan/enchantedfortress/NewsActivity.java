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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewsActivity extends AppCompatActivity {

    private static final String LOG_TAG = "NewsActivity";

    public static final String BanishCost = "BanishCost";
    public static final String DemonIndividualStrength = "DemonIndividualStrength";
    public static final String FirstBanish = "FirstBanish";
    public static final String HellgatesClosed = "HellgatesClosed";
    public static final String BattleAttackers = "BattleAttackers";
    public static final String BattleVictims = "BattleVictims";
    public static final String ScoutedDemons = "ScoutedDemons";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();

        TextView goalReport = this.findViewById(R.id.goalReport);
        if (intent.getIntExtra(HellgatesClosed, 0) <= 0)
            goalReport.setVisibility(View.GONE);
        else if (intent.getBooleanExtra(FirstBanish, true))
            goalReport.setText(getResources().getString(R.string.banishProgress, intent.getIntExtra(BanishCost, 0) / 100));
        else
            goalReport.setText(getResources().getString(R.string.banishProgressShort, intent.getIntExtra(BanishCost, 0) / 100));

        TextView battleReport = this.findViewById(R.id.battleReport);
        if (intent.getIntExtra(BattleAttackers, 0) <= 0)
            battleReport.setVisibility(View.GONE);
        else
            battleReport.setText(getResources().getString(
                    R.string.battleReport,
                    intent.getIntExtra(BattleAttackers, 0),
                    intent.getIntExtra(BattleVictims, 0)));

        TextView scoutReport = this.findViewById(R.id.scoutReport);
        scoutReport.setText(
                getResources().getString(R.string.scoutedLong,
                        Integer.toString(intent.getIntExtra(ScoutedDemons, 0)),
                        Integer.toString((int)intent.getDoubleExtra(DemonIndividualStrength, 0)),
                        Integer.toString((int)(intent.getDoubleExtra(DemonIndividualStrength, 0) * intent.getIntExtra(ScoutedDemons, 0)))));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
