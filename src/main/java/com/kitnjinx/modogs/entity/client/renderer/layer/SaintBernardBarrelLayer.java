package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.SaintBernardEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class SaintBernardBarrelLayer extends GeoRenderLayer<SaintBernardEntity> {
    public static final ResourceLocation NO_BARREL = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collar/53x36/collar_none.png");
    public static final ResourceLocation BARREL = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/saint_bernard/barrel.png");

    public SaintBernardBarrelLayer(GeoRenderer<SaintBernardEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getBarrelLocation(SaintBernardEntity instance) {
        if (instance.getBarrel()) {
            return BARREL;
        } else {
            return NO_BARREL;
        }
    }

    public void render(PoseStack poseStack, SaintBernardEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getBarrelLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
