package com.victorfaurschou.instantactions.mixin;

import com.victorfaurschou.instantactions.InstantActionsConfig;
import com.victorfaurschou.instantactions.PlayerConfig;
import com.victorfaurschou.instantactions.PlayerConfigStore;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
	@Unique
	private static final ThreadLocal<Boolean> APPLYING_RADIUS = ThreadLocal.withInitial(() -> false);

	@Inject(method = "useOn", at = @At("HEAD"))
	private void onUseOnHead(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		if (context.getLevel().isClientSide()) return;
		Player player = context.getPlayer();
		InstantActionsConfig.BONE_MEAL_PLAYER_CONFIG.set(player != null ? PlayerConfigStore.get(player) : null);
	}

	@Inject(method = "useOn", at = @At("RETURN"))
	private void onUseOnReturn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		InstantActionsConfig.BONE_MEAL_PLAYER_CONFIG.remove();
	}

	@Inject(method = "growCrop", at = @At("HEAD"), cancellable = true)
	private static void onGrowCrop(ItemStack itemStack, Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		PlayerConfig config = InstantActionsConfig.BONE_MEAL_PLAYER_CONFIG.get();
		boolean doInstant = config != null && config.instantBoneMeal();
		boolean doRadius = config != null && config.boneMealWithRadius() && !APPLYING_RADIUS.get();

		if (!doInstant && !doRadius) return;
		if (!(level instanceof ServerLevel serverLevel)) return;

		BlockState state = level.getBlockState(pos);
		if (!(state.getBlock() instanceof BonemealableBlock bonemealable)) return;
		if (bonemealable.getType() != BonemealableBlock.Type.GROWER) return;
		if (!bonemealable.isValidBonemealTarget(level, pos, state)) return;

		Block cropBlock = state.getBlock();
		boolean grew = false;

		if (doInstant) {
			for (int i = 0; i < 16 && !itemStack.isEmpty(); i++) {
				state = level.getBlockState(pos);
				if (!(state.getBlock() instanceof BonemealableBlock current)) break;
				if (!current.isValidBonemealTarget(level, pos, state)) break;
				current.performBonemeal(serverLevel, level.getRandom(), pos, state);
				itemStack.shrink(1);
				grew = true;
			}
		} else {
			if (bonemealable.isBonemealSuccess(level, level.getRandom(), pos, state)) {
				bonemealable.performBonemeal(serverLevel, level.getRandom(), pos, state);
			}
			itemStack.shrink(1);
			grew = true;
		}

		if (doRadius && !itemStack.isEmpty()) {
			APPLYING_RADIUS.set(true);
			try {
				for (int dx = -1; dx <= 1; dx++) {
					for (int dz = -1; dz <= 1; dz++) {
						if (dx == 0 && dz == 0) continue;
						if (itemStack.isEmpty()) break;
						BlockPos neighbor = pos.offset(dx, 0, dz);
						if (level.getBlockState(neighbor).getBlock() != cropBlock) continue;
						BoneMealItem.growCrop(itemStack, level, neighbor);
					}
				}
			} finally {
				APPLYING_RADIUS.set(false);
			}
		}

		if (grew) {
			cir.setReturnValue(true);
		}
	}
}
