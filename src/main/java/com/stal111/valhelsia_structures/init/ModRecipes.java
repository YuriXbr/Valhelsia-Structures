package com.stal111.valhelsia_structures.init;

import com.stal111.valhelsia_structures.ValhelsiaStructures;
import com.stal111.valhelsia_structures.recipe.AxeCraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

/**
 * Recipes
 * Valhelsia Structures - com.stal111.valhelsia_structures.init.ModRecipes
 *
 * @author Valhelsia Team
 * @version 14.0.4
 * @since 2020-06-01
 */
public class ModRecipes {

    public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, ValhelsiaStructures.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<?>> AXE_CRAFTING_SERIALIZER = register("axe_crafting", new SpecialRecipeSerializer<>(AxeCraftingRecipe::new));

    @SuppressWarnings("SameParameterValue")
    @Nonnull
    private static RegistryObject<IRecipeSerializer<?>> register(@Nonnull String name, @Nonnull IRecipeSerializer<?> serializer) {
        return SERIALIZERS.register(name, () -> serializer);
    }
}
