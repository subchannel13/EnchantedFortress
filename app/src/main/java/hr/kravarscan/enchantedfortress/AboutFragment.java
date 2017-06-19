package hr.kravarscan.enchantedfortress;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AboutFragment extends AAttachableFragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        ((TextView) view.findViewById(R.id.aboutDeveloper)).setText(
                getResources().getString(R.string.aboutDevelopBy) + " Ivan Kravarščan"
        );

        TextView translators = view.findViewById(R.id.aboutTranslator);
        if (getResources().getStringArray(R.array.translators).length > 0) {
            translators.setText(
                    getResources().getString(R.string.aboutTranslateBy) + " "
            );
        } else
            translators.setVisibility(View.GONE);

        ((TextView) view.findViewById(R.id.aboutTracker)).setText(
                getResources().getString(R.string.aboutTrackerAt) + "\nhttps://github.com/subchannel13/EnchantedFortress/issues"
        );

        ((TextView) view.findViewById(R.id.aboutCode)).setText(
                getResources().getString(R.string.aboutCodeAt) + "\nhttps://github.com/subchannel13/EnchantedFortress"
        );

        ((TextView) view.findViewById(R.id.aboutSupport)).setText(
                getResources().getString(R.string.aboutSupportAt) + "\nhttps://www.paypal.me/IvanKravarscan/5"
        );

        ((TextView) view.findViewById(R.id.aboutVersion)).setText(
                getResources().getString(R.string.aboutVersion) + ": " + BuildConfig.VERSION_NAME
        );
        return view;
    }

    @Override
    public void attach(Object listener) { }
}
