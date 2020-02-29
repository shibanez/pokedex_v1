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
import com.example.pokedexv1.PokemonAPI.PokemonSingleton;
import com.example.pokedexv1.model.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonDetailActivity extends AppCompatActivity {

    private RequestQueue requestQueue = PokemonSingleton.getInstance(this).getRequestQueue();
    private Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);

        pokemon = (Pokemon) getIntent().getSerializableExtra("POKEMON");

        final ImageView pokemonImageView = findViewById(R.id.image_pokemon_detail_image);
        final ImageView pokemonBGImageView = findViewById(R.id.image_pokemon_detail_bg);
        TextView pokemonNameTextView = findViewById(R.id.text_pokemon_detail_name);

        pokemonNameTextView.setText(pokemon.getName());


        ImageRequest imageRequest = new ImageRequest(pokemon.getImageUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        pokemonImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PokemonDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        StringRequest pokemonInfoRequest = new StringRequest(Request.Method.GET, pokemon.getInfoUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject pokemonsJson = new JSONObject(response);
                            JSONArray pokemonTypes = pokemonsJson.getJSONArray("types");
                            int len = pokemonTypes.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject pokemonType = pokemonTypes.getJSONObject(i);
                                if (pokemonType.getInt("slot") == 2) {
                                    pokemon.setType2(pokemonType.getJSONObject("type").getString("name"));
                                } else {
                                    pokemon.setType1(pokemonType.getJSONObject("type").getString("name"));
                                }
                            }
                            int bgResourceID = getResources().getIdentifier("bg_" + pokemon.getType1(), "drawable", getPackageName());
                            pokemonBGImageView.setImageDrawable(getDrawable(bgResourceID));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        requestQueue.add(imageRequest);
        requestQueue.add(pokemonInfoRequest);

    }
}
