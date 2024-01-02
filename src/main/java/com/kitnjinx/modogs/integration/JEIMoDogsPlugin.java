package com.kitnjinx.modogs.integration;

import com.kitnjinx.modogs.MoDogs;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEIMoDogsPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MoDogs.MOD_ID, "jei_plugin");
    }
}
