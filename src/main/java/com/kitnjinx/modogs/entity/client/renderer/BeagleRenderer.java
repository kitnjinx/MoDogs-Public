package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BeagleModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BeagleCollarLayer;
import com.kitnjinx.modogs.entity.custom.BeagleEntity;
import com.kitnjinx.modogs.entity.variant.BeagleVariant;
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

public class BeagleRenderer extends GeoEntityRenderer<BeagleEntity> {

    public static final Map<BeagleVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BeagleVariant.class), (var) -> {
                var.put(BeagleVariant.BLACK_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/beagle/beagle_black_tan.png"));
                var.put(BeagleVariant.TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/beagle/beagle_tan.png"));
                var.put(BeagleVariant.DARK_EARS,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/beagle/beagle_dark_ears.png"));
            });

    public BeagleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BeagleModel());

        addRenderLayer(new BeagleCollarLayer(this));

        this.shadowRadius = 0.35f;
    }

    @Override
    public ResourceLocation getTextureLocation(BeagleEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, BeagleEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~14 in
        if(animatable.isBaby()) {
            withScale(0.35f, 0.35f);
        } else {
            withScale(0.7f, 0.7f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(BeagleEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
