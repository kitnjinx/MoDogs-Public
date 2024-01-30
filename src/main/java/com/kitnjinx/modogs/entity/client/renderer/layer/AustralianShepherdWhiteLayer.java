package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.AustralianShepherdEntity;
import com.kitnjinx.modogs.entity.variant.pattern_variation.AustralianShepherdWhiteVariant;
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

public class AustralianShepherdWhiteLayer extends GeoRenderLayer<AustralianShepherdEntity> {
    public static final Map<AustralianShepherdWhiteVariant, ResourceLocation> LOCATION_BY_WHITE =
            Util.make(Maps.newEnumMap(AustralianShepherdWhiteVariant.class), (whi) -> {
                whi.put(AustralianShepherdWhiteVariant.WHITE1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/australian_shepherd/white/australian_shepherd_white1.png"));
                whi.put(AustralianShepherdWhiteVariant.WHITE2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/australian_shepherd/white/australian_shepherd_white2.png"));
                whi.put(AustralianShepherdWhiteVariant.WHITE3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/australian_shepherd/white/australian_shepherd_white3.png"));
                whi.put(AustralianShepherdWhiteVariant.WHITE4,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/australian_shepherd/white/australian_shepherd_white4.png"));
            });

    public AustralianShepherdWhiteLayer(GeoRenderer<AustralianShepherdEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getWhiteLocation(AustralianShepherdEntity instance) {
        return LOCATION_BY_WHITE.get(instance.getWhiteVariant());
    }

    public void render(PoseStack poseStack, AustralianShepherdEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getWhiteLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
