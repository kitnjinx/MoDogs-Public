package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.DobermanModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.DobermanCollarLayer;
import com.kitnjinx.modogs.entity.custom.DobermanEntity;
import com.kitnjinx.modogs.entity.variant.DobermanVariant;
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

public class DobermanRenderer extends GeoEntityRenderer<DobermanEntity> {

    public static final Map<DobermanVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(DobermanVariant.class), (var) -> {
                var.put(DobermanVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/doberman/doberman_black.png"));
                var.put(DobermanVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/doberman/doberman_red.png"));
                var.put(DobermanVariant.BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/doberman/doberman_blue.png"));
                var.put(DobermanVariant.FAWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/doberman/doberman_fawn.png"));
            });

    public DobermanRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DobermanModel());

        addRenderLayer(new DobermanCollarLayer(this));

        this.shadowRadius = 0.575f;
    }

    @Override
    public ResourceLocation getTextureLocation(DobermanEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, DobermanEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~26 in
        if(animatable.isBaby()) {
            withScale(0.6f, 0.6f);
        } else {
            withScale(1.2f, 1.2f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(DobermanEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
