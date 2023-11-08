package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BasenjiRenderer;
import com.kitnjinx.modogs.entity.custom.BasenjiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BasenjiModel extends AnimatedGeoModel<BasenjiEntity> {
    @Override
    public ResourceLocation getModelLocation(BasenjiEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/basenji.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BasenjiEntity object) {
        return BasenjiRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BasenjiEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/basenji.animation.json");
    }
}