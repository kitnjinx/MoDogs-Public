package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.GermanShepherdRenderer;
import com.kitnjinx.modogs.entity.custom.GermanShepherdEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GermanShepherdModel extends AnimatedGeoModel<GermanShepherdEntity> {
    @Override
    public ResourceLocation getModelLocation(GermanShepherdEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/german_shepherd.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GermanShepherdEntity object) {
        return GermanShepherdRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GermanShepherdEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/german_shepherd.animation.json");
    }
}
