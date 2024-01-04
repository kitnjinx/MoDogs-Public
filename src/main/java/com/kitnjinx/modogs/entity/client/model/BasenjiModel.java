package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BasenjiRenderer;
import com.kitnjinx.modogs.entity.custom.BasenjiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BasenjiModel extends GeoModel<BasenjiEntity> {
    @Override
    public ResourceLocation getModelResource(BasenjiEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/basenji.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BasenjiEntity object) {
        return BasenjiRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(BasenjiEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/basenji.animation.json");
    }
}