package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.CKCharlesSpanielModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.CKCharlesSpanielCollarLayer;
import com.kitnjinx.modogs.entity.custom.CKCharlesSpanielEntity;
import com.kitnjinx.modogs.entity.custom.CardiganCorgiEntity;
import com.kitnjinx.modogs.entity.variant.CKCharlesSpanielVariant;
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

public class CKCharlesSpanielRenderer extends GeoEntityRenderer<CKCharlesSpanielEntity> {

    public static final Map<CKCharlesSpanielVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(CKCharlesSpanielVariant.class), (var) -> {
                var.put(CKCharlesSpanielVariant.BLENHEIM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/ck_charles_spaniel/ck_charles_spaniel_blenheim.png"));
                var.put(CKCharlesSpanielVariant.TRICOLOR,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/ck_charles_spaniel/ck_charles_spaniel_tricolor.png"));
                var.put(CKCharlesSpanielVariant.RUBY,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/ck_charles_spaniel/ck_charles_spaniel_ruby.png"));
                var.put(CKCharlesSpanielVariant.BLACK_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/ck_charles_spaniel/ck_charles_spaniel_black_tan.png"));
            });

    public CKCharlesSpanielRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CKCharlesSpanielModel());

        addRenderLayer(new CKCharlesSpanielCollarLayer(this));

        this.shadowRadius = 0.375f;
    }

    @Override
    public ResourceLocation getTextureLocation(CKCharlesSpanielEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, CKCharlesSpanielEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~12 inches
        if(animatable.isBaby()) {
            stack.scale(0.4f, 0.4f, 0.4f);
        } else {
            stack.scale(0.8f, 0.8f, 0.8f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(CKCharlesSpanielEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
