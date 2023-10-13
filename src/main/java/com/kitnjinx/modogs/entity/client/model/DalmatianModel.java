package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.DalmatianRenderer;
import com.kitnjinx.modogs.entity.custom.DalmatianEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DalmatianModel extends AnimatedGeoModel<DalmatianEntity> {
    @Override
    public ResourceLocation getModelLocation(DalmatianEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/dalmatian.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DalmatianEntity object) {
        return DalmatianRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DalmatianEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/dalmatian.animation.json");
    }
}