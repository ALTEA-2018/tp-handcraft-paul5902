package com.miage.altea.bo.controller;

import com.miage.altea.bo.annotation.Controller;
import com.miage.altea.bo.annotation.RequestMapping;
import com.miage.altea.bo.bo.PokemonType;
import com.miage.altea.bo.repository.PokemonTypeRepository;

import java.util.Map;

@Controller
public class PokemonTypeController {
    private PokemonTypeRepository repository = new PokemonTypeRepository();

    @RequestMapping(uri="/pokemons")
    public PokemonType getPokemon(Map<String,String[]> parameters){
            if(parameters == null) {
                throw new IllegalArgumentException("parameters should not be empty");
            }
            if(parameters.get("name") != null) {
                return repository.findPokemonByName(parameters.get("name")[0]);
            }
            else if(parameters.get("id") != null) {
                return repository.findPokemonById(Integer.parseInt(parameters.get("id")[0]));
            }
            else {
                throw new IllegalArgumentException("unknown parameter");
            }
    }
}
