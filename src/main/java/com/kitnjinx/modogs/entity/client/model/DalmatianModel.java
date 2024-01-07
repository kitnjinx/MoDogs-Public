package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.DalmatianRenderer;
import com.kitnjinx.modogs.entity.custom.DalmatianEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DalmatianModel extends GeoModel<DalmatianEntity> {
    @Override
    public ResourceLocation getModelResource(DalmatianEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/dalmatian.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DalmatianEntity object) {
        return DalmatianRenderer.WHITE_BASE_LOCATION;
    }

    @Override
    public ResourceLocation getAnimationResource(DalmatianEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/dalmatian.animation.json");
    }
}