package com.victorfaurschou.instantactions.mixin;

import com.victorfaurschou.instantactions.PlayerConfig;
import com.victorfaurschou.instantactions.PlayerConfigStore;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodProperties.class)
public class FoodPropertiesMixin {
	// Injected at TAIL, before Consumable calls stack.consume(1, user).
	// stack.getCount() is still the pre-eat count here, so we leave 1 for that consume call.
	@Inject(method = "onConsume", at = @At("TAIL"))
	private void onEatToFull(Level level, LivingEntity user, ItemStack stack, Consumable consumable, CallbackInfo ci) {
		if (!(user instanceof Player player)) return;
		PlayerConfig config = PlayerConfigStore.get(player);
		if (config == null || !config.eatToFull()) return;

		FoodProperties food = stack.get(DataComponents.FOOD);
		if (food == null) return;

		FoodData foodData = player.getFoodData();
		int extra = 0;
		while (foodData.needsFood() && stack.getCount() > extra + 1) {
			foodData.eat(food);
			extra++;
		}
		if (extra > 0) {
			stack.shrink(extra);
		}
	}
}
