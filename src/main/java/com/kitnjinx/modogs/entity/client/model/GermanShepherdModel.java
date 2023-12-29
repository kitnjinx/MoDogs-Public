package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.GermanShepherdRenderer;
import com.kitnjinx.modogs.entity.custom.GermanShepherdEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GermanShepherdModel extends AnimatedGeoModel<GermanShepherdEntity> {
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
