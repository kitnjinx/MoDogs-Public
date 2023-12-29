package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.HuskyModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.HuskyCollarLayer;
import com.kitnjinx.modogs.entity.custom.HuskyEntity;
import com.kitnjinx.modogs.entity.variant.HuskyVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class HuskyRenderer extends GeoEntityRenderer<HuskyEntity> {

    public static final Map<HuskyVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(HuskyVariant.class), (var) -> {
                var.put(HuskyVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/husky_black.png"));
                var.put(HuskyVariant.GRAY,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/husky_gray.png"));
                var.put(HuskyVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/husky_red.png"));
                var.put(HuskyVariant.SABLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/husky_sable.png"));
                var.put(HuskyVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/husky/husky_white.png"));
            });

    public HuskyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HuskyModel());

        addLayer(new HuskyCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(HuskyEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(HuskyEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~22 inches
        if(animatable.isBaby()) {
            stack.scale(0.5f, 0.5f, 0.5f);
        } else {
            stack.scale(1f, 1f, 1f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}