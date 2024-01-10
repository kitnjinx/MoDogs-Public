package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.GreyhoundEntity;
import com.kitnjinx.modogs.entity.variant.GreyhoundVariant;
import com.kitnjinx.modogs.entity.variant.pattern_variation.GreyhoundWhiteVariant;
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

public class GreyhoundWhiteLayer extends GeoRenderLayer<GreyhoundEntity> {
    public static final Map<GreyhoundWhiteVariant, ResourceLocation> WHITE_PATTERNS =
            Util.make(Maps.newEnumMap(GreyhoundWhiteVariant.class), (pat) -> {
                pat.put(GreyhoundWhiteVariant.WHITE1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/white/greyhound_white1.png"));
                pat.put(GreyhoundWhiteVariant.WHITE2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/white/greyhound_white2.png"));
                pat.put(GreyhoundWhiteVariant.WHITE3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/white/greyhound_white3.png"));
            });

    public static final ResourceLocation NO_PATTERN = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collar/58x31/collar_none.png");

    public GreyhoundWhiteLayer(GeoRenderer<GreyhoundEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getPatternLocation(GreyhoundEntity instance) {
        if (instance.getVariant() == GreyhoundVariant.WHITE || !instance.hasWhite()) {
            return NO_PATTERN;
        } else {
            return WHITE_PATTERNS.get(instance.getWhiteVariant());
        }
    }

    public void render(PoseStack poseStack, GreyhoundEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getPatternLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
