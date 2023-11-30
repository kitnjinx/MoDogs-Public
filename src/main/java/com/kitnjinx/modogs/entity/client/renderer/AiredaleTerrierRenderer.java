package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.AiredaleTerrierModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.AiredaleTerrierCollarLayer;
import com.kitnjinx.modogs.entity.custom.AiredaleTerrierEntity;
import com.kitnjinx.modogs.entity.variant.ShadeVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class AiredaleTerrierRenderer extends GeoEntityRenderer<AiredaleTerrierEntity> {

    public static final Map<ShadeVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ShadeVariant.class), (var) -> {
                var.put(ShadeVariant.LIGHT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/airedale_terrier/airedale_terrier_light.png"));
                var.put(ShadeVariant.MEDIUM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/airedale_terrier/airedale_terrier_medium.png"));
                var.put(ShadeVariant.DARK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/airedale_terrier/airedale_terrier_dark.png"));
            });

    public AiredaleTerrierRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AiredaleTerrierModel());

        addLayer(new AiredaleTerrierCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(AiredaleTerrierEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(AiredaleTerrierEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~23 inches
        if(animatable.isBaby()) {
            stack.scale(0.525f, 0.525f, 0.525f);
        } else {
            stack.scale(1.05f, 1.05f, 1.05f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
