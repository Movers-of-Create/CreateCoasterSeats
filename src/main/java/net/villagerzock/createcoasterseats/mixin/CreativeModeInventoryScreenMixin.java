package net.villagerzock.createcoasterseats.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.Mod;
import net.villagerzock.createcoasterseats.registry.ModCreativeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {
    @Shadow
    private static CreativeModeTab selectedTab;

    @Redirect(method = "renderTabButton", at= @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;getIconItem()Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack createcoasterseats$renderAnimatedIcon(CreativeModeTab instance){
        if (instance == ModCreativeTabs.MAIN.get()){
            return ModCreativeTabs.getAnimatedIcon();
        }
        return instance.getIconItem();
    }

    @Inject(method = "renderBg", at=@At("HEAD"))
    private void createcoasterseats$renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci){
        createcoasterseats$setTintIfSeatsTab(guiGraphics);
    }

    @Inject(method = "renderTabButton", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.BEFORE))
    private void createcoasterseats$resetColorBeforeItemRender(GuiGraphics guiGraphics, CreativeModeTab creativeModeTab, CallbackInfo ci){
        guiGraphics.setColor(1f,1f,1f,1f);
    }
    @Inject(method = "renderTabButton", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.AFTER))
    private void createcoasterseats$setColorAfterItemRender(GuiGraphics guiGraphics, CreativeModeTab creativeModeTab, CallbackInfo ci){
        createcoasterseats$setTintIfSeatsTab(guiGraphics);
    }

    @Unique
    private void createcoasterseats$setTintIfSeatsTab(GuiGraphics guiGraphics) {
        if (selectedTab == ModCreativeTabs.MAIN.get()){
            int argb = ModCreativeTabs.getAnimatedTint().getTextureDiffuseColor();
            float a = ((argb >> 24) & 0xFF) / 255.0f;
            float r = ((argb >> 16) & 0xFF) / 255.0f;
            float g = ((argb >> 8) & 0xFF) / 255.0f;
            float b = (argb & 0xFF) / 255.0f;
            guiGraphics.setColor(r,g,b,a);
        }
    }

    @Unique
    private boolean createcoasterseats$isDarkColor(DyeColor dyeColor){
        return switch (dyeColor){
            case RED, LIME, WHITE -> false;
            case BLUE, GRAY, BLACK, BROWN, GREEN, PURPLE, MAGENTA, ORANGE, YELLOW, LIGHT_BLUE, LIGHT_GRAY, PINK, CYAN -> true;
        };
    }

    @Redirect(method = "renderLabels", at= @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab;getLabelColor()I"))
    private int createcoasterseats$changeLabelColor(CreativeModeTab instance){
        if (instance == ModCreativeTabs.MAIN.get() && createcoasterseats$isDarkColor(ModCreativeTabs.getAnimatedTint())){
            return 0xFFFFFFFF;
        }
        return Objects.requireNonNullElse(ChatFormatting.DARK_GRAY.getColor(), 0);
    }
}
