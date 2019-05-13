package com.outlook.movieappv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.outlook.movieappv2.Adapters.MovieSearchAdapter;
import com.outlook.movieappv2.Client.RetrofitClient;
import com.outlook.movieappv2.Interfaces.RetrofitService;
import com.outlook.movieappv2.Model.GenreResponse;
import com.outlook.movieappv2.Model.GenreResponseResults;
import com.outlook.movieappv2.Model.MovieResponse;
import com.outlook.movieappv2.Model.MovieResponseResults;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton homeButton;
    private AppCompatButton favoritesButton;
    private AppCompatEditText searchBarEditText;
    private NiceSpinner genreSpinner;
    private ImageButton searchButton;
    private RecyclerView resultsRecyclerView;
    private Button resetButton;

    private RetrofitService retrofitService;

    private MovieResponse movieResponse;
    private List<MovieResponseResults> movieResponseResults;
    private List<MovieResponseResults> favoritesList;
    private GenreResponse genreResponse;
    private GenreResponseResults selectedGenre;

    private final String searchBarHintString = "Enter a movie title...";
    private final String genreLanguage = "en-US";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this prevents resizing of cardview when keyboard pops up
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setupToolbar();

        // initialize all activity objects
        searchBarEditText = findViewById(R.id.searchBarEditText);
        searchBarEditText.setHint(searchBarHintString);

        genreSpinner = findViewById(R.id.genreSpinner);
        searchButton = findViewById(R.id.searchButton);
        resetButton = findViewById(R.id.resetButton);

        // get and initialize recyclerview to display movies
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView);
        resultsRecyclerView.setHasFixedSize(true);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // initiate paperdb
        Paper.init(this);

        // check if a movie got favorited while we were in a different activity
        checkForNewFavorites();

        // get data from offline db if cached, or online if not
        getDataFromDBOrAPI();
        setupListeners();
    }

    private void setupToolbar() {
        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContext() != MainActivity.this) {
                    Intent goHomeIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(goHomeIntent);
                }
            }
        });

        favoritesButton = findViewById(R.id.favoritesButton);
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FavoritesActivity.class);
                intent.putParcelableArrayListExtra("favorites_list", (ArrayList<? extends Parcelable>) favoritesList);
                intent.putParcelableArrayListExtra("movie_response_results", (ArrayList<? extends Parcelable>) movieResponseResults);
                startActivity(intent);
            }
        });
    }

    private void checkForNewFavorites() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            favoritesList = intent.getExtras().getParcelableArrayList("favorites_list");

            // write new list of favorites to offline db
            MovieResponse favoritesResponse = new MovieResponse(1, favoritesList.size(), 1, favoritesList);
            Paper.book().write("favorites_response", new Gson().toJson(favoritesResponse));
        }
    }

    private void getDataFromDBOrAPI() {
        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
        movieResponse = null;

        // if genre spinner position is cached, retrieve it
        if (Paper.book().read("spinnerPosition") != null) {
            int position = Paper.book().read("spinnerPosition");
            genreSpinner.setSelectedIndex(position);
        }

        // retrieve genre list from paperdb if cache is not empty
        if (Paper.book().read("genres") != null) {
            String cachedResults = Paper.book().read("genres");
            genreResponse = new Gson().fromJson(cachedResults, GenreResponse.class);
            setupGenreSpinner(genreResponse);
        } else {
            // if cache is empty, make a GET request and store info in offline db
            Call<GenreResponse> genreResponseCall =
                    retrofitService.getMovieGenres(genreLanguage, BuildConfig.THE_MOVIE_DB_API_KEY);
            genreResponseCall.enqueue(new Callback<GenreResponse>() {
                @Override
                public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                    // on response, get the body and populate results list
                    genreResponse = response.body();
                    setupGenreSpinner(genreResponse);

                    // now store results in paperdb to access offline
                    Paper.book().write("genres", new Gson().toJson(genreResponse));
                }

                @Override
                public void onFailure(Call<GenreResponse> call, Throwable t) {
                    System.out.println("Failed to get genres from API...");
                }
            });
        }

        // retrieve favorites list from paperdb if caches is not empty
        if (Paper.book().read("favorites_response") != null) {
            String cachedResults = Paper.book().read("favorites_response");
            MovieResponse favoritesResponse = new Gson().fromJson(cachedResults, MovieResponse.class);
            favoritesList = favoritesResponse.getResults();
        } else {
            // if no list in caches, create empty list
            favoritesList = new ArrayList<>();
        }

        // retrieve results from paperdb if caches is not empty
        if (Paper.book().read("cache") != null) {
            String cachedResults = Paper.book().read("cache");
            movieResponse = new Gson().fromJson(cachedResults, MovieResponse.class);
            if (movieResponse != null) {
                movieResponseResults = movieResponse.getResults();
                processMovieResponseWithFilter(movieResponseResults);
            }
        } else {
            // if cache is empty, make a GET request and store in offline db
            Call<MovieResponse> movieResponseCall = retrofitService
                    .getMoviesOnStart(BuildConfig.RELEASE_DATE_FROM, BuildConfig.RELEASE_DATE_TO,
                            BuildConfig.THE_MOVIE_DB_API_KEY);
            movieResponseCall.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    // on response, get the body and populate results list
                    movieResponse = response.body();
                    if (movieResponse != null) {
                        movieResponseResults = movieResponse.getResults();
                        processMovieResponseWithFilter(movieResponseResults);
                    }

                    // now store results in paperdb to access offline
                    Paper.book().write("cache", new Gson().toJson(movieResponse));
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    System.out.println("Failed to get movies from API...");
                }
            });

        }
    }

    private void setupListeners() {
        genreSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide soft keyboard if its up
                hideSoftKeyboard(MainActivity.this);
            }
        });

        // filter recyclerview movie list based on spinner genere selection
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSpinnerText = genreSpinner.getSelectedItem().toString();
                selectedGenre = genreResponse.getGenreObjectFromSpinnerText(selectedSpinnerText);
                processMovieResponseWithFilter(movieResponseResults);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        // search online moviedb when search button is clicked
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMovieSearch();
            }
        });

        // search online moviedb when enter button is clicked on soft keyboard
        searchBarEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doMovieSearch();
                    return true;
                }
                return false;
            }
        });

        // setup reset button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGenre = null;
                searchBarEditText.setHint(searchBarHintString);
                getDataFromDBOrAPI();
            }
        });
    }

    private void setupGenreSpinner(GenreResponse genreResponse) {
        if (genreResponse != null) {
            List<GenreResponseResults> genreResponseResults = genreResponse.getGenres();
            List<String> allGenres = new ArrayList<>();
            allGenres.add("All genres");
            for (GenreResponseResults each : genreResponseResults) {
                allGenres.add(each.getName());
            }
            genreSpinner.attachDataSource(allGenres);
        }
    }

    private void setupRecyclerView(List<MovieResponseResults> movieResponseResults,
                                   List<MovieResponseResults> ResponseResults) {
        MovieSearchAdapter movieSearchAdapter = new MovieSearchAdapter(MainActivity.this,
                R.layout.search_layout_items, movieResponseResults, ResponseResults);
        resultsRecyclerView.setAdapter(movieSearchAdapter);

        // animation for recyclerview loading
        LayoutAnimationController controller = AnimationUtils.
                loadLayoutAnimation(MainActivity.this, R.anim.layout_slide_right);

        resultsRecyclerView.setLayoutAnimation(controller);
        resultsRecyclerView.scheduleLayoutAnimation();
    }

    private void doMovieSearch() {
        if (searchBarEditText.getText() != null) {
            // done with keyboard
            hideSoftKeyboard(MainActivity.this);
            String query = searchBarEditText.getText().toString();
            // trim and replace all spaces with'+' using regex
            String[] words = query.trim().split("[\" \"]+");
            if (words.length != 0 && !words[0].equals("")) {
                searchBarEditText.setText("");
                searchBarEditText.setHint("Showing movies with '" + query +"'");

                // query the offline db
                movieResponseResults = getFilteredResultsByTitle(words);
                processMovieResponseWithFilter(movieResponseResults);
            }
        }
    }

    private void processMovieResponseWithFilter(List<MovieResponseResults> responseResults) {
        List<MovieResponseResults> filteredResults;
        if (selectedGenre != null) {
            // a particular genre selected
            int selectedGenreID = selectedGenre.getId();
            filteredResults = getFilteredResultsByGenre(selectedGenreID);
        } else {
            // "All genres" selected
            filteredResults = responseResults;
        }

        if (filteredResults.size() != 0) {
            // setup recyclerview to display filtered results
            setupRecyclerView(filteredResults, favoritesList);
            //Paper.book().write("spinnerPosition", genreSpinner.getSelectedIndex());
        } else {
            // if no results match, display Toast text
            Toast.makeText(getApplicationContext(), "No movies to display...", Toast.LENGTH_SHORT).show();
        }
    }

    // i know this isn't the most efficient/comprehensive search algorithm, but its better than making
    // several http calls to the db
    private List<MovieResponseResults> getFilteredResultsByTitle(String[] words) {
        List<MovieResponseResults> filteredResults = new ArrayList<>();
        boolean contains = true;
        // check if movie title that contain ALL words in string
        if (words.length > 1) {
            for (MovieResponseResults each : movieResponseResults) {
                for (String word : words) {
                    if (!each.getTitle().toLowerCase().contains(word.toLowerCase())) {
                        contains = false;
                        break;
                    }
                }
                if (contains) filteredResults.add(each);
                contains = true;
            }
        }
        // check if movie title contains any of the word, giving preference to earlier words
        for (String word : words) {
            for (MovieResponseResults each : movieResponseResults) {
                if (each.getTitle().toLowerCase().contains(word.toLowerCase())) {
                    filteredResults.add(each);
                }
            }
        }
        return removeDuplicates(filteredResults);
    }

    private List<MovieResponseResults> removeDuplicates(List<MovieResponseResults> list) {
        if (list == null || list.size() <= 1) return list;
        Set<MovieResponseResults> set = new TreeSet<>(new Comparator<MovieResponseResults>() {
            @Override
            public int compare(MovieResponseResults a, MovieResponseResults b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        for (MovieResponseResults each : list) {
            set.add(each);
        }
        List<MovieResponseResults> result = new ArrayList<>();
        for (MovieResponseResults each : set) {
            result.add(each);
        }
        return result;
    }

    private List<MovieResponseResults> getFilteredResultsByGenre(int selectedGenreID) {
        List<MovieResponseResults> filteredResults = new ArrayList<>();
        for (MovieResponseResults each : movieResponseResults) {
            if (each.getGenre_ids().contains(selectedGenreID)) {
                filteredResults.add(each);
            }
        }
        return filteredResults;
    }

    private void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    // couldnt get mainactivity to retain new favorites list without refreshing
//        @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == 1) {
//            if(resultCode == RESULT_OK){
//                favoritesList = intent.getExtras().getParcelableArrayList("favorites_list");
//
//                // write new list of favorites to offline db
//                MovieResponse favoritesResponse = new MovieResponse(1, favoritesList.size(), 1, favoritesList);
//                Paper.book().write("favorites_response", new Gson().toJson(favoritesResponse));
//            }
//            if (resultCode == RESULT_CANCELED) {
//                System.out.println("Hi! OnActivityResult() issue in MainActivity");
//            }
//        }
//    }

}
