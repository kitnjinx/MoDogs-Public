package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BloodhoundModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BloodhoundCollarLayer;
import com.kitnjinx.modogs.entity.custom.BloodhoundEntity;
import com.kitnjinx.modogs.entity.variant.BloodhoundVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

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

        addLayer(new BloodhoundCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(BloodhoundEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(BloodhoundEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~25 inches
        if(animatable.isBaby()) {
            stack.scale(0.575f, 0.575f, 0.575f);
        } else {
            stack.scale(1.15f, 1.15f, 1.15f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}