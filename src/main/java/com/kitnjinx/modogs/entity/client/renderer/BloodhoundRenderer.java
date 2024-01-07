package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BloodhoundModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.BloodhoundCollarLayer;
import com.kitnjinx.modogs.entity.custom.BloodhoundEntity;
import com.kitnjinx.modogs.entity.variant.BloodhoundVariant;
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

public class BloodhoundRenderer extends GeoEntityRenderer<BloodhoundEntity> {

    public static final Map<BloodhoundVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BloodhoundVariant.class), (var) -> {
                var.put(BloodhoundVariant.BLACK_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bloodhound/bloodhound_black_tan.png"));
                var.put(BloodhoundVariant.LIVER_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bloodhound/bloodhound_liver_tan.png"));
                var.put(BloodhoundVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bloodhound/bloodhound_red.png"));
            });

    public BloodhoundRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BloodhoundModel());

        addRenderLayer(new BloodhoundCollarLayer(this));

        this.shadowRadius = 0.55f;
    }

    @Override
    public ResourceLocation getTextureLocation(BloodhoundEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, BloodhoundEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~25 inches
        if(animatable.isBaby()) {
            withScale(0.575f, 0.575f);
        } else {
            withScale(1.15f, 1.15f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(BloodhoundEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}