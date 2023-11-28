package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.GermanSpitzModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.GermanSpitzCollarLayer;
import com.kitnjinx.modogs.entity.custom.GermanSpitzEntity;
import com.kitnjinx.modogs.entity.variant.GermanSpitzVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class GermanSpitzRenderer extends GeoEntityRenderer<GermanSpitzEntity> {

    public static final Map<GermanSpitzVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(GermanSpitzVariant.class), (var) -> {
                var.put(GermanSpitzVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/german_spitz/german_spitz_white.png"));
                var.put(GermanSpitzVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/german_spitz/german_spitz_red.png"));
                var.put(GermanSpitzVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/german_spitz/german_spitz_black.png"));
                var.put(GermanSpitzVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/german_spitz/german_spitz_brown.png"));
            });

    public GermanSpitzRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GermanSpitzModel());

        addLayer(new GermanSpitzCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(GermanSpitzEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(GermanSpitzEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~14 in
        if(animatable.isBaby()) {
            stack.scale(0.35f, 0.35f, 0.35f);
        } else {
            stack.scale(0.7f, 0.7f, 0.7f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
