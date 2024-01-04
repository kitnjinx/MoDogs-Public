package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.CardiganCorgiRenderer;
import com.kitnjinx.modogs.entity.custom.CardiganCorgiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CardiganCorgiModel extends GeoModel<CardiganCorgiEntity> {
    @Override
    public ResourceLocation getModelResource(CardiganCorgiEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/cardigan_corgi.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CardiganCorgiEntity object) {
        return CardiganCorgiRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(CardiganCorgiEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/cardigan_corgi.animation.json");
    }
}
