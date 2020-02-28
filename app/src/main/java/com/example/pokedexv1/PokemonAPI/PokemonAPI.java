package com.example.pokedexv1.PokemonAPI;

import android.content.Context;
import android.util.Log;
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
import com.android.volley.toolbox.Volley;
import com.example.pokedexv1.model.Pokemon;
import java.net.URL;

public class PokemonAPI {
// Start the queue

    private Context context;
    public static final String POKEMON_BASE_URL = "https://pokeapi.co/api/v2/";

    public PokemonAPI(Context context) {
        this.context = context;
    }

}
