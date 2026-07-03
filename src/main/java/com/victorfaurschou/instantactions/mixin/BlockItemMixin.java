package com.victorfaurschou.instantactions.mixin;

import com.victorfaurschou.instantactions.PlayerConfig;
import com.victorfaurschou.instantactions.PlayerConfigStore;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
	@Inject(method = "place", at = @At("RETURN"))
	private void onPlace(BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
		if (cir.getReturnValue() != InteractionResult.SUCCESS) return;
		Level level = context.getLevel();
		if (!(level instanceof ServerLevel)) return;
		Player player = context.getPlayer();
		if (player == null) return;
		PlayerConfig config = PlayerConfigStore.get(player);
		if (config == null || !config.doubleSeeding()) return;

		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		if (!(state.getBlock() instanceof CropBlock)) return;

		ItemStack itemStack = context.getItemInHand();

		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (dx == 0 && dz == 0) continue;
				if (itemStack.isEmpty()) break;
				BlockPos neighbor = pos.offset(dx, 0, dz);
				if (!level.getBlockState(neighbor).isAir()) continue;
				if (!state.canSurvive(level, neighbor)) continue;
				level.setBlock(neighbor, state, 3);
				itemStack.consume(1, player);
			}
		}
	}
}
