package com.victorfaurschou.instantactions.mixin;

import com.victorfaurschou.instantactions.PlayerConfig;
import com.victorfaurschou.instantactions.PlayerConfigStore;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

@Mixin(Block.class)
public class CropBlockMixin {
	@Inject(method = "playerDestroy", at = @At("TAIL"))
	private void onPlayerDestroy(Level level, Player player, BlockPos origin, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
		PlayerConfig config = PlayerConfigStore.get(player);
		if (config == null || !config.chainHarvest()) return;
		if (!((Object) this instanceof CropBlock cropBlock)) return;
		if (!(level instanceof ServerLevel)) return;
		if (!cropBlock.isMaxAge(state)) return;

		Block targetBlock = state.getBlock();
		Set<BlockPos> visited = new HashSet<>();
		Queue<BlockPos> queue = new ArrayDeque<>();
		visited.add(origin);

		enqueueNeighbors(origin, origin, level, targetBlock, cropBlock, visited, queue);

		while (!queue.isEmpty()) {
			BlockPos current = queue.poll();
			BlockState currentState = level.getBlockState(current);
			enqueueNeighbors(origin, current, level, targetBlock, cropBlock, visited, queue);
			Block.dropResources(currentState, level, current, null, player, tool);
			level.removeBlock(current, false);
		}
	}

	private static void enqueueNeighbors(BlockPos origin, BlockPos pos, Level level, Block targetBlock, CropBlock cropBlock, Set<BlockPos> visited, Queue<BlockPos> queue) {
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (dx == 0 && dz == 0) continue;
				BlockPos neighbor = pos.offset(dx, 0, dz);
				if (visited.contains(neighbor)) continue;
				if (Math.abs(neighbor.getX() - origin.getX()) > 3 || Math.abs(neighbor.getZ() - origin.getZ()) > 3) continue;
				visited.add(neighbor);
				BlockState neighborState = level.getBlockState(neighbor);
				if (neighborState.getBlock() == targetBlock && cropBlock.isMaxAge(neighborState)) {
					queue.add(neighbor);
				}
			}
		}
	}
}
