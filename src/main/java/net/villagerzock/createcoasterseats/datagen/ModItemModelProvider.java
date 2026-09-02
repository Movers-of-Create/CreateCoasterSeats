package net.villagerzock.createcoasterseats.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.simibubi.create.Create;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.villagerzock.createcoasterseats.Createcoasterseats;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModItemModelProvider extends ItemModelProvider {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final PackOutput output;
    private final ExistingFileHelper existingFileHelper;
    private CachedOutput cachedOutput;


    public ModItemModelProvider(
            PackOutput output,
            String modid,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, modid, existingFileHelper);

        this.output = output;
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.cachedOutput = cache;
        return super.run(cache);
    }

    @Override
    protected void registerModels() {
        registerSeat(
                "restrictor_seat",
                "restrictor/default_restrictor",
                0.0F,
                0.0F,
                0.0F,
                Map.of(
                        "7", "create:block/seat/top_%1$s",
                        "22", "create:block/seat/side_%1$s"
                )
        );

        registerSeat(
                "lapbar_seat",
                "restrictor/lapbar",
                0.0F,
                20.0F / 16.0F,
                -2.0F / 16.0F,
                Map.of(
                        "7", "create:block/seat/top_%1$s",
                        "22", "create:block/seat/side_%1$s"
                )
        );
    }

    private void registerSeat(
            String seatModel,
            String restrictorModel,
            float restrictorX,
            float restrictorY,
            float restrictorZ,
            Map<String, String> textures
    ) {
        generateCombinedSeatModel(
                seatModel,
                restrictorModel,
                restrictorX,
                restrictorY,
                restrictorZ
        );

        for (DyeColor color : DyeColor.values()) {
            generateColoredSeat(
                    color,
                    seatModel,
                    textures
            );
        }
    }

    private void generateColoredSeat(
            DyeColor color,
            String seatModel,
            Map<String, String> textures
    ) {
        String colorName = color.getName();
        String modelName = colorName + "_" + seatModel;

        ItemModelBuilder model = getBuilder(modelName)
                .parent(
                        new ModelFile.UncheckedModelFile(
                                modLoc("item/" + seatModel)
                        )
                );

        for (Map.Entry<String, String> entry : textures.entrySet()) {
            model.texture(
                    entry.getKey(),
                    ResourceLocation.parse(
                            entry.getValue().formatted(colorName)
                    )
            );
        }
    }

    private void generateCombinedSeatModel(
            String seatModel,
            String restrictorModel,
            float restrictorX,
            float restrictorY,
            float restrictorZ
    ) {
        JsonObject seatJson = readModel(
                modLoc("block/" + seatModel)
        );

        JsonObject restrictorJson = readModel(
                modLoc("block/" + restrictorModel)
        );

        JsonObject result = seatJson.deepCopy();

        JsonArray resultElements = getOrCreateElements(result);

        if (restrictorJson.has("elements")) {
            for (JsonElement element : restrictorJson.getAsJsonArray("elements")) {
                JsonObject copiedElement = element
                        .getAsJsonObject()
                        .deepCopy();

                offsetElement(
                        copiedElement,
                        restrictorX,
                        restrictorY,
                        restrictorZ
                );

                resultElements.add(copiedElement);
            }
        }

        mergeTextures(
                result,
                restrictorJson
        );

        writeItemModel(
                seatModel,
                result
        );
    }

    private JsonObject readModel(ResourceLocation location) {
        try {
            Resource resource = existingFileHelper.getResource(
                    location,
                    PackType.CLIENT_RESOURCES,
                    ".json",
                    "models"
            );

            try (Reader reader = resource.openAsReader()) {
                return JsonParser
                        .parseReader(reader)
                        .getAsJsonObject();
            }
        } catch (IOException exception) {
            throw new RuntimeException(
                    "Could not read model: " + location,
                    exception
            );
        }
    }

    private JsonArray getOrCreateElements(JsonObject model) {
        if (model.has("elements")) {
            return model.getAsJsonArray("elements");
        }

        JsonArray elements = new JsonArray();

        model.add(
                "elements",
                elements
        );

        return elements;
    }

    private void mergeTextures(
            JsonObject target,
            JsonObject source
    ) {
        if (!source.has("textures")) {
            return;
        }

        JsonObject targetTextures;

        if (target.has("textures")) {
            targetTextures = target.getAsJsonObject("textures");
        } else {
            targetTextures = new JsonObject();

            target.add(
                    "textures",
                    targetTextures
            );
        }

        JsonObject sourceTextures = source.getAsJsonObject("textures");

        for (Map.Entry<String, JsonElement> entry : sourceTextures.entrySet()) {
            targetTextures.add(
                    entry.getKey(),
                    entry.getValue().deepCopy()
            );
        }
    }

    private void offsetElement(
            JsonObject element,
            float x,
            float y,
            float z
    ) {
        offsetVector(
                element,
                "from",
                x,
                y,
                z
        );

        offsetVector(
                element,
                "to",
                x,
                y,
                z
        );

        if (element.has("rotation")) {
            JsonObject rotation = element.getAsJsonObject("rotation");

            offsetVector(
                    rotation,
                    "origin",
                    x,
                    y,
                    z
            );
        }
    }

    private void offsetVector(
            JsonObject object,
            String key,
            float x,
            float y,
            float z
    ) {
        if (!object.has(key)) {
            return;
        }

        JsonArray vector = object.getAsJsonArray(key);

        float oldX = vector.get(0).getAsFloat();
        float oldY = vector.get(1).getAsFloat();
        float oldZ = vector.get(2).getAsFloat();

        JsonArray result = new JsonArray();

        result.add(oldX + x);
        result.add(oldY + y);
        result.add(oldZ + z);

        object.add(
                key,
                result
        );
    }

    private void writeItemModel(
            String name,
            JsonObject model
    ) {
        Path path = output
                .getOutputFolder()
                .resolve(
                        "assets/createcoasterseats/models/item/"
                                + name
                                + ".json"
                );

        try {
            Files.createDirectories(path.getParent());

            System.out.println("Creating Item Model File: " + path.toAbsolutePath());

            DataProvider.saveStable(
                    cachedOutput,
                    model,
                    path
            );
        } catch (IOException exception) {
            throw new RuntimeException(
                    "Could not write item model: " + path,
                    exception
            );
        }
    }
}