package com.victorfaurschou.instantactions.mixin;

import com.victorfaurschou.instantactions.PlayerConfig;
import com.victorfaurschou.instantactions.PlayerConfigStore;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {
	@Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
	private void onUseItemOn(
		ItemStack itemStack,
		BlockState state,
		Level level,
		BlockPos pos,
		Player player,
		InteractionHand hand,
		BlockHitResult hitResult,
		CallbackInfoReturnable<InteractionResult> cir
	) {
		if (level.isClientSide()) return;
		PlayerConfig config = PlayerConfigStore.get(player);
		if (config == null || !config.instantCompost()) return;

		int fillLevel = state.getValue(ComposterBlock.LEVEL);
		if (fillLevel >= 7) return;
		if (!ComposterBlock.COMPOSTABLES.containsKey(itemStack.getItem())) return;

		int levelsNeeded = 7 - fillLevel;
		int levelsToFill = Math.min(levelsNeeded, itemStack.getCount());

		BlockState newState = state.setValue(ComposterBlock.LEVEL, fillLevel + levelsToFill);
		level.setBlock(pos, newState, 3);
		level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));

		if (fillLevel + levelsToFill == 7) {
			level.scheduleTick(pos, state.getBlock(), 20);
		}

		level.levelEvent(1500, pos, 1);
		player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
		itemStack.consume(levelsToFill, player);

		cir.setReturnValue(InteractionResult.SUCCESS);
	}
}
