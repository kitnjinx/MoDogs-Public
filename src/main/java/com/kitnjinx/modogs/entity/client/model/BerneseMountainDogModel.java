package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BerneseMountainDogRenderer;
import com.kitnjinx.modogs.entity.custom.BerneseMountainDogEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BerneseMountainDogModel extends AnimatedGeoModel<BerneseMountainDogEntity> {
    @Override
    public ResourceLocation getModelResource(BerneseMountainDogEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/bernese_mountain_dog.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BerneseMountainDogEntity object) {
        return BerneseMountainDogRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(BerneseMountainDogEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/bernese_mountain_dog.animation.json");
    }
}
