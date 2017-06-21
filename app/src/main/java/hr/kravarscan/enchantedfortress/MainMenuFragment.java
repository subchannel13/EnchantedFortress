package hr.kravarscan.enchantedfortress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hr.kravarscan.enchantedfortress.storage.SaveLoad;

public class MainMenuFragment extends AAttachableFragment {

    private OnFragmentInteractionListener listener;

    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main_menu, container, false);

        View continueButton = layout.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onContinue();
            }
        });

        if (!SaveLoad.get().hasAutosave(container.getContext()))
            continueButton.setVisibility(View.GONE);

        layout.findViewById(R.id.newGameButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onNewGame();
                    }
                }
        );

        layout.findViewById(R.id.helpButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onHelp();
                    }
                }
        );

        layout.findViewById(R.id.aboutButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onAbout();
                    }
                }
        );

        return layout;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void attach(Object listener) {
        if (listener instanceof OnFragmentInteractionListener) {
            this.listener = (OnFragmentInteractionListener) listener;
        } else {
            throw new RuntimeException(listener.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    interface OnFragmentInteractionListener {
        void onContinue();
        void onNewGame();
        void onHelp();
        void onAbout();
    }
}
