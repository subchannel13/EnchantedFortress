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
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends Activity {

    private static final String LOG_TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_about);

        ((TextView) this.findViewById(R.id.aboutDeveloper)).setText(
                getResources().getString(R.string.aboutDevelopBy, "Ivan Kravarščan")
        );

        TextView translators = this.findViewById(R.id.aboutTranslator);
        if (getResources().getStringArray(R.array.translators).length > 0) {
            translators.setText(
                    getResources().getString(R.string.aboutTranslateBy, translatorList())
            );
        } else
            translators.setVisibility(View.GONE);

        ((TextView) this.findViewById(R.id.aboutTracker)).setText(
                getResources().getString(R.string.aboutTrackerAt, "https://github.com/subchannel13/EnchantedFortress/issues")
        );

        ((TextView) this.findViewById(R.id.aboutCode)).setText(
                getResources().getString(R.string.aboutCodeAt, "https://github.com/subchannel13/EnchantedFortress")
        );

        ((TextView) this.findViewById(R.id.aboutSupport)).setText(
                getResources().getString(R.string.aboutSupportAt, "https://www.paypal.me/IvanKravarscan/5")
        );

        ((TextView) this.findViewById(R.id.aboutVersion)).setText(
                getResources().getString(R.string.aboutVersion, BuildConfig.VERSION_NAME)
        );
    }

    private String translatorList() {
        StringBuilder sb = new StringBuilder();

        for(String line : getResources().getStringArray(R.array.translators)) {
            sb.append(line);
            sb.append("\n");
        }

        return  sb.toString().trim();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
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
}
