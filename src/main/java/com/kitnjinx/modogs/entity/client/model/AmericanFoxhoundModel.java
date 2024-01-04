package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.AmericanFoxhoundRenderer;
import com.kitnjinx.modogs.entity.custom.AmericanFoxhoundEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AmericanFoxhoundModel extends GeoModel<AmericanFoxhoundEntity> {
    @Override
    public ResourceLocation getModelResource(AmericanFoxhoundEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/american_foxhound.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AmericanFoxhoundEntity object) {
        return AmericanFoxhoundRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(AmericanFoxhoundEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/american_foxhound.animation.json");
    }
}
