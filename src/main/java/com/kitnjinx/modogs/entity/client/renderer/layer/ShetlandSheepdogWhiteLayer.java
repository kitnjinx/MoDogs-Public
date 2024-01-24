package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.ShetlandSheepdogEntity;
import com.kitnjinx.modogs.entity.variant.pattern_variation.SixWhiteVariant;
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

public class ShetlandSheepdogWhiteLayer extends GeoRenderLayer<ShetlandSheepdogEntity> {
    public static final Map<SixWhiteVariant, ResourceLocation> LOCATION_BY_WHITE =
            Util.make(Maps.newEnumMap(SixWhiteVariant.class), (whi) -> {
                whi.put(SixWhiteVariant.WHITE1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/white/shetland_sheepdog_white1.png"));
                whi.put(SixWhiteVariant.WHITE2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/white/shetland_sheepdog_white2.png"));
                whi.put(SixWhiteVariant.WHITE3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/white/shetland_sheepdog_white3.png"));
                whi.put(SixWhiteVariant.WHITE4,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/white/shetland_sheepdog_white4.png"));
                whi.put(SixWhiteVariant.WHITE5,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/white/shetland_sheepdog_white5.png"));
                whi.put(SixWhiteVariant.WHITE6,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/shetland_sheepdog/white/shetland_sheepdog_white6.png"));
            });

    public ShetlandSheepdogWhiteLayer(GeoRenderer<ShetlandSheepdogEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getWhiteLocation(ShetlandSheepdogEntity instance) {
        return LOCATION_BY_WHITE.get(instance.getWhiteVariant());
    }

    public void render(PoseStack poseStack, ShetlandSheepdogEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getWhiteLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
