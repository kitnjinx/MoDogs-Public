package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.GreyhoundModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.GreyhoundWhiteLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.GreyhoundCollarLayer;
import com.kitnjinx.modogs.entity.custom.GreyhoundEntity;
import com.kitnjinx.modogs.entity.variant.GreyhoundVariant;
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

public class GreyhoundRenderer extends GeoEntityRenderer<GreyhoundEntity> {

    public static final Map<GreyhoundVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(GreyhoundVariant.class), (var) -> {
                var.put(GreyhoundVariant.WHITE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/greyhound_white.png"));
                var.put(GreyhoundVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/greyhound_black.png"));
                var.put(GreyhoundVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/greyhound_red.png"));
                var.put(GreyhoundVariant.BLUE,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/greyhound/greyhound_blue.png"));

            });

    public GreyhoundRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GreyhoundModel());

        addRenderLayer(new GreyhoundWhiteLayer(this));
        addRenderLayer(new GreyhoundCollarLayer(this));

        this.shadowRadius = 0.6f;
    }

    @Override
    public ResourceLocation getTextureLocation(GreyhoundEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, GreyhoundEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~28 in
        if(animatable.isBaby()) {
            withScale(0.65f, 0.65f);
        } else {
            withScale(1.3f, 1.3f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(GreyhoundEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
