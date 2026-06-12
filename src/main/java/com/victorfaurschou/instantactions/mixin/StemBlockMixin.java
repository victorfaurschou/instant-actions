package com.victorfaurschou.instantactions.mixin;

import com.victorfaurschou.instantactions.InstantActionsConfig;
import com.victorfaurschou.instantactions.PlayerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(StemBlock.class)
public class StemBlockMixin {
	@Shadow private ResourceKey<Block> fruit;
	@Shadow private ResourceKey<Block> attachedStem;
	@Shadow private TagKey<Block> fruitSupportBlocks;

	@Inject(method = "performBonemeal", at = @At("TAIL"))
	private void onPerformBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state, CallbackInfo ci) {
		PlayerConfig config = InstantActionsConfig.BONE_MEAL_PLAYER_CONFIG.get();
		if (config == null || !config.instantBoneMeal()) return;

		BlockState current = level.getBlockState(pos);
		if (!(current.getBlock() instanceof StemBlock)) return;
		if (current.getValue(StemBlock.AGE) != StemBlock.MAX_AGE) return;

		Registry<Block> blocks = level.registryAccess().lookupOrThrow(Registries.BLOCK);
		Optional<Block> fruitOpt = blocks.getOptional(this.fruit);
		Optional<Block> stemOpt = blocks.getOptional(this.attachedStem);
		if (fruitOpt.isEmpty() || stemOpt.isEmpty()) return;

		for (Direction direction : new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
			BlockPos adjacent = pos.relative(direction);
			if (level.getBlockState(adjacent).isAir() && level.getBlockState(adjacent.below()).is(this.fruitSupportBlocks)) {
				level.setBlockAndUpdate(adjacent, fruitOpt.get().defaultBlockState());
				level.setBlockAndUpdate(pos, stemOpt.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, direction));
				return;
			}
		}
	}
}
