package com.example.memorizewords.fragment;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.memorizewords.R;
import com.example.memorizewords.WordViewModel;
import com.example.memorizewords.adapter.WordAdapter;
import com.example.memorizewords.adapter.WordListAdapter;
import com.example.memorizewords.bean.Word;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static java.lang.Boolean.FALSE;


/**
 * A simple {@link Fragment} subclass.
 */
public class show_fragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    WordViewModel wordViewModel;
    WordListAdapter wordAdapter1, wordAdapter2;
    LiveData<List<Word>> filterWords;
    List<Word> allWords;
    public show_fragment() {
        setHasOptionsMenu(true);
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = requireActivity().findViewById(R.id.show_all);
        floatingActionButton = requireActivity().findViewById(R.id.floatingActionButton);
        //ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication());
        SavedStateViewModelFactory factory = new SavedStateViewModelFactory(requireActivity().getApplication(), requireActivity());
        wordViewModel = new ViewModelProvider(requireActivity(), factory).get(WordViewModel.class);
        wordAdapter1 = new WordListAdapter(false, wordViewModel);
        wordAdapter2 = new WordListAdapter(true, wordViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if ( wordViewModel.getIsCard() ) {
            recyclerView.setAdapter(wordAdapter2);
        } else {
            recyclerView.setAdapter(wordAdapter1);
        }
        filterWords = wordViewModel.getAllWords();
       // filterWords.removeObservers(requireActivity());

        filterWords.observe(getViewLifecycleOwner() , new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                //int temp=wordAdapter1.getItemCount();
                Log.d("ALLWORD", "onChanged:" + words);
//                wordAdapter1.setWordsList(words);
//                wordAdapter2.setWordsList(words);
                // if ( words.size()!=temp ) {
//                wordAdapter1.notifyDataSetChanged();
//                wordAdapter2.notifyDataSetChanged();
                allWords=words;
                wordAdapter1.submitList(words);
                wordAdapter2.submitList(words);
                //   }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_show_fragment_to_add_fragment);
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator(){
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);

                LinearLayoutManager layoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();
               if ( layoutManager!=null ) {
                   int begin = layoutManager.findFirstVisibleItemPosition();
                   int end = layoutManager.findLastVisibleItemPosition();
                    for (int i=begin;i<=end;i++){
                        WordListAdapter.MyViewHolder holder= (WordListAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                        if ( holder!=null ){
                            holder.id.setText(String.valueOf(i+1));
                        }
                    }
               }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(500);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String pattern = newText.trim();
                filterWords = wordViewModel.getSimilar(pattern);
                filterWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
//                        wordAdapter1.setWordsList(words);
//                        wordAdapter2.setWordsList(words);
                        // if ( words.size()!=temp ) {
//                        wordAdapter1.notifyDataSetChanged();
//                        wordAdapter2.notifyDataSetChanged();
                        allWords=words;
                        recyclerView.smoothScrollBy(0,-200);
                        wordAdapter1.submitList(words);
                        wordAdapter2.submitList(words);
                    }
                });
                return true;
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.START|ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final Word deleteWord=allWords.get(viewHolder.getAdapterPosition());
                wordViewModel.deleteWord(deleteWord);
                Snackbar.make(requireActivity().findViewById(R.id.show_fragment),R.string.Snackbar_delete_tip,Snackbar.LENGTH_SHORT)
                        .setAction(R.string.callback, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wordViewModel.addWord(deleteWord);
                            }
                        })
//                        .addCallback(new Snackbar.Callback(){
//                    @Override
//                    public void onDismissed(Snackbar transientBottomBar, int event) {
//                        super.onDismissed(transientBottomBar, event);
//                        switch (event)
//                        {
//                            case Snackbar.Callback.DISMISS_EVENT_ACTION:
//                                Log.d("snackbar", "onDismissed: 撤销了");
//
//                                break;
//                            case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
//                            case Snackbar.Callback.DISMISS_EVENT_MANUAL:
//                            case Snackbar.Callback.DISMISS_EVENT_SWIPE:
//                            case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
//                                //TODO 网络操作
//                                 Word deleteWord=allWords.get(viewHolder.getAdapterPosition());
//                                wordViewModel.deleteWord(deleteWord);
//                                Log.d("snackbar", "onDismissed: 执行了");
//                                break;
//
//                        }
//                    }
//                })
                        .show();




            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_data:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(R.string.clear_data_message);
                builder.setPositiveButton(R.string.positive_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wordViewModel.deleteAll();
                        wordViewModel.resetId();
                        wordViewModel.clearSavedStateHandle();
                    }
                });
                builder.setNegativeButton(R.string.negative_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
            case R.id.change_theme:
                if ( recyclerView.getAdapter() == wordAdapter1 ) {
                    recyclerView.setAdapter(wordAdapter2);
                    wordViewModel.save("is_card","yes");
                } else {
                    recyclerView.setAdapter(wordAdapter1);
                    wordViewModel.save("is_card","no");

                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        wordViewModel.saveAll();
    }
}
