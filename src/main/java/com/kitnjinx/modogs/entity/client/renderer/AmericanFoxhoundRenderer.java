package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.AmericanFoxhoundModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.AmericanFoxhoundCollarLayer;
import com.kitnjinx.modogs.entity.custom.AmericanFoxhoundEntity;
import com.kitnjinx.modogs.entity.variant.ShadeVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.util.Map;

public class AmericanFoxhoundRenderer extends GeoEntityRenderer<AmericanFoxhoundEntity> {

    public static final Map<ShadeVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ShadeVariant.class), (var) -> {
                var.put(ShadeVariant.LIGHT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/american_foxhound/american_foxhound_light.png"));
                var.put(ShadeVariant.MEDIUM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/american_foxhound/american_foxhound_medium.png"));
                var.put(ShadeVariant.DARK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/american_foxhound/american_foxhound_dark.png"));
            });

    public AmericanFoxhoundRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AmericanFoxhoundModel());

        addRenderLayer(new AmericanFoxhoundCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(AmericanFoxhoundEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, AmericanFoxhoundEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~23 inches
        if(animatable.isBaby()) {
            withScale(0.525f, 0.525f);
        } else {
            withScale(1.05f, 1.05f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(AmericanFoxhoundEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
