package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.SchnauzerModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.SchnauzerCollarLayer;
import com.kitnjinx.modogs.entity.custom.SchnauzerEntity;
import com.kitnjinx.modogs.entity.variant.SchnauzerVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
import java.util.Map;

public class SchnauzerRenderer extends GeoEntityRenderer<SchnauzerEntity> {

    public static final Map<SchnauzerVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(SchnauzerVariant.class), (var) -> {
                var.put(SchnauzerVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/schnauzer/schnauzer_black.png"));
                var.put(SchnauzerVariant.PEPPER_SALT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/schnauzer/schnauzer_pepper_salt.png"));
            });

    public SchnauzerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SchnauzerModel());

        addRenderLayer(new SchnauzerCollarLayer(this));

        this.shadowRadius = 0.4f;
    }

    @Override
    public ResourceLocation getTextureLocation(SchnauzerEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, SchnauzerEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~18 in
        if(animatable.isBaby()) {
            withScale(0.4f, 0.4f);
        } else {
            withScale(0.8f, 0.8f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(SchnauzerEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
