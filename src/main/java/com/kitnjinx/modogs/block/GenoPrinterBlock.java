package com.kitnjinx.modogs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class GenoPrinterBlock extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public GenoPrinterBlock(Properties pProperties) {
        super(pProperties);
    }

    private static final VoxelShape BASE = Block.box(0, 0, 0, 16, 15, 16);
    private static final VoxelShape HOLDER_N = Block.box(10, 15, 6.5, 14, 16.5, 9.5);
    private static final VoxelShape HOLDER_S = Block.box(2, 15, 6.5, 6, 16.5, 9.5);
    private static final VoxelShape HOLDER_E = Block.box(6.5, 15, 10, 9.5, 16.5, 14);
    private static final VoxelShape HOLDER_W = Block.box(6.5, 15, 2, 9.5, 16.5, 6);
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case EAST -> Shapes.or(BASE, HOLDER_E);
            case WEST -> Shapes.or(BASE, HOLDER_W);
            case SOUTH -> Shapes.or(BASE, HOLDER_S);
            default -> Shapes.or(BASE, HOLDER_N);
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }
}
