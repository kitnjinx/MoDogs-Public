package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.PitBullModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.PitBullCollarLayer;
import com.kitnjinx.modogs.entity.custom.PitBullEntity;
import com.kitnjinx.modogs.entity.variant.PitBullVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class PitBullRenderer extends GeoEntityRenderer<PitBullEntity> {

    public static final Map<PitBullVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(PitBullVariant.class), (var) -> {
                var.put(PitBullVariant.BROWN_WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pit_bull/pit_bull_brown_white.png"));
                var.put(PitBullVariant.BLACK_WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pit_bull/pit_bull_black_white.png"));
                var.put(PitBullVariant.BLUE_WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pit_bull/pit_bull_blue_white.png"));
                var.put(PitBullVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pit_bull/pit_bull_brown.png"));
                var.put(PitBullVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pit_bull/pit_bull_black.png"));
                var.put(PitBullVariant.BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pit_bull/pit_bull_blue.png"));
                var.put(PitBullVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pit_bull/pit_bull_white.png"));
            });

    public PitBullRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PitBullModel());

        addLayer(new PitBullCollarLayer(this));

        this.shadowRadius = 0.45f;
    }

    @Override
    public ResourceLocation getTextureLocation(PitBullEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(PitBullEntity animatable, float partialTicks, PoseStack stack,
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
