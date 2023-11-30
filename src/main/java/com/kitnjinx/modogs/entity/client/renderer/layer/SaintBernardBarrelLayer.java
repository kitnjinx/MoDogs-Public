package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.SaintBernardEntity;
import com.kitnjinx.modogs.entity.variant.CollarVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.Map;

public class SaintBernardBarrelLayer extends GeoLayerRenderer {
    public static final ResourceLocation NO_BARREL = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collar/53x36/collar_none.png");
    public static final ResourceLocation BARREL = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/saint_bernard/barrel.png");

    private static final ResourceLocation MODEL = new ResourceLocation(MoDogs.MOD_ID, "geo/saint_bernard.geo.json");

    public SaintBernardBarrelLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getBarrelLocation(SaintBernardEntity instance) {
        if (instance.getBarrel()) {
            return BARREL;
        } else {
            return NO_BARREL;
        }
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType cameo =  RenderType.armorCutoutNoCull(getBarrelLocation((SaintBernardEntity) entityLivingBaseIn));
        matrixStackIn.pushPose();

        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, cameo, matrixStackIn, bufferIn,
                bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        matrixStackIn.popPose();
    }
}
