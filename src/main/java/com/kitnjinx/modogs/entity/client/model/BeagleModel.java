package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.BeagleRenderer;
import com.kitnjinx.modogs.entity.custom.BeagleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BeagleModel extends AnimatedGeoModel<BeagleEntity> {
    @Override
    public ResourceLocation getModelLocation(BeagleEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/beagle.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BeagleEntity object) {
        return BeagleRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(BeagleEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/beagle.animation.json");
    }
}
