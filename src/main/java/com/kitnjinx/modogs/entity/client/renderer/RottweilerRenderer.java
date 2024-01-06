package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.RottweilerModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.RottweilerCollarLayer;
import com.kitnjinx.modogs.entity.custom.RottweilerEntity;
import com.kitnjinx.modogs.entity.variant.RottweilerVariant;
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

public class RottweilerRenderer extends GeoEntityRenderer<RottweilerEntity> {

    public static final Map<RottweilerVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(RottweilerVariant.class), (var) -> {
                var.put(RottweilerVariant.TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/rottweiler/rottweiler_tan.png"));
                var.put(RottweilerVariant.RUST,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/rottweiler/rottweiler_rust.png"));
                var.put(RottweilerVariant.MAHOGANY,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/rottweiler/rottweiler_mahogany.png"));
            });

    public RottweilerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RottweilerModel());

        addRenderLayer(new RottweilerCollarLayer(this));

        this.shadowRadius = 0.525f;
    }

    @Override
    public ResourceLocation getTextureLocation(RottweilerEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, RottweilerEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~24 in
        if(animatable.isBaby()) {
            withScale(0.55f, 0.55f);
        } else {
            withScale(1.1f, 1.1f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(RottweilerEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
