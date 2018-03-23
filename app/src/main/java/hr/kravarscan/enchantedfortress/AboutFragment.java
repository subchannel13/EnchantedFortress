package hr.kravarscan.enchantedfortress;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class AboutFragment extends AAttachableFragment {

    private static final String LOG_TAG = "AboutFragment";

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        ((TextView) view.findViewById(R.id.aboutDeveloper)).setText(
                getResources().getString(R.string.aboutDevelopBy, "Ivan Kravarščan")
        );

        TextView translators = view.findViewById(R.id.aboutTranslator);
        if (getResources().getStringArray(R.array.translators).length > 0) {
            translators.setText(
                    getResources().getString(R.string.aboutTranslateBy, translatorList())
            );
        } else
            translators.setVisibility(View.GONE);

        ((TextView) view.findViewById(R.id.aboutTracker)).setText(
                getResources().getString(R.string.aboutTrackerAt, "https://github.com/subchannel13/EnchantedFortress/issues")
        );

        ((TextView) view.findViewById(R.id.aboutCode)).setText(
                getResources().getString(R.string.aboutCodeAt, "https://github.com/subchannel13/EnchantedFortress")
        );

        ((TextView) view.findViewById(R.id.aboutSupport)).setText(
                getResources().getString(R.string.aboutSupportAt, "https://www.paypal.me/IvanKravarscan/5")
        );

        ((TextView) view.findViewById(R.id.aboutVersion)).setText(
                getResources().getString(R.string.aboutVersion, BuildConfig.VERSION_NAME)
        );
        return view;
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
    public void attach(Object listener) {
        Log.d(LOG_TAG, "attach");
    }
}
