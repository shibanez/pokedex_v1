package com.example.pokedexv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.pokedexv1.PokemonAPI.PokemonSingleton;
import com.example.pokedexv1.model.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonDetailActivity extends AppCompatActivity {

    private ImageView pokemonImageView;
    private ImageView pokemonTypeBGImageView;

    private RequestQueue requestQueue = PokemonSingleton.getInstance(this).getRequestQueue();
    private Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);

        pokemon = new Pokemon(getIntent().getStringExtra("POKEMON_NAME"));

        getPokemonDetails(pokemon.getName());

        //pokemonNameTextView.setText(pokemon.getName());

    }

    private void setPokemonGIF(String pokemonName) {
        pokemonImageView = findViewById(R.id.image_pokemon_detail_image);
        String pokemonGifUrl = getResources().getString(R.string.gif_link) +
                        pokemonName + ".gif";
        Glide.with(this)
                .load(pokemonGifUrl)
                .into(pokemonImageView);
    }

    private void getPokemonDetails (String pokemonName) {
        String pokemonUrl = getResources().getString(R.string.pokemon_api_base_url) +
                "pokemon/" + pokemonName;
        StringRequest pokemonInfoRequest = new StringRequest(Request.Method.GET, pokemonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parsePokemonDetailsResponse(response);
                        setBackgroundFromType(pokemon.getType1());
                        setPokemonGIF(pokemon.getName());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PokemonDetailActivity.this, "Pokemon not found!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

        requestQueue.add(pokemonInfoRequest);
    }

    private void parsePokemonDetailsResponse(String response) {
        try {
            JSONObject pokemonJson = new JSONObject(response);
            JSONArray pokemonTypes = pokemonJson.getJSONArray("types");
            int len = pokemonTypes.length();
            for (int i = 0; i < len; i++) {
                JSONObject pokemonType = pokemonTypes.getJSONObject(i);
                if (pokemonType.getInt("slot") == 2) {
                    pokemon.setType2(pokemonType.getJSONObject("type").getString("name"));
                } else {
                    pokemon.setType1(pokemonType.getJSONObject("type").getString("name"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setBackgroundFromType(String type) {
        pokemonTypeBGImageView = findViewById(R.id.image_pokemon_detail_bg);
        
        int bgResourceID = getResources().getIdentifier("bg_" + type, "drawable", getPackageName());
        if (bgResourceID != 0) {
            pokemonTypeBGImageView.setImageDrawable(getDrawable(bgResourceID));
        } else {
            Toast.makeText(PokemonDetailActivity.this, pokemon.getType1(), Toast.LENGTH_SHORT).show();
            Toast.makeText(PokemonDetailActivity.this, "No background resource", Toast.LENGTH_SHORT).show();
        }
    }
}
