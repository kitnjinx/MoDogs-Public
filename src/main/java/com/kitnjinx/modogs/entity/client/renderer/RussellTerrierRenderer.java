package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.RussellTerrierModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.RussellTerrierCollarLayer;
import com.kitnjinx.modogs.entity.custom.RussellTerrierEntity;
import com.kitnjinx.modogs.entity.variant.RussellTerrierVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class RussellTerrierRenderer extends GeoEntityRenderer<RussellTerrierEntity> {

    public static final Map<RussellTerrierVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(RussellTerrierVariant.class), (var) -> {
                var.put(RussellTerrierVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/russell_terrier/russell_terrier_brown.png"));
                var.put(RussellTerrierVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/russell_terrier/russell_terrier_black.png"));
                var.put(RussellTerrierVariant.TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/russell_terrier/russell_terrier_tan.png"));
                var.put(RussellTerrierVariant.CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/russell_terrier/russell_terrier_cream.png"));
                var.put(RussellTerrierVariant.TRI_BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/russell_terrier/russell_terrier_tri_brown.png"));
                var.put(RussellTerrierVariant.TRI_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/russell_terrier/russell_terrier_tri_tan.png"));
            });

    public RussellTerrierRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RussellTerrierModel());

        addLayer(new RussellTerrierCollarLayer(this));

        this.shadowRadius = 0.4f;
    }

    @Override
    public ResourceLocation getTextureLocation(RussellTerrierEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(RussellTerrierEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~12 inches
        if(animatable.isBaby()) {
            stack.scale(0.3f, 0.3f, 0.3f);
        } else {
            stack.scale(0.6f, 0.6f, 0.6f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
