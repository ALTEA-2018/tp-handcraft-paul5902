package com.miage.altea.bo.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miage.altea.bo.bo.PokemonType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PokemonTypeRepository {

    private List<PokemonType> pokemons;

    public PokemonTypeRepository() {
        try {
            var pokemonsStream = this.getClass().getResourceAsStream("/pokemons.json");

            var objectMapper = new ObjectMapper();

            var pokemonsArray = objectMapper.readValue(pokemonsStream, PokemonType[].class);
            this.pokemons = Arrays.asList(pokemonsArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PokemonType findPokemonById(int id) {
        for(PokemonType pkm : pokemons) {
            if(pkm.getId() == id) {
                return pkm;
            }
        }
        return null;
    }

    public PokemonType findPokemonByName(String name) {
        System.out.println("Loading Pokemon information for Pokemon name " + name);
        for(PokemonType pkm : pokemons) {
            if(pkm.getName().equals(name)) {
                return pkm;
            }
        }
        return null;
    }

    public List<PokemonType> findAllPokemon() {
        return pokemons;
    }
}