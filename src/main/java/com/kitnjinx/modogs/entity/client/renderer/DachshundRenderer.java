package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.DachshundModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.DachshundCollarLayer;
import com.kitnjinx.modogs.entity.custom.DachshundEntity;
import com.kitnjinx.modogs.entity.variant.DachshundVariant;
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

public class DachshundRenderer extends GeoEntityRenderer<DachshundEntity> {

    public static final Map<DachshundVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(DachshundVariant.class), (var) -> {
                var.put(DachshundVariant.BLACK_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dachshund/dachshund_black_tan.png"));
                var.put(DachshundVariant.CHOCOLATE_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dachshund/dachshund_chocolate_tan.png"));
                var.put(DachshundVariant.FAWN_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dachshund/dachshund_fawn_tan.png"));
                var.put(DachshundVariant.BLACK_CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dachshund/dachshund_black_cream.png"));
                var.put(DachshundVariant.CHOCOLATE_CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dachshund/dachshund_chocolate_cream.png"));
                var.put(DachshundVariant.FAWN_CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/dachshund/dachshund_fawn_cream.png"));
            });

    public DachshundRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DachshundModel());

        addRenderLayer(new DachshundCollarLayer(this));

        this.shadowRadius = 0.4f;
    }

    @Override
    public ResourceLocation getTextureLocation(DachshundEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, DachshundEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~10 inches
        if(animatable.isBaby()) {
            withScale(0.35f, 0.35f);
        } else {
            withScale(0.7f, 0.7f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(DachshundEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
