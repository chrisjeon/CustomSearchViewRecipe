package com.toasterbits.customsearchviewrecipe;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chrisjeon on 1/23/17.
 */

public class SearchResultsActivity extends AppCompatActivity {
    private static final String LOG_TAG = SearchResultsActivity.class.getSimpleName();
    private static final int RECOGNIZER_REQ_CODE = 1234;

    private RecyclerView mSearchHistoryRecyclerView;
    private RecyclerView mSearchResultRecyclerView;
    private SearchHistoryRowAdapter mSearchHistoryRowAdapter = new SearchHistoryRowAdapter();
    private SearchResultRowAdapter mSearchResultRowAdapter = new SearchResultRowAdapter();

    private ArrayList<SearchHistory> mSearchHistoryList = new ArrayList<>();

    private MaterialSearchView mSearchView;
    private ImageView mBackButton;
    private ImageView mVoiceSearchButton;
    private ImageView mEmptyButton;
    private EditText mSearchTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);

        setContentView(R.layout.activity_search_results);

        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);

        mSearchView.setVoiceSearch(true);

        mSearchView.showSearch(false);

        mBackButton = (ImageView) mSearchView.findViewById(R.id.action_up_btn);

        mVoiceSearchButton = (ImageView) mSearchView.findViewById(R.id.action_voice_btn);

        mEmptyButton = (ImageView) mSearchView.findViewById(R.id.action_empty_btn);

        mSearchTextView = (EditText) mSearchView.findViewById(R.id.searchTextView);

        mVoiceSearchButton.setVisibility(View.VISIBLE);

        mVoiceSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                startActivityForResult(intent, RECOGNIZER_REQ_CODE);
            }
        });

        mEmptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoiceSearchButton.setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                mSearchTextView.setText("");
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSearchHistoryRecyclerView = (RecyclerView) findViewById(R.id.recycler_search_history);
        mSearchResultRecyclerView = (RecyclerView) findViewById(R.id.recycler_search_results);
        mSearchHistoryRecyclerView.setAdapter(mSearchHistoryRowAdapter);
        mSearchResultRecyclerView.setAdapter(mSearchResultRowAdapter);

        mSearchHistoryList = (ArrayList<SearchHistory>) SearchHistory.listAll(SearchHistory.class);

        mSearchHistoryRowAdapter.setSearchResults(mSearchHistoryList);

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchHistory searchResult = new SearchHistory(query);
                searchResult.save();
                mSearchHistoryRowAdapter.setSearchResults((ArrayList<SearchHistory>) SearchHistory.listAll(SearchHistory.class));
                mSearchHistoryRowAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    mSearchHistoryRecyclerView.setVisibility(View.GONE);
                    mSearchResultRecyclerView.setVisibility(View.VISIBLE);
                    List usersList = User.findWithQuery(User.class, "SELECT * FROM user WHERE email LIKE ?", "%" + newText + "%");
                    mSearchResultRowAdapter.setSearchResults((ArrayList<User>) usersList);
                } else {
                    mVoiceSearchButton.setVisibility(View.VISIBLE);
                    mSearchHistoryRecyclerView.setVisibility(View.VISIBLE);
                    mSearchResultRecyclerView.setVisibility(View.GONE);
                }

                return true;
            }
        });
    }

    public class SearchHistoryRowAdapter extends RecyclerView.Adapter<SearchResultsActivity.SearchHistoryRowAdapter.ViewHolder> {
        ArrayList<SearchHistory> searchHistoryList = null;

        class ViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public TextView mTitleTextView;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleTextView = (TextView) mView.findViewById(R.id.title);
            }

            void updateSearchHistory(SearchHistory searchHistory) {
                mTitleTextView.setText(searchHistory.getTitle());
            }
        }

        @Override
        public SearchResultsActivity.SearchHistoryRowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_history, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(SearchHistoryRowAdapter.ViewHolder holder, int position) {
            holder.updateSearchHistory(searchHistoryList.get(position));
        }

        @Override
        public int getItemCount() {
            return searchHistoryList.size();
        }

        public void setSearchResults(ArrayList<SearchHistory> searchHistoryList) {
            this.searchHistoryList = searchHistoryList;
            notifyDataSetChanged();
        }
    }

    public class SearchResultRowAdapter extends RecyclerView.Adapter<SearchResultsActivity.SearchResultRowAdapter.ViewHolder> {
        ArrayList<User> searchResults = new ArrayList<>();

        class ViewHolder extends RecyclerView.ViewHolder {
            private User mUser = null;
            public View mView;
            public TextView mFullNameTextView;
            public TextView mEmailTextView;


            ViewHolder(View view) {
                super(view);
                mView = view;
                mFullNameTextView = (TextView) view.findViewById(R.id.full_name);
                mEmailTextView = (TextView) view.findViewById(R.id.email);
            }

            void updateSearchResult(User userResult) {
                this.mUser = userResult;
                mFullNameTextView.setText(mUser.getFullName());
                mEmailTextView.setText(mUser.getEmail());
            }
        }

        @Override
        public SearchResultsActivity.SearchResultRowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_result, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(SearchResultsActivity.SearchResultRowAdapter.ViewHolder holder, int position) {
            holder.updateSearchResult(searchResults.get(position));
        }

        @Override
        public int getItemCount() {
            return searchResults.size();
        }

        public void setSearchResults(ArrayList<User> searchResults) {
            this.searchResults = searchResults;
            notifyDataSetChanged();
        }
    }
}
