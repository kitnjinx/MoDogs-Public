package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.ScottishTerrierModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.ScottishTerrierCollarLayer;
import com.kitnjinx.modogs.entity.custom.SchnauzerEntity;
import com.kitnjinx.modogs.entity.custom.ScottishTerrierEntity;
import com.kitnjinx.modogs.entity.variant.ScottishTerrierVariant;
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

public class ScottishTerrierRenderer extends GeoEntityRenderer<ScottishTerrierEntity> {

    public static final Map<ScottishTerrierVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(ScottishTerrierVariant.class), (var) -> {
                var.put(ScottishTerrierVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/scottish_terrier/scottish_terrier_black.png"));
                var.put(ScottishTerrierVariant.WHEATEN,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/scottish_terrier/scottish_terrier_wheaten.png"));
            });

    public ScottishTerrierRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ScottishTerrierModel());

        addRenderLayer(new ScottishTerrierCollarLayer(this));

        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(ScottishTerrierEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    @Override
    public void preRender(PoseStack stack, ScottishTerrierEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                          float alpha) {
        // Height ~10 inches
        if(animatable.isBaby()) {
            stack.scale(0.3f, 0.3f, 0.3f);
        } else {
            stack.scale(0.6f, 0.6f, 0.6f);
        }

        super.preRender(stack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(ScottishTerrierEntity animatable, ResourceLocation texture,
                                    @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
