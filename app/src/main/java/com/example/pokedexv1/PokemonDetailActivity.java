package com.example.pokedexv1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    private ImageView pokemonImageView, pokemonTypeBGImageView;
    private CardView pokemonType1Card, pokemonType2Card;
    private TextView pokemonNameTextView, pokemonType1TextView, pokemonType2TextView;

    private RequestQueue requestQueue = PokemonSingleton.getInstance(this).getRequestQueue();
    private Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);

        pokemon = new Pokemon(getIntent().getStringExtra("POKEMON_NAME"));

        pokemonImageView = findViewById(R.id.image_pokemon_detail_image);
        pokemonNameTextView = findViewById(R.id.text_pokemon_detail_name);
        pokemonTypeBGImageView = findViewById(R.id.image_pokemon_detail_bg);
        pokemonType1Card = findViewById(R.id.card_pokemon_type1);
        pokemonType2Card = findViewById(R.id.card_pokemon_type2);
        pokemonType1TextView = findViewById(R.id.text_pokemon_type1);
        pokemonType2TextView = findViewById(R.id.text_pokemon_type2);

        getPokemonDetails(pokemon.getName());

        //pokemonNameTextView.setText(pokemon.getName());

    }

    private void setPokemonGIF(String pokemonName) {
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
                        setTypeDetails(pokemon.getType1(), pokemon.getType2());
                        String pokemonName = pokemon.getName();
                        setPokemonGIF(pokemonName);
                        pokemonNameTextView.setText(capitalizeFirstLetter(pokemonName));
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

    private void setTypeDetails(String type1, String type2) {
        int bgResourceID = getResources().getIdentifier("bg_" + type1, "drawable", getPackageName());
        if (bgResourceID != 0) {
            pokemonTypeBGImageView.setImageDrawable(getDrawable(bgResourceID));
        } else {
            Toast.makeText(PokemonDetailActivity.this, pokemon.getType1(), Toast.LENGTH_SHORT).show();
            Toast.makeText(PokemonDetailActivity.this, "No background resource", Toast.LENGTH_SHORT).show();
        }
        int type1ResourceID = getResources().getIdentifier(type1 + "Type", "color", getPackageName());
        int type2ResourceID = getResources().getIdentifier(type2 + "Type", "color", getPackageName());

        pokemonType1Card.setCardBackgroundColor(getResources().getColor(type1ResourceID));
        pokemonType1TextView.setText(capitalizeFirstLetter(type1));

        if (type2 == null) {
            pokemonType2Card.setVisibility(View.GONE);
        } else {
            pokemonType2Card.setCardBackgroundColor(getResources().getColor(type2ResourceID));
            pokemonType2TextView.setText(capitalizeFirstLetter(type2));
        }
    }

    private String capitalizeFirstLetter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
