package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.GermanShepherdModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.GermanShepherdCollarLayer;
import com.kitnjinx.modogs.entity.custom.GermanShepherdEntity;
import com.kitnjinx.modogs.entity.variant.GermanShepherdVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class GermanShepherdRenderer extends GeoEntityRenderer<GermanShepherdEntity> {

    public static final Map<GermanShepherdVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(GermanShepherdVariant.class), (var) -> {
                var.put(GermanShepherdVariant.STANDARD,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/german_shepherd/german_shepherd_standard.png"));
                var.put(GermanShepherdVariant.BROWN_POINTS,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/german_shepherd/german_shepherd_brown_points.png"));
                var.put(GermanShepherdVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/german_shepherd/german_shepherd_black.png"));
                var.put(GermanShepherdVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/german_shepherd/german_shepherd_white.png"));
            });

    public GermanShepherdRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GermanShepherdModel());

        addLayer(new GermanShepherdCollarLayer(this));

        this.shadowRadius = 0.525f;
    }

    @Override
    public ResourceLocation getTextureLocation(GermanShepherdEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(GermanShepherdEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~24 inches
        if(animatable.isBaby()) {
            stack.scale(0.55f, 0.55f, 0.55f);
        } else {
            stack.scale(1.1f, 1.1f, 1.1f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
