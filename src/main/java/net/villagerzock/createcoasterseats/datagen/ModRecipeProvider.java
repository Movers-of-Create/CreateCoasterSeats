package net.villagerzock.createcoasterseats.datagen;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.villagerzock.createcoasterseats.Createcoasterseats;
import net.villagerzock.createcoasterseats.registry.ModFluids;
import net.villagerzock.createcoasterseats.registry.ModItems;
import net.villagerzock.createcoasterseats.registry.ModRestrictors;
import net.villagerzock.createcoasterseats.restrictions.RestrictorType;


import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    private static final TagKey<Item> PLASTICS = TagKey.create(
            Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "plastics"));

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        restrictor(ModRestrictors.BASIC.get(), output);
        restrictor(ModRestrictors.LAPBAR.get(), output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,
                        ModItems.COLORED_BLOCKS.get(DyeColor.LIGHT_BLUE).get())
                .requires(PLASTICS)
                .requires(ItemTags.WOODEN_SLABS)
                .unlockedBy("has_plastic", has(PLASTICS))
                .save(output, recipeId("crafting/light_blue_plastic_seat"));

        // Crushing wheels also accept milling recipes.
        new StandardProcessingRecipe.Builder<>(MillingRecipe::new, recipeId("sculk_powder"))
                .require(Items.SCULK)
                .output(ModItems.SCULK_POWDER.get(), 4)
                .output(0.5F, Items.SCULK)
                .duration(100)
                .build(output);

        new StandardProcessingRecipe.Builder<>(MixingRecipe::new, recipeId("organic_mass"))
                .require(ModItems.SCULK_POWDER.get())
                .require(ModItems.SCULK_POWDER.get())
                .require(ModItems.SCULK_POWDER.get())
                .require(ModItems.SCULK_POWDER.get())
                .require(ModItems.SCULK_POWDER.get())
                .require(ModItems.SCULK_POWDER.get())
                .require(ModItems.SCULK_POWDER.get())
                .require(ModItems.SCULK_POWDER.get())
                .require(SizedFluidIngredient.of(Fluids.WATER, 250))
                .output(ModItems.ORGANIC_MASS.get())
                .requiresHeat(HeatCondition.HEATED)
                .duration(100)
                .build(output);

        new StandardProcessingRecipe.Builder<>(MixingRecipe::new, recipeId("polymer_resin"))
                .require(ModItems.ORGANIC_MASS.get())
                .output(ModItems.POLYMER_RESIN.get())
                .requiresHeat(HeatCondition.SUPERHEATED)
                .duration(100)
                .build(output);

        new StandardProcessingRecipe.Builder<>(MixingRecipe::new, recipeId("molten_plastic"))
                .require(ModItems.POLYMER_RESIN.get())
                .require(Items.MAGMA_CREAM)
                .output(ModFluids.MOLTEN_PLASTIC.get(), 500)
                .requiresHeat(HeatCondition.SUPERHEATED)
                .duration(100)
                .build(output);

        // Fluid pressing is a basin compacting recipe, rather than item pressing.
        new StandardProcessingRecipe.Builder<>(CompactingRecipe::new, recipeId("plastic_sheet"))
                .require(SizedFluidIngredient.of(ModFluids.MOLTEN_PLASTIC.get().getSource(), 1000))
                .output(ModItems.PLASTIC_SHEET.get(), 4)
                .build(output);

        new StandardProcessingRecipe.Builder<>(MixingRecipe::new, recipeId("sculk"))
                .require(Items.SCULK_CATALYST)
                .require(Ingredient.of(AllItems.EXP_NUGGET, Items.EXPERIENCE_BOTTLE))
                .output(Items.SCULK_CATALYST)
                .output(Items.SCULK)
                .build(output);
    }

    private static ResourceLocation recipeId(String name) {
        return ResourceLocation.fromNamespaceAndPath(Createcoasterseats.MOD_ID, name);
    }

    public void restrictor(RestrictorType restrictorType, RecipeOutput output){
        SingleItemRecipeBuilder.stonecutting(
                Ingredient.of(AllBlocks.METAL_GIRDER.get().asItem()),
                RecipeCategory.BUILDING_BLOCKS,
                restrictorType.getItem(),
                4
        )
                .unlockedBy("has_metal_girder", has(AllBlocks.METAL_GIRDER.get()))
                .save(output, BuiltInRegistries.ITEM.getKey(restrictorType.getItem())
                        .withSuffix("_from_stonecutting"));
    }
}
