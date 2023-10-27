package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.ShibaInuModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.ShibaInuCollarLayer;
import com.kitnjinx.modogs.entity.custom.ShibaInuEntity;
import com.kitnjinx.modogs.entity.variant.ShibaInuVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class ShibaInuRenderer extends GeoEntityRenderer<ShibaInuEntity> {

    public static final Map<ShibaInuVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ShibaInuVariant.class), (var) -> {
                var.put(ShibaInuVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shiba_inu/shiba_inu_red.png"));
                var.put(ShibaInuVariant.BLACK_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shiba_inu/shiba_inu_black_tan.png"));
                var.put(ShibaInuVariant.CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shiba_inu/shiba_inu_cream.png"));
                var.put(ShibaInuVariant.DARK_CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shiba_inu/shiba_inu_dark_cream.png"));
            });

    public ShibaInuRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShibaInuModel());

        addLayer(new ShibaInuCollarLayer(this));

        this.shadowRadius = 0.4f;
    }

    @Override
    public ResourceLocation getTextureLocation(ShibaInuEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(ShibaInuEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~15 inches
        if(animatable.isBaby()) {
            stack.scale(0.475f, 0.475f, 0.475f);
        } else {
            stack.scale(0.95f, 0.95f, 0.95f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
