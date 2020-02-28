package com.example.pokedexv1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.pokedexv1.PokemonAPI.PokemonAPI;
import com.example.pokedexv1.model.Pokemon;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {

    private List<Pokemon> pokemonList;
    private Context context;
    private RequestQueue requestQueue;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView pokemonCardView;
        public ImageView pokemonSpriteImageView;
        public TextView pokemonNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            pokemonCardView = itemView.findViewById(R.id.card_pokemon_mini);
            pokemonSpriteImageView = itemView.findViewById(R.id.image_pokemon_sprite);
            pokemonNameTextView = itemView.findViewById(R.id.text_pokemon_name);
        }
    }

    public PokemonAdapter(Context context, List<Pokemon> pokemonList, RequestQueue requestQueue) {
        this.pokemonList = pokemonList;
        this.context = context;
        this.requestQueue = requestQueue;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pokemon_simple, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Pokemon pokemon = pokemonList.get(position);
        holder.pokemonNameTextView.setText(pokemon.getName());

        ImageRequest request = new ImageRequest(pokemon.getSpriteUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        holder.pokemonSpriteImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(request);

        holder.pokemonCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

}
