package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.MiniBullTerrierEntity;
import com.kitnjinx.modogs.entity.variant.pattern_variation.BullTerrierWhiteVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Map;

public class MiniBullTerrierWhiteLayer extends GeoRenderLayer<MiniBullTerrierEntity> {
    public static final Map<BullTerrierWhiteVariant, ResourceLocation> LOW_WHITE_PATTERNS =
            Util.make(Maps.newEnumMap(BullTerrierWhiteVariant.class), (pat) -> {
                pat.put(BullTerrierWhiteVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/white/bull_terrier_low_white1.png"));
                pat.put(BullTerrierWhiteVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/white/bull_terrier_low_white2.png"));
            });

    public static final Map<BullTerrierWhiteVariant, ResourceLocation> HIGH_WHITE_PATTERNS =
            Util.make(Maps.newEnumMap(BullTerrierWhiteVariant.class), (pat) -> {
                pat.put(BullTerrierWhiteVariant.PATTERN1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/white/bull_terrier_high_white1.png"));
                pat.put(BullTerrierWhiteVariant.PATTERN2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/white/bull_terrier_high_white2.png"));
            });

    public static final ResourceLocation PURE_WHITE_LOCATION = new ResourceLocation(MoDogs.MOD_ID, 
            "textures/entity/bull_terrier/white/bull_terrier_white.png");


    public MiniBullTerrierWhiteLayer(GeoRenderer<MiniBullTerrierEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getTargetLocation(MiniBullTerrierEntity instance) {
        if (instance.isPureWhite()) {
            return PURE_WHITE_LOCATION;
        } else if (instance.hasHighWhite()) {
            return HIGH_WHITE_PATTERNS.get(instance.getWhitePattern());
        } else {
            return LOW_WHITE_PATTERNS.get(instance.getWhitePattern());
        }
    }

    public void render(PoseStack poseStack, MiniBullTerrierEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getTargetLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
