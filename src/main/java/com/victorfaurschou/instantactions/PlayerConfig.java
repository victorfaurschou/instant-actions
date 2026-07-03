package com.victorfaurschou.instantactions;

public record PlayerConfig(
	boolean instantCompost,
	boolean instantBoneMeal,
	boolean boneMealWithRadius,
	boolean instantTaming,
	boolean eatToFull,
	boolean chainHarvest,
	boolean doubleSeeding,
	boolean doubleTilling
) {}
