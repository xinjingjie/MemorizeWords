package com.example.memorizewords.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.memorizewords.R;
import com.example.memorizewords.WordViewModel;
import com.example.memorizewords.bean.Word;


/**
 * A simple {@link Fragment} subclass.
 */
public class add_fragment extends Fragment {
    EditText editText1, editText2;
    Button button;
    WordViewModel wordViewModel;

    public add_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editText1 = requireActivity().findViewById(R.id.editText);
        editText2 = requireActivity().findViewById(R.id.editText2);
        button = requireActivity().findViewById(R.id.button);
        button.setEnabled(false);
        // ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication());
        SavedStateViewModelFactory factory = new SavedStateViewModelFactory(requireActivity().getApplication(), requireActivity());

        wordViewModel = new ViewModelProvider(requireActivity(), factory).get(WordViewModel.class);
        editText1.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText1, 0);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String english = editText1.getText().toString().trim();
                String chinese = editText2.getText().toString().trim();
                button.setEnabled(!english.isEmpty() && !chinese.isEmpty());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText1.addTextChangedListener(textWatcher);
        editText2.addTextChangedListener(textWatcher);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String english = editText1.getText().toString().trim();
                String chinese = editText2.getText().toString().trim();
                Log.d("Add", "onClick: " + english + chinese);
                Word word = new Word(english, chinese);
                wordViewModel.addWord(word);
                Log.d("ADDWORD", "onClick: " + english + chinese);
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_add_fragment_to_show_fragment);
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }
}
