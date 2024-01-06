package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.DalmatianModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.DalmatianCollarLayer;
import com.kitnjinx.modogs.entity.custom.DalmatianEntity;
import com.kitnjinx.modogs.entity.variant.DalmatianVariant;
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

public class DalmatianRenderer extends GeoEntityRenderer<DalmatianEntity> {

    public static final Map<DalmatianVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(DalmatianVariant.class), (var) -> {
                var.put(DalmatianVariant.BLACK1,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_black1.png"));
                var.put(DalmatianVariant.BLACK2,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_black2.png"));
                var.put(DalmatianVariant.BLACK3,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_black3.png"));
                var.put(DalmatianVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dalmatian/dalmatian_brown.png"));
            });

    public DalmatianRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DalmatianModel());

        addRenderLayer(new DalmatianCollarLayer(this));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(DalmatianEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, DalmatianEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~22 inches
        if(animatable.isBaby()) {
            withScale(0.5f, 0.5f);
        } else {
            withScale(1f, 1f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(DalmatianEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}