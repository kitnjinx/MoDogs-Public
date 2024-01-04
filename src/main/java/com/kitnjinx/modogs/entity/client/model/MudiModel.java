package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.MudiRenderer;
import com.kitnjinx.modogs.entity.custom.MudiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MudiModel extends GeoModel<MudiEntity> {
    @Override
    public ResourceLocation getModelResource(MudiEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/mudi.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MudiEntity object) {
        return MudiRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(MudiEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/mudi.animation.json");
    }
}