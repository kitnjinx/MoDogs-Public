package com.kitnjinx.modogs.entity.client.model;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.renderer.PembrokeCorgiRenderer;
import com.kitnjinx.modogs.entity.custom.PembrokeCorgiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PembrokeCorgiModel extends AnimatedGeoModel<PembrokeCorgiEntity> {
    @Override
    public ResourceLocation getModelResource(PembrokeCorgiEntity object) {
        return new ResourceLocation(MoDogs.MOD_ID, "geo/pembroke_corgi.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PembrokeCorgiEntity object) {
        return PembrokeCorgiRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public ResourceLocation getAnimationResource(PembrokeCorgiEntity animatable) {
        return new ResourceLocation(MoDogs.MOD_ID, "animations/pembroke_corgi.animation.json");
    }
}
