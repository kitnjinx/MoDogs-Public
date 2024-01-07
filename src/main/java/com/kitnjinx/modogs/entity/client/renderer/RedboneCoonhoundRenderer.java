package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.RedboneCoonhoundModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.RedboneCoonhoundCollarLayer;
import com.kitnjinx.modogs.entity.custom.RedboneCoonhoundEntity;
import com.kitnjinx.modogs.entity.variant.ShadeVariant;
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

public class RedboneCoonhoundRenderer extends GeoEntityRenderer<RedboneCoonhoundEntity> {

    public static final Map<ShadeVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ShadeVariant.class), (var) -> {
                var.put(ShadeVariant.LIGHT,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/redbone_coonhound/redbone_coonhound_brown.png"));
                var.put(ShadeVariant.MEDIUM,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/redbone_coonhound/redbone_coonhound_red.png"));
                var.put(ShadeVariant.DARK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/redbone_coonhound/redbone_coonhound_dark_red.png"));
            });

    public RedboneCoonhoundRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RedboneCoonhoundModel());

        addRenderLayer(new RedboneCoonhoundCollarLayer(this));

        this.shadowRadius = 0.525f;
    }

    @Override
    public ResourceLocation getTextureLocation(RedboneCoonhoundEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, RedboneCoonhoundEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~24 inches
        if(animatable.isBaby()) {
            withScale(0.55f, 0.55f);
        } else {
            withScale(1.1f, 1.1f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(RedboneCoonhoundEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
