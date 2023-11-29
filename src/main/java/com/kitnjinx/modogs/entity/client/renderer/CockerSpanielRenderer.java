package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.CockerSpanielModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.CockerSpanielCollarLayer;
import com.kitnjinx.modogs.entity.custom.CockerSpanielEntity;
import com.kitnjinx.modogs.entity.variant.CockerSpanielVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class CockerSpanielRenderer extends GeoEntityRenderer<CockerSpanielEntity> {

    public static final Map<CockerSpanielVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(CockerSpanielVariant.class), (var) -> {
                var.put(CockerSpanielVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/cocker_spaniel/cocker_spaniel_red.png"));
                var.put(CockerSpanielVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/cocker_spaniel/cocker_spaniel_brown.png"));
                var.put(CockerSpanielVariant.BUFF,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/cocker_spaniel/cocker_spaniel_buff.png"));
                var.put(CockerSpanielVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/cocker_spaniel/cocker_spaniel_black.png"));
                var.put(CockerSpanielVariant.SILVER,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/cocker_spaniel/cocker_spaniel_silver.png"));
            });

    public CockerSpanielRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CockerSpanielModel());

        addLayer(new CockerSpanielCollarLayer(this));

        this.shadowRadius = 0.35f;
    }

    @Override
    public ResourceLocation getTextureLocation(CockerSpanielEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(CockerSpanielEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~14 in
        if(animatable.isBaby()) {
            stack.scale(0.35f, 0.35f, 0.35f);
        } else {
            stack.scale(0.7f, 0.7f, 0.7f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
