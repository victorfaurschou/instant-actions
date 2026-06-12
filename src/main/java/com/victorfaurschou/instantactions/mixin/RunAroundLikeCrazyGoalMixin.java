package com.victorfaurschou.instantactions.mixin;

import com.victorfaurschou.instantactions.PlayerConfig;
import com.victorfaurschou.instantactions.PlayerConfigStore;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RunAroundLikeCrazyGoal.class)
public class RunAroundLikeCrazyGoalMixin {
	@Shadow
	private AbstractHorse horse;

	@Unique
	private int tameTicks = -1;
	@Unique
	private Player lastRider = null;

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void onTick(CallbackInfo ci) {
		Entity passenger = this.horse.getFirstPassenger();
		if (!(passenger instanceof Player player)) {
			tameTicks = -1;
			lastRider = null;
			return;
		}

		PlayerConfig config = PlayerConfigStore.get(player);
		if (config == null || !config.instantTaming()) return;

		if (player != lastRider) {
			lastRider = player;
			tameTicks = 40 + this.horse.getRandom().nextInt(21);
		}

		tameTicks--;
		if (tameTicks <= 0) {
			this.horse.tameWithName(player);
			tameTicks = -1;
			lastRider = null;
		}

		ci.cancel();
	}
}
