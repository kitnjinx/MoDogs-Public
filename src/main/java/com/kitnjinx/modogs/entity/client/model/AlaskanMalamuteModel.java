package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.AlaskanMalamuteRenderer;
import com.kitnjinx.modogs.entity.custom.AlaskanMalamuteEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AlaskanMalamuteModel extends AnimatedGeoModel<AlaskanMalamuteEntity> {
    @Override
    public ResourceLocation getModelLocation(AlaskanMalamuteEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/alaskan_malamute.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(AlaskanMalamuteEntity object) {
        return AlaskanMalamuteRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(AlaskanMalamuteEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/alaskan_malamute.animation.json");
    }
}
