package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.RussellTerrierModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.RussellTerrierWhiteLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.RussellTerrierCollarLayer;
import com.kitnjinx.modogs.entity.custom.RussellTerrierEntity;
import com.kitnjinx.modogs.entity.variant.RussellTerrierVariant;
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

public class RussellTerrierRenderer extends GeoEntityRenderer<RussellTerrierEntity> {

    public static final Map<RussellTerrierVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(RussellTerrierVariant.class), (var) -> {
                var.put(RussellTerrierVariant.BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/russell_terrier/russell_terrier_brown.png"));
                var.put(RussellTerrierVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/russell_terrier/russell_terrier_black.png"));
                var.put(RussellTerrierVariant.TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/russell_terrier/russell_terrier_tan.png"));
                var.put(RussellTerrierVariant.CREAM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/russell_terrier/russell_terrier_cream.png"));
                var.put(RussellTerrierVariant.TRI_BROWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/russell_terrier/russell_terrier_tri_brown.png"));
                var.put(RussellTerrierVariant.TRI_TAN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/russell_terrier/russell_terrier_tri_tan.png"));
            });

    public RussellTerrierRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RussellTerrierModel());

        addRenderLayer(new RussellTerrierWhiteLayer(this));
        addRenderLayer(new RussellTerrierCollarLayer(this));

        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(RussellTerrierEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, RussellTerrierEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
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
    public RenderType getRenderType(RussellTerrierEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
