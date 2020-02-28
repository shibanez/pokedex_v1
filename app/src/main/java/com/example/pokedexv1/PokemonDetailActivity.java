package com.example.pokedexv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.pokedexv1.PokemonAPI.PokemonSingleton;
import com.example.pokedexv1.model.Pokemon;

public class PokemonDetailActivity extends AppCompatActivity {

    private RequestQueue requestQueue = PokemonSingleton.getInstance(this).getRequestQueue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);

        Pokemon pokemon = (Pokemon) getIntent().getSerializableExtra("POKEMON");

        final ImageView pokemonImageView = findViewById(R.id.image_pokemon_detail_image);
        //TextView pokemonNameTextView = findViewById(R.id.text_pokemon_detail_name);

        //pokemonNameTextView.setText(pokemon.getName());
        ImageRequest request = new ImageRequest(pokemon.getImageUrl(),
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

        requestQueue.add(request);
    }
}
