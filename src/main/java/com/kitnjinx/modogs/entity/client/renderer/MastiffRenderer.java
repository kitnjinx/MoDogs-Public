package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.MastiffModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.MastiffCollarLayer;
import com.kitnjinx.modogs.entity.custom.MastiffEntity;
import com.kitnjinx.modogs.entity.variant.MastiffVariant;
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

public class MastiffRenderer extends GeoEntityRenderer<MastiffEntity> {

    public static final Map<MastiffVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(MastiffVariant.class), (var) -> {
                var.put(MastiffVariant.FAWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mastiff/mastiff_fawn.png"));
                var.put(MastiffVariant.APRICOT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/mastiff/mastiff_apricot.png"));
            });

    public MastiffRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MastiffModel());

        addRenderLayer(new MastiffCollarLayer(this));

        this.shadowRadius = 0.65f;
    }

    @Override
    public ResourceLocation getTextureLocation(MastiffEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, MastiffEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~30 in
        if(animatable.isBaby()) {
            withScale(0.7f, 0.7f);
        } else {
            withScale(1.4f, 1.4f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(MastiffEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
