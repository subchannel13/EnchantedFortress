package hr.kravarscan.enchantedfortress;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hr.kravarscan.enchantedfortress.AAttachableFragment;
import hr.kravarscan.enchantedfortress.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoresFragment extends AAttachableFragment {

    public ScoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scores, container, false);
    }

    @Override
    public void attach(Object listener) {
    }
}
