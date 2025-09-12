package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.AlaskanMalamuteModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.AlaskanMalamuteCollarLayer;
import com.kitnjinx.modogs.entity.custom.AlaskanMalamuteEntity;
import com.kitnjinx.modogs.entity.variant.AlaskanMalamuteVariant;
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

public class AlaskanMalamuteRenderer extends GeoEntityRenderer<AlaskanMalamuteEntity> {

    public static final Map<AlaskanMalamuteVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(AlaskanMalamuteVariant.class), (var) -> {
                var.put(AlaskanMalamuteVariant.GRAY,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/alaskan_malamute/alaskan_malamute_gray.png"));
                var.put(AlaskanMalamuteVariant.SABLE,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/alaskan_malamute/alaskan_malamute_sable.png"));
                var.put(AlaskanMalamuteVariant.SEAL,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/alaskan_malamute/alaskan_malamute_seal.png"));
                var.put(AlaskanMalamuteVariant.BLACK,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/alaskan_malamute/alaskan_malamute_black.png"));
                var.put(AlaskanMalamuteVariant.RED,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/alaskan_malamute/alaskan_malamute_red.png"));
                var.put(AlaskanMalamuteVariant.SILVER,
                        ResourceLocation.fromNamespaceAndPath(MoDogs.MOD_ID, "textures/entity/alaskan_malamute/alaskan_malamute_silver.png"));
            });

    public AlaskanMalamuteRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AlaskanMalamuteModel());

        addRenderLayer(new AlaskanMalamuteCollarLayer(this));

        this.shadowRadius = 0.525f;
    }

    @Override
    public ResourceLocation getTextureLocation(AlaskanMalamuteEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack poseStack, AlaskanMalamuteEntity animatable, BakedGeoModel model, @org.jetbrains.annotations.Nullable MultiBufferSource bufferSource, @org.jetbrains.annotations.Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {

        // Height ~24 in
        if(animatable.isBaby()) {
            withScale(0.55f, 0.55f);
        } else {
            withScale(1.1f, 1.1f);
        }

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public RenderType getRenderType(AlaskanMalamuteEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
