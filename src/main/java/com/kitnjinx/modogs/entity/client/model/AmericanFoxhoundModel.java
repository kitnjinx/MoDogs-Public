package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.AmericanFoxhoundRenderer;
import com.kitnjinx.modogs.entity.custom.AmericanFoxhoundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AmericanFoxhoundModel extends AnimatedGeoModel<AmericanFoxhoundEntity> {
    @Override
    public ResourceLocation getModelLocation(AmericanFoxhoundEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/american_foxhound.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(AmericanFoxhoundEntity object) {
        return AmericanFoxhoundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(AmericanFoxhoundEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/american_foxhound.animation.json");
    }
}
