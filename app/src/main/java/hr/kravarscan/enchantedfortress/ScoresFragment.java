package hr.kravarscan.enchantedfortress;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import hr.kravarscan.enchantedfortress.UiUtils.ScoresAdapter;
import hr.kravarscan.enchantedfortress.storage.HighScores;


public class ScoresFragment extends AAttachableFragment {

    public ScoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scores, container, false);

        ListView listView = view.findViewById(R.id.scoreList);
        listView.setAdapter(new ScoresAdapter(this.getActivity(), HighScores.get().getAll()));

        return view;
    }

    @Override
    public void attach(Object listener) {
    }
}
