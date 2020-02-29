package com.example.pokedexv1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokemonGridRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_pokemon_grid);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        pokemonGridRecyclerView.setLayoutManager(mLayoutManager);

        Cache cache = new DiskBasedCache(getCacheDir()); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);

        requestQueue = PokemonSingleton.getInstance(this).getRequestQueue();
        getPokemonList();

    }

    private void getPokemonList() {
        String url = getResources().getString(R.string.pokemon_api_base_url) + "pokemon";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject pokemonsJson = new JSONObject(response);
                            JSONArray pokemonArray = pokemonsJson.getJSONArray("results");

                            int len = pokemonArray.length();
                            for (int i = 0; i < len; i++) {
                                String pokemonName = pokemonArray.getJSONObject(i).getString("name");
                                String pokemonUrl = pokemonArray.getJSONObject(i).getString("url");
                                String pokemonSpriteUrl = getResources().getString(R.string.sprite_link) +
                                        ( i + 1 ) + ".png";
                                String pokemonGifUrl = getResources().getString(R.string.gif_link) +
                                        pokemonName + ".gif";
                                Pokemon pokemon = new Pokemon(pokemonName, pokemonUrl, pokemonSpriteUrl, pokemonGifUrl);
                                pokemonList.add(pokemon);
                            }
                            pokemonAdapter = new PokemonAdapter(PokemonListActivity.this, pokemonList);
                            pokemonGridRecyclerView.setAdapter(pokemonAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PokemonListActivity.this, "Error fetching from Pokemon API", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(stringRequest);
    }

}
