package com.victorfaurschou.instantactions.mixin;

import com.mojang.datafixers.util.Pair;
import com.victorfaurschou.instantactions.PlayerConfig;
import com.victorfaurschou.instantactions.PlayerConfigStore;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(HoeItem.class)
public class HoeItemMixin {
	@Shadow
	@Final
	protected static Map<Block, Pair<Predicate<UseOnContext>, Consumer<UseOnContext>>> TILLABLES;

	@Inject(method = "useOn", at = @At("RETURN"))
	private void onUseOnReturn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		if (cir.getReturnValue() != InteractionResult.SUCCESS) return;
		Level level = context.getLevel();
		if (!(level instanceof ServerLevel)) return;
		Player player = context.getPlayer();
		if (player == null) return;
		PlayerConfig config = PlayerConfigStore.get(player);
		if (config == null || !config.doubleTilling()) return;

		BlockPos pos = context.getClickedPos();
		ItemStack itemStack = context.getItemInHand();

		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (dx == 0 && dz == 0) continue;
				BlockPos neighbor = pos.offset(dx, 0, dz);
				Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> logicPair = TILLABLES.get(level.getBlockState(neighbor).getBlock());
				if (logicPair == null) continue;

				BlockHitResult hitResult = new BlockHitResult(Vec3.atCenterOf(neighbor), Direction.UP, neighbor, false);
				UseOnContext neighborContext = new UseOnContext(level, player, context.getHand(), itemStack, hitResult);
				if (!logicPair.getFirst().test(neighborContext)) continue;

				logicPair.getSecond().accept(neighborContext);
				itemStack.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
			}
		}
	}
}
