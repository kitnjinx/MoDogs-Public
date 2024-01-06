package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.PugModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.PugCollarLayer;
import com.kitnjinx.modogs.entity.custom.PugEntity;
import com.kitnjinx.modogs.entity.variant.PugVariant;
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

public class PugRenderer extends GeoEntityRenderer<PugEntity> {

    public static final Map<PugVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(PugVariant.class), (var) -> {
                var.put(PugVariant.FAWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pug/pug_fawn.png"));
                var.put(PugVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/pug/pug_black.png"));
            });

    public PugRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PugModel());

        addRenderLayer(new PugCollarLayer(this));

        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(PugEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, PugEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~12 inches
        if(animatable.isBaby()) {
            withScale(0.3f, 0.3f);
        } else {
            withScale(0.6f, 0.6f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(PugEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
