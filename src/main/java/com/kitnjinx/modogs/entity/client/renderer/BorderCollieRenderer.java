package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BorderCollieModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BorderCollieCollarLayer;
import com.kitnjinx.modogs.entity.custom.BorderCollieEntity;
import com.kitnjinx.modogs.entity.variant.BorderCollieVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class BorderCollieRenderer extends GeoEntityRenderer<BorderCollieEntity> {

    public static final Map<BorderCollieVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BorderCollieVariant.class), (var) -> {
                var.put(BorderCollieVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/border_collie_black.png"));
                var.put(BorderCollieVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/border_collie_red.png"));
                var.put(BorderCollieVariant.LILAC,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/border_collie_lilac.png"));
                var.put(BorderCollieVariant.BLACK_MERLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/border_collie_black_merle.png"));
                var.put(BorderCollieVariant.RED_MERLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/border_collie_red_merle.png"));
                var.put(BorderCollieVariant.LILAC_MERLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/border_collie_lilac_merle.png"));
            });

    public BorderCollieRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BorderCollieModel());

        addLayer(new BorderCollieCollarLayer(this));

        this.shadowRadius = 0.45f;
    }

    @Override
    public ResourceLocation getTextureLocation(BorderCollieEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(BorderCollieEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~20 in
        if(animatable.isBaby()) {
            stack.scale(0.4f, 0.4f, 0.4f);
        } else {
            stack.scale(0.9f, 0.9f, 0.9f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
