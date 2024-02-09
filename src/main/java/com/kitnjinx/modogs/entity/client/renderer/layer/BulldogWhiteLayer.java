package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.BulldogEntity;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.kitnjinx.modogs.entity.variant.pattern_variation.TwoWhiteVariant;
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

public class BulldogWhiteLayer extends GeoRenderLayer<BulldogEntity> {
    public static final Map<TwoWhiteVariant, ResourceLocation> LOW_WHITE_PATTERNS =
            Util.make(Maps.newEnumMap(TwoWhiteVariant.class), (pat) -> {
                pat.put(TwoWhiteVariant.WHITE1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bulldog/white/bulldog_low_white1.png"));
                pat.put(TwoWhiteVariant.WHITE2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bulldog/white/bulldog_low_white2.png"));
            });

    public static final Map<TwoWhiteVariant, ResourceLocation> HIGH_WHITE_PATTERNS =
            Util.make(Maps.newEnumMap(TwoWhiteVariant.class), (pat) -> {
                pat.put(TwoWhiteVariant.WHITE1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bulldog/white/bulldog_high_white1.png"));
                pat.put(TwoWhiteVariant.WHITE2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bulldog/white/bulldog_high_white2.png"));
            });

    public static final ResourceLocation PURE_WHITE_LOCATION = new ResourceLocation(MoDogs.MOD_ID,
            "textures/entity/bulldog/white/bulldog_white.png");

    public BulldogWhiteLayer(GeoRenderer<BulldogEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getCollarLocation(BulldogEntity instance) {
        if (instance.isPureWhite()) {
            return PURE_WHITE_LOCATION;
        } else if (instance.hasWhite()) {
            return HIGH_WHITE_PATTERNS.get(instance.getWhitePattern());
        } else {
            return LOW_WHITE_PATTERNS.get(instance.getWhitePattern());
        }
    }

    public void render(PoseStack poseStack, BulldogEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getCollarLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
