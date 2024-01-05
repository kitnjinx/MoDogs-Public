package com.kitnjinx.modogs.entity.client.renderer.layer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.custom.BorderCollieEntity;
import com.kitnjinx.modogs.entity.variant.pattern_variation.BorderCollieStripeVariant;
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

public class BorderCollieStripeLayer extends GeoRenderLayer<BorderCollieEntity> {
    public static final Map<BorderCollieStripeVariant, ResourceLocation> LOCATION_BY_STRIPE =
            Util.make(Maps.newEnumMap(BorderCollieStripeVariant.class), (pat) -> {
                pat.put(BorderCollieStripeVariant.STRIPE1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/stripe/border_collie_stripe1.png"));
                pat.put(BorderCollieStripeVariant.STRIPE2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/stripe/border_collie_stripe2.png"));
                pat.put(BorderCollieStripeVariant.STRIPE3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/stripe/border_collie_stripe3.png"));
                pat.put(BorderCollieStripeVariant.STRIPE4,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/border_collie/stripe/border_collie_stripe4.png"));
            });

    public BorderCollieStripeLayer(GeoRenderer<BorderCollieEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public ResourceLocation getPatternLocation(BorderCollieEntity instance) {
        return LOCATION_BY_STRIPE.get(instance.getStripeVariant());
    }

    public void render(PoseStack poseStack, BorderCollieEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getPatternLocation(animatable));

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
