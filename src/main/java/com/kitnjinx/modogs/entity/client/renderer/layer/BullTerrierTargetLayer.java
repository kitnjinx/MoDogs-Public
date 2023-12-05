package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.BullTerrierEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class BullTerrierTargetLayer extends GeoLayerRenderer {
    public static final ResourceLocation NO_TARGET = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/collar/53x36/collar_none.png");
    public static final ResourceLocation TARGET = new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bull_terrier/target.png");

    private static final ResourceLocation MODEL = new ResourceLocation(MoDogs.MOD_ID, "geo/bull_terrier.geo.json");

    public BullTerrierTargetLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getBarrelLocation(BullTerrierEntity instance) {
        if (instance.showTarget()) {
            return TARGET;
        } else {
            return NO_TARGET;
        }
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType cameo =  RenderType.armorCutoutNoCull(getBarrelLocation((BullTerrierEntity) entityLivingBaseIn));
        matrixStackIn.pushPose();

        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, cameo, matrixStackIn, bufferIn,
                bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        matrixStackIn.popPose();
    }
}
