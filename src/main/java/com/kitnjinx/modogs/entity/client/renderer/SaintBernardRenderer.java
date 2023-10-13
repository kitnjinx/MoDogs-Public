package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.SaintBernardModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.SaintBernardBarrelLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.SaintBernardCollarLayer;
import com.kitnjinx.modogs.entity.custom.SaintBernardEntity;
import com.kitnjinx.modogs.entity.variant.SaintBernardVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class SaintBernardRenderer extends GeoEntityRenderer<SaintBernardEntity> {

    public static final Map<SaintBernardVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(SaintBernardVariant.class), (var) -> {
                var.put(SaintBernardVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/saint_bernard/saint_bernard_brown.png"));
                var.put(SaintBernardVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/saint_bernard/saint_bernard_red.png"));
                var.put(SaintBernardVariant.ORANGE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/saint_bernard/saint_bernard_orange.png"));
                var.put(SaintBernardVariant.YELLOW_BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/saint_bernard/saint_bernard_yellow_brown.png"));
                var.put(SaintBernardVariant.MAHOGANY,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/saint_bernard/saint_bernard_mahogany.png"));
            });

    public SaintBernardRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SaintBernardModel());

        addLayer(new SaintBernardCollarLayer(this));
        addLayer(new SaintBernardBarrelLayer(this));

        this.shadowRadius = 0.55f;
    }

    @Override
    public ResourceLocation getTextureLocation(SaintBernardEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(SaintBernardEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~28 in
        if(animatable.isBaby()) {
            stack.scale(0.65f, 0.65f, 0.65f);
        } else {
            stack.scale(1.3f, 1.3f, 1.3f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
