package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.ToyPoodleModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.ToyPoodleCollarLayer;
import com.kitnjinx.modogs.entity.custom.ToyPoodleEntity;
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

public class ToyPoodleRenderer extends GeoEntityRenderer<ToyPoodleEntity> {

    public static final Map<PoodleVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(PoodleVariant.class), (var) -> {
                var.put(PoodleVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_black.png"));
                var.put(PoodleVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/toy_poodle_brown.png"));
                var.put(PoodleVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_white.png"));
                var.put(PoodleVariant.SILVER,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_silver.png"));
                var.put(PoodleVariant.CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/poodle/poodle_cream.png"));
            });

    public ToyPoodleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ToyPoodleModel());

        addRenderLayer(new ToyPoodleCollarLayer(this));

        this.shadowRadius = 0.25f;
    }

    @Override
    public ResourceLocation getTextureLocation(ToyPoodleEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, ToyPoodleEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~10 inches
        if(animatable.isBaby()) {
            withScale(0.25f, 0.25f);
        } else {
            withScale(0.5f, 0.5f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(ToyPoodleEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
