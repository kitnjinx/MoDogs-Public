package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.PoodleModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.PoodleCollarLayer;
import com.kitnjinx.modogs.entity.custom.PoodleEntity;
import com.kitnjinx.modogs.entity.variant.PoodleVariant;
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

public class PoodleRenderer extends GeoEntityRenderer<PoodleEntity> {

    public static final Map<PoodleVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(PoodleVariant.class), (var) -> {
                var.put(PoodleVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_black.png"));
                var.put(PoodleVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_brown.png"));
                var.put(PoodleVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_white.png"));
                var.put(PoodleVariant.SILVER,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_silver.png"));
                var.put(PoodleVariant.CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_cream.png"));
            });

    public PoodleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PoodleModel());

        addRenderLayer(new PoodleCollarLayer(this));

        this.shadowRadius = 0.4f;
    }

    @Override
    public ResourceLocation getTextureLocation(PoodleEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, PoodleEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
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
    public RenderType getRenderType(PoodleEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
