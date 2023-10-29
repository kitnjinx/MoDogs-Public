package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BostonTerrierModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BostonTerrierCollarLayer;
import com.kitnjinx.modogs.entity.custom.BostonTerrierEntity;
import com.kitnjinx.modogs.entity.variant.BostonTerrierVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class BostonTerrierRenderer extends GeoEntityRenderer<BostonTerrierEntity> {

    public static final Map<BostonTerrierVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BostonTerrierVariant.class), (var) -> {
                var.put(BostonTerrierVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/boston_terrier/boston_terrier_black.png"));
                var.put(BostonTerrierVariant.SEAL,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/boston_terrier/boston_terrier_seal.png"));
            });

    public BostonTerrierRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BostonTerrierModel());

        addLayer(new BostonTerrierCollarLayer(this));

        this.shadowRadius = 0.4f;
    }

    @Override
    public ResourceLocation getTextureLocation(BostonTerrierEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(BostonTerrierEntity animatable, float partialTicks, PoseStack stack,
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
