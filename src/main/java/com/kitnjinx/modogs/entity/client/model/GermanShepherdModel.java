package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.GermanShepherdRenderer;
import com.kitnjinx.modogs.entity.custom.GermanShepherdEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GermanShepherdModel extends GeoModel<GermanShepherdEntity> {
    @Override
    public ResourceLocation getModelResource(GermanShepherdEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/german_shepherd.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GermanShepherdEntity object) {
        return GermanShepherdRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(GermanShepherdEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/german_shepherd.animation.json");
    }
}
