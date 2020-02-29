package com.example.pokedexv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.example.pokedexv1.PokemonAPI.PokemonSingleton;
import com.example.pokedexv1.model.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PokemonListActivity extends AppCompatActivity {

    private List<Pokemon> pokemonList = new ArrayList<>();
    private RecyclerView pokemonGridRecyclerView;
    private PokemonAdapter pokemonAdapter;
    private RequestQueue requestQueue;
    private EndlessRecyclerViewScrollListener scrollListener;
    GridLayoutManager mLayoutManager;
    private String nextPokemonSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);

        requestQueue = PokemonSingleton.getInstance(this).getRequestQueue();
        setUpPokemonGridRecyclerView();

        String initialPokemonSetUrl = getResources().getString(R.string.pokemon_api_base_url) + "pokemon";
        getPokemonList(initialPokemonSetUrl);
    }

    private void setUpPokemonGridRecyclerView() {
        pokemonGridRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_pokemon_grid);
        mLayoutManager = new GridLayoutManager(this, 2);
        pokemonGridRecyclerView.setLayoutManager(mLayoutManager);
        pokemonAdapter = new PokemonAdapter(PokemonListActivity.this, pokemonList);
        pokemonGridRecyclerView.setAdapter(pokemonAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getPokemonList(nextPokemonSet);
            }
        };

        pokemonGridRecyclerView.addOnScrollListener(scrollListener);
    }

    private void getPokemonList(String url) {
        StringRequest pokemonSetRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject pokemonsJson = new JSONObject(response);
                            JSONArray pokemonArray = pokemonsJson.getJSONArray("results");
                            nextPokemonSet = pokemonsJson.getString("next");

                            int len = pokemonArray.length();
                            for (int i = 0; i < len; i++) {
                                String pokemonName = pokemonArray.getJSONObject(i).getString("name");
                                String pokemonUrl = pokemonArray.getJSONObject(i).getString("url");
                                String pokemonSpriteUrl = getResources().getString(R.string.sprite_link) +
                                        (pokemonList.size() + 1) + ".png";
                                String pokemonGifUrl = getResources().getString(R.string.gif_link) +
                                        pokemonName + ".gif";
                                Pokemon pokemon = new Pokemon(pokemonName, pokemonUrl, pokemonSpriteUrl, pokemonGifUrl);
                                pokemonList.add(pokemon);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pokemonAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PokemonListActivity.this, "Error fetching from Pokemon API", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(pokemonSetRequest);
    }
}
