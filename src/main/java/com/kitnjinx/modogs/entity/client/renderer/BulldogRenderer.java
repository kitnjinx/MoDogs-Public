package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.BulldogModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.BulldogWhiteLayer;
import com.kitnjinx.modogs.entity.client.renderer.layer.collar.BulldogCollarLayer;
import com.kitnjinx.modogs.entity.custom.BulldogEntity;
import com.kitnjinx.modogs.entity.variant.BulldogVariant;
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

public class BulldogRenderer extends GeoEntityRenderer<BulldogEntity> {

    public static final Map<BulldogVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(BulldogVariant.class), (var) -> {
                var.put(BulldogVariant.RED,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bulldog/bulldog_red.png"));
                var.put(BulldogVariant.FAWN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/bulldog/bulldog_fawn.png"));
            });

    public BulldogRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BulldogModel());

        addRenderLayer(new BulldogWhiteLayer(this));
        addRenderLayer(new BulldogCollarLayer(this));

        this.shadowRadius = 0.375f;
    }

    @Override
    public ResourceLocation getTextureLocation(BulldogEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, BulldogEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~15 inches
        if(animatable.isBaby()) {
            withScale(0.375f, 0.375f);
        } else {
            withScale(0.75f, 0.75f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(BulldogEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
