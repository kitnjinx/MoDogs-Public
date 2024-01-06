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
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;
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

        addRenderLayer(new GermanSpitzCollarLayer(this));

        this.shadowRadius = 0.35f;
    }

    @Override
    public ResourceLocation getTextureLocation(GermanSpitzEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, GermanSpitzEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
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
    public RenderType getRenderType(GermanSpitzEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
