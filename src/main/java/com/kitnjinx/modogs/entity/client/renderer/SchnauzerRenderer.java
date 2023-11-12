package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.SchnauzerModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.SchnauzerCollarLayer;
import com.kitnjinx.modogs.entity.custom.SchnauzerEntity;
import com.kitnjinx.modogs.entity.variant.SchnauzerVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class SchnauzerRenderer extends GeoEntityRenderer<SchnauzerEntity> {

    public static final Map<SchnauzerVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(SchnauzerVariant.class), (var) -> {
                var.put(SchnauzerVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/schnauzer/schnauzer_black.png"));
                var.put(SchnauzerVariant.PEPPER_SALT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/schnauzer/schnauzer_pepper_salt.png"));
            });

    public SchnauzerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SchnauzerModel());

        addLayer(new SchnauzerCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(SchnauzerEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(SchnauzerEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~18 in
        if(animatable.isBaby()) {
            stack.scale(0.4f, 0.4f, 0.4f);
        } else {
            stack.scale(0.8f, 0.8f, 0.8f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
