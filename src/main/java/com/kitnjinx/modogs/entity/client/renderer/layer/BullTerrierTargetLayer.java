package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.AiredaleTerrierEntity;
import com.kitnjinx.modogs.entity.custom.BullTerrierEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.GeoRenderer;

public class BullTerrierTargetLayer extends GeoRenderLayer {
    public static final ResourceLocation NO_TARGET = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collar/53x36/collar_none.png");
    public static final ResourceLocation TARGET = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/target.png");

    private static final ResourceLocation MODEL = new ResourceLocation(MoDogs.MOD_ID, "geo/bull_terrier.geo.json");

    public BullTerrierTargetLayer(GeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getTargetLocation(BullTerrierEntity instance) {
        if (instance.showTarget()) {
            return TARGET;
        } else {
            return NO_TARGET;
        }
    }

    public void render(PoseStack poseStack, BullTerrierEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getTargetLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
