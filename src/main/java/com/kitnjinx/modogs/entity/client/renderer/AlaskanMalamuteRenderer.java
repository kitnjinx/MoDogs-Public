package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.AlaskanMalamuteModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.AlaskanMalamuteCollarLayer;
import com.kitnjinx.modogs.entity.custom.AlaskanMalamuteEntity;
import com.kitnjinx.modogs.entity.variant.AlaskanMalamuteVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class AlaskanMalamuteRenderer extends GeoEntityRenderer<AlaskanMalamuteEntity> {

    public static final Map<AlaskanMalamuteVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(AlaskanMalamuteVariant.class), (var) -> {
                var.put(AlaskanMalamuteVariant.GRAY,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/alaskan_malamute/alaskan_malamute_gray.png"));
                var.put(AlaskanMalamuteVariant.SABLE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/alaskan_malamute/alaskan_malamute_sable.png"));
                var.put(AlaskanMalamuteVariant.SEAL,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/alaskan_malamute/alaskan_malamute_seal.png"));
                var.put(AlaskanMalamuteVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/alaskan_malamute/alaskan_malamute_black.png"));
                var.put(AlaskanMalamuteVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/alaskan_malamute/alaskan_malamute_red.png"));
                var.put(AlaskanMalamuteVariant.SILVER,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/alaskan_malamute/alaskan_malamute_silver.png"));
            });

    public AlaskanMalamuteRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AlaskanMalamuteModel());

        addLayer(new AlaskanMalamuteCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(AlaskanMalamuteEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(AlaskanMalamuteEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~24 in
        if(animatable.isBaby()) {
            stack.scale(0.55f, 0.55f, 0.55f);
        } else {
            stack.scale(1.1f, 1.1f, 1.1f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
