package net.villagerzock.createcoasterseats;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CreateCoasterSeatsConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLE_CREATIVE_TAB_ANIMATION = BUILDER
            .comment("Whether to animate the Creative Tab Colorfully")
            .define("enableCreativeTabAnimations", true);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
