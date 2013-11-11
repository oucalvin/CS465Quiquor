package client.readOnly;

import client.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Neal Eric
 * Date: 11/10/13
 */

/**
 * PLEASE DO NOT CHANGE AS IT IS A REQUIRED CLASS TO INTERACT WITH THE BARTENDER
 */

public class Drink {
    private String name;
    private HashMap<Ingredient, Double> ingredients;

    public Drink(String name, HashMap<Ingredient, Double> ingredients)
    {
        this.name = name;
        this.ingredients = ingredients;
    }

    public double getTotalMetricWeight()
    {
        double totalVolume = 0;
        for (Double volume : ingredients.values())
        {
            totalVolume += volume;
        }

        // TODO: Density.
        return totalVolume;
    }

    public String getName()
    {
        return name;
    }

    public HashMap<Ingredient, Double> getIngredientMap()
    {
        return ingredients;
    }

    public boolean satisfy()
    {
        for (Ingredient ingredient : getIngredients())
        {
            if (!Main.pumpMap.containsKey(ingredient))
                return false;
        }
        return true;
    }

    public List<Ingredient> getIngredients()
    {
        return new ArrayList<>(this.ingredients.keySet());
    }
}
