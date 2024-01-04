package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.AiredaleTerrierEntity;
import com.kitnjinx.modogs.entity.custom.SaintBernardEntity;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.Map;

public class SaintBernardBarrelLayer extends GeoRenderLayer {
    public static final ResourceLocation NO_BARREL = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collar/53x36/collar_none.png");
    public static final ResourceLocation BARREL = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/saint_bernard/barrel.png");

    private static final ResourceLocation MODEL = new ResourceLocation(MoDogs.MOD_ID, "geo/saint_bernard.geo.json");

    public SaintBernardBarrelLayer(GeoRenderer entityRendererIn) {
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
