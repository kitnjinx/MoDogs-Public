package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.NorwegianElkhoundRenderer;
import com.kitnjinx.modogs.entity.custom.NorwegianElkhoundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class NorwegianElkhoundModel extends GeoModel<NorwegianElkhoundEntity> {
    @Override
    public ResourceLocation getModelResource(NorwegianElkhoundEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/norwegian_elkhound.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NorwegianElkhoundEntity object) {
        return NorwegianElkhoundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(NorwegianElkhoundEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/norwegian_elkhound.animation.json");
    }
}
