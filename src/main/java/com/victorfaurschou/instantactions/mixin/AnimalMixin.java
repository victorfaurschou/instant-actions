package com.victorfaurschou.instantactions.mixin;

import com.victorfaurschou.instantactions.PlayerConfig;
import com.victorfaurschou.instantactions.PlayerConfigStore;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class AnimalMixin {
	@Unique
	private Player instantActions$feedingPlayer;

	@Inject(method = "mobInteract", at = @At("HEAD"))
	private void onMobInteractHead(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		this.instantActions$feedingPlayer = player;
	}

	// Vanilla passes getSpeedUpSecondsWhenFeeding(-age) here, which only speeds up growth by ~10%.
	// Replace it with enough seconds to reach age 0 in one go; ageUp() clamps to 0, so overshooting is safe.
	@ModifyArg(
		method = "mobInteract",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Animal;ageUp(IZ)V"),
		index = 0
	)
	private int onFeedAgeUpSeconds(int seconds) {
		Player player = this.instantActions$feedingPlayer;
		if (player == null) return seconds;

		PlayerConfig config = PlayerConfigStore.get(player);
		if (config == null || !config.instantAnimalMaturing()) return seconds;

		int ticksUntilAdult = -((Animal) (Object) this).getAge();
		return (ticksUntilAdult + 19) / 20;
	}
}
