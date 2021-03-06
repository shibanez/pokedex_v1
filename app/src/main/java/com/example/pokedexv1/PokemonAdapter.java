package com.example.pokedexv1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.example.pokedexv1.PokemonAPI.PokemonSingleton;
import com.example.pokedexv1.model.Pokemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;


public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private List<Pokemon> pokemonList;
    private Context context;
    private RequestQueue requestQueue;

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        public CardView pokemonCardView;
        public ImageView pokemonSpriteImageView;
        public TextView pokemonNameTextView;

        public PokemonViewHolder(View itemView) {
            super(itemView);
            pokemonCardView = itemView.findViewById(R.id.card_pokemon_mini);
            pokemonSpriteImageView = itemView.findViewById(R.id.image_pokemon_sprite);
            pokemonNameTextView = itemView.findViewById(R.id.text_pokemon_name);
        }
    }

    public PokemonAdapter(Context context, List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
        this.context = context;
        this.requestQueue = PokemonSingleton.getInstance(context).getRequestQueue();
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pokemon_simple, parent, false);

        return new PokemonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PokemonViewHolder holder, int position) {
        final Pokemon pokemon = pokemonList.get(position);

        final String pokemonName = pokemon.getName();
        String capitalizedPokemonName = pokemonName.substring(0, 1).toUpperCase() + pokemonName.substring(1);
        holder.pokemonNameTextView.setText(capitalizedPokemonName);

        Glide.with(context)
                .load(pokemon.getSpriteUrl())
                .placeholder(R.drawable.ditto)
                .into(holder.pokemonSpriteImageView);

        holder.pokemonCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PokemonDetailActivity.class);
                intent.putExtra("POKEMON_NAME", pokemonName);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
