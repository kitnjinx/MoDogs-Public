package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BullTerrierModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BullTerrierCollarLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.BullTerrierTargetLayer;
import com.kitnjinx.modogs.entity.custom.BullTerrierEntity;
import com.kitnjinx.modogs.entity.variant.BullTerrierVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class BullTerrierRenderer extends GeoEntityRenderer<BullTerrierEntity> {

    public static final Map<BullTerrierVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BullTerrierVariant.class), (var) -> {
                var.put(BullTerrierVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/bull_terrier_white.png"));
                var.put(BullTerrierVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/bull_terrier_black.png"));
                var.put(BullTerrierVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/bull_terrier_red.png"));
                var.put(BullTerrierVariant.WHITE_BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/bull_terrier_white_black.png"));
                var.put(BullTerrierVariant.WHITE_RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/bull_terrier_white_red.png"));
            });

    public BullTerrierRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BullTerrierModel());

        addLayer(new BullTerrierCollarLayer(this));
        addLayer(new BullTerrierTargetLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(BullTerrierEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(BullTerrierEntity animatable, float partialTicks, PoseStack stack,
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