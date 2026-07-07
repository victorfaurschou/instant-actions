package com.victorfaurschou.instantactions.mixin;

import com.victorfaurschou.instantactions.PlayerConfig;
import com.victorfaurschou.instantactions.PlayerConfigStore;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Parrot.class)
public abstract class ParrotMixin {
	@Unique
	private Player instantActions$feedingPlayer;

	@Inject(method = "mobInteract", at = @At("HEAD"))
	private void onMobInteractHead(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		this.instantActions$feedingPlayer = player;
	}

	// Vanilla only tames on a 1-in-10 chance per seed, checked inline in mobInteract. Force a guaranteed roll.
	@Redirect(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
	private int onTameRoll(RandomSource random, int bound) {
		Player player = this.instantActions$feedingPlayer;
		PlayerConfig config = player == null ? null : PlayerConfigStore.get(player);
		if (config == null || !config.instantTaming()) return random.nextInt(bound);
		return 0;
	}
}
