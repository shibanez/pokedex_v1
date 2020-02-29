package com.example.pokedexv1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pokedexv1.PokemonAPI.PokemonSingleton;
import com.example.pokedexv1.model.Pokemon;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PokemonListActivity extends AppCompatActivity {

    private List<Pokemon> pokemonList = new ArrayList<>();
    private RecyclerView pokemonGridRecyclerView;
    private PokemonAdapter pokemonAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private GridLayoutManager mLayoutManager;

    private RequestQueue requestQueue;
    private String nextPokemonSet;

    private FloatingActionButton searchPokemonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);

        requestQueue = PokemonSingleton.getInstance(this).getRequestQueue();
        setUpPokemonGridRecyclerView();

        String initialPokemonSetUrl = getResources().getString(R.string.pokemon_api_base_url) + "pokemon";
        getPokemonList(initialPokemonSetUrl);
        
        setUpSearchPokemonButton();
    }

    private void setUpPokemonGridRecyclerView() {
        pokemonGridRecyclerView = findViewById(R.id.recycle_view_pokemon_grid);
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
                        parsePokemonSetResponse(response);
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

    private void parsePokemonSetResponse (String response) {
        try {
            JSONObject pokemonsJson = new JSONObject(response);
            JSONArray pokemonArray = pokemonsJson.getJSONArray("results");
            nextPokemonSet = pokemonsJson.getString("next");

            int len = pokemonArray.length();
            for (int i = 0; i < len; i++) {
                String pokemonName = pokemonArray.getJSONObject(i).getString("name");
                String pokemonSpriteUrl = getResources().getString(R.string.sprite_link) +
                        (pokemonList.size() + 1) + ".png";
                Pokemon pokemon = new Pokemon(pokemonName, pokemonSpriteUrl);
                pokemonList.add(pokemon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpSearchPokemonButton() {
        searchPokemonButton = findViewById(R.id.fab_search_pokemon);
        searchPokemonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PokemonListActivity.this);
                builder.setTitle("Search Pokemon");

                final EditText input = new EditText(PokemonListActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pokemonName = input.getText().toString().toLowerCase();
                        Intent intent = new Intent(PokemonListActivity.this, PokemonDetailActivity.class);
                        intent.putExtra("POKEMON_NAME", pokemonName);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }


}
