package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.MudiModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.MudiCollarLayer;
import com.kitnjinx.modogs.entity.custom.MudiEntity;
import com.kitnjinx.modogs.entity.custom.NorwegianElkhoundEntity;
import com.kitnjinx.modogs.entity.variant.MudiVariant;
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

public class MudiRenderer extends GeoEntityRenderer<MudiEntity> {

    public static final Map<MudiVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(MudiVariant.class), (var) -> {
                var.put(MudiVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mudi/mudi_black.png"));
                var.put(MudiVariant.BLACK_MERLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mudi/mudi_black_merle.png"));
                var.put(MudiVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mudi/mudi_brown.png"));
                var.put(MudiVariant.BROWN_MERLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mudi/mudi_brown_merle.png"));
                var.put(MudiVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mudi/mudi_white.png"));
            });

    public MudiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MudiModel());

        addRenderLayer(new MudiCollarLayer(this));

        this.shadowRadius = 0.4f;
    }

    @Override
    public ResourceLocation getTextureLocation(MudiEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, MudiEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~18 in
        if(animatable.isBaby()) {
            stack.scale(0.4f, 0.4f, 0.4f);
        } else {
            stack.scale(0.8f, 0.8f, 0.8f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(MudiEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
