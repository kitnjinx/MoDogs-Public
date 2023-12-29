package com.kitnjinx.modogs.entity.client.renderer;

import com.google.common.collect.Maps;
import com.kitnjinx.modogs.MoDogs;
import com.kitnjinx.modogs.entity.client.model.TreeWalkHoundModel;
import com.kitnjinx.modogs.entity.client.renderer.layer.TreeWalkHoundCollarLayer;
import com.kitnjinx.modogs.entity.custom.TreeWalkHoundEntity;
import com.kitnjinx.modogs.entity.variant.TreeWalkHoundVariant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class TreeWalkHoundRenderer extends GeoEntityRenderer<TreeWalkHoundEntity> {

    public static final Map<TreeWalkHoundVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(TreeWalkHoundVariant.class), (var) -> {
                var.put(TreeWalkHoundVariant.TRICOLOR,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/tree_walk_hound/tree_walk_hound_tricolor.png"));
                var.put(TreeWalkHoundVariant.BLACK,
                        new ResourceLocation(MoDogs.MOD_ID, "textures/entity/tree_walk_hound/tree_walk_hound_black.png"));
            });

    public TreeWalkHoundRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TreeWalkHoundModel());

        addLayer(new TreeWalkHoundCollarLayer(this));

        this.shadowRadius = 0.525f;
    }

    @Override
    public ResourceLocation getTextureLocation(TreeWalkHoundEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }

    public RenderType getRenderType(TreeWalkHoundEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        // Height ~24 inches
        if(animatable.isBaby()) {
            stack.scale(0.55f, 0.55f, 0.55f);
        } else {
            stack.scale(1.1f, 1.1f, 1.1f);
        }

        return  super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
