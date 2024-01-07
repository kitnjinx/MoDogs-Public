package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BoxerModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.BoxerCollarLayer;
import com.kitnjinx.modogs.entity.custom.BoxerEntity;
import com.kitnjinx.modogs.entity.variant.BoxerVariant;
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

public class BoxerRenderer extends GeoEntityRenderer<BoxerEntity> {

    public static final Map<BoxerVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BoxerVariant.class), (var) -> {
                var.put(BoxerVariant.MEDIUM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/boxer/boxer_medium.png"));
                var.put(BoxerVariant.LIGHT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/boxer/boxer_light.png"));
                var.put(BoxerVariant.DARK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/boxer/boxer_dark.png"));
                var.put(BoxerVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/boxer/boxer_black.png"));
            });

    public BoxerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BoxerModel());

        addRenderLayer(new BoxerCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(BoxerEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, BoxerEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~23 inches
        if(animatable.isBaby()) {
            withScale(0.525f, 0.525f);
        } else {
            withScale(1.05f, 1.05f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(BoxerEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
