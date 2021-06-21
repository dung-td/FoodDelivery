package com.example.fooddelivery.activity.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.SearchingItemAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.model.OnGetDataListener;
import com.example.fooddelivery.model.Product;
import com.example.fooddelivery.model.SearchString;

import java.util.ArrayList;
import java.util.Locale;

public class SearchingActivity extends AppCompatActivity {

    TextView textViewDeleteSearches, textViewNoSearches;
    EditText editTextSearchInput;
    RecyclerView recyclerViewSearches;
    ImageButton buttonBack, buttonClearText;
    ArrayList<String> searchData;
    public static ArrayList<Product> queryResult;
    public static SearchingItemAdapter searchingItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);

        initView();
        loadLanguage();
        initRecyclerViewSearches();
        deleteSearches();
        startSearchingInput();
    }

    private void initView() {
        editTextSearchInput = findViewById(R.id.et_search_input);
        recyclerViewSearches = findViewById(R.id.recycler_view_searches);
        textViewDeleteSearches = findViewById(R.id.btn_delete_searches);
        textViewNoSearches = findViewById(R.id.tv_no_search_data);
        buttonBack = findViewById(R.id.btn_back);
        buttonClearText = findViewById(R.id.btn_clear_text);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchingActivity.super.onBackPressed();
            }
        });
        buttonClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextSearchInput.setText("");
            }
        });
        editTextSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextSearchInput.getText().length() > 0)
                    buttonClearText.setVisibility(View.VISIBLE);
                else buttonClearText.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void initRecyclerViewSearches() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSearches.setLayoutManager(layoutManager);
        getSearchesList();
    }

    private void setRecyclerViewSearchesAdapter () {
        searchData = new ArrayList<>();
        for (SearchString search : LoginActivity.firebase.searchList) {
            searchData.add(search.getDetail());
        }
        if (searchData.isEmpty()) {
            textViewDeleteSearches.setVisibility(View.INVISIBLE);
            textViewNoSearches.setVisibility(View.VISIBLE);
        }
        else {
            textViewNoSearches.setVisibility(View.INVISIBLE);
            textViewDeleteSearches.setVisibility(View.VISIBLE);
            searchingItemAdapter = new SearchingItemAdapter(this, searchData);
            recyclerViewSearches.setAdapter(searchingItemAdapter);
        }
    }

    private void getSearchesList() {
        if (HomeFragment.isSearchFirstClick) {
            LoginActivity.firebase.getSearchData(new OnGetDataListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess() {
                    HomeFragment.isSearchFirstClick = false;
                    if (LoginActivity.firebase.searchList.size() == 0) {
                        textViewNoSearches.setVisibility(View.VISIBLE);
                    }
                    else {
                        setRecyclerViewSearchesAdapter();
                    }
                }
            });
        }
        else {
            setRecyclerViewSearchesAdapter();
        }
    }

    public void setSearchInputFromUp (String input) {
        editTextSearchInput.setText(input);
    }

    public void deleteSearches () {
        textViewDeleteSearches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.firebase.removeSearchData(new OnGetDataListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess() {
                        recyclerViewSearches.removeAllViewsInLayout();
                        recyclerViewSearches.invalidate();
                        textViewNoSearches.setVisibility(View.VISIBLE);
                        textViewDeleteSearches.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }

    private void startSearchingInput() {
        editTextSearchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER) &&
                        !editTextSearchInput.getText().toString().equals("")) {
                    HomeFragment.isSearchFirstClick = true;
                    if (!checkIfSearchExist()) {
                        LoginActivity.firebase.addSearchDataToFirebase(editTextSearchInput.getText().toString());
                        searchData.add(editTextSearchInput.getText().toString());
                    }

                    queryResult = new ArrayList<>();
                    LoginActivity.firebase.querySearch(queryResult, editTextSearchInput.getText().toString(), new OnGetDataListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess() {
                            forwardSearchResultActivity();
                        }
                    });
                    return true;
                }
                return false;
            }
        });
    }

    private boolean checkIfSearchExist() {
        for (SearchString search : LoginActivity.firebase.searchList) {
            if (search.getDetail().equals(editTextSearchInput.getText().toString())) {
                return true;
            }
        }
        return false;
    }

    private void forwardSearchResultActivity() {
        Intent intent = new Intent(SearchingActivity.this, SearchResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("searchInput", editTextSearchInput.getText());
        startActivity(intent);
    }

    void loadLanguage() {
        String langPref = "lang_code";
        SharedPreferences prefs = getSharedPreferences("MyPref",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");

        Log.e("language", language);

        Locale locale = new Locale(language);
        locale.setDefault(locale);

        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}