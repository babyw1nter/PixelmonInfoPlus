package io.github.hhui64.PixelmonInfoPlus.gui;

import net.minecraft.util.ResourceLocation;

public class PokemonIcon {
    public String pokemonNumberID;
    public Boolean isShiny;

    public PokemonIcon(String pokemonNumberID, Boolean isShiny) {
        this.pokemonNumberID = pokemonNumberID;
        this.isShiny = isShiny;
    }

    public ResourceLocation getResourceLocation() {
        String path = this.isShiny ? "textures/sprites/shinypokemon/" : "textures/sprites/pokemon/";
        return new ResourceLocation("pixelmon", path + this.pokemonNumberID + ".png");
    }
}
