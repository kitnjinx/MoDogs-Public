package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.CardiganCorgiRenderer;
import com.kitnjinx.modogs.entity.custom.CardiganCorgiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CardiganCorgiModel extends AnimatedGeoModel<CardiganCorgiEntity> {
    @Override
    public ResourceLocation getModelLocation(CardiganCorgiEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/cardigan_corgi.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CardiganCorgiEntity object) {
        return CardiganCorgiRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CardiganCorgiEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/cardigan_corgi.animation.json");
    }
}
