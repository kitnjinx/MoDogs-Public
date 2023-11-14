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
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

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

        addLayer(new DobermanCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(DobermanEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(DobermanEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~26 in
        if(animatable.isBaby()) {
            stack.scale(0.6f, 0.6f, 0.6f);
        } else {
            stack.scale(1.2f, 1.2f, 1.2f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
