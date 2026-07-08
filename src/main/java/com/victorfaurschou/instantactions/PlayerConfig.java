package com.victorfaurschou.instantactions;

public record PlayerConfig(
	boolean doubleTilling,
	boolean doubleSeeding,
	boolean boneMealWithRadius,
	boolean instantBoneMeal,
	boolean chainHarvest,
	boolean instantCompost,
	boolean instantTaming,
	boolean instantAnimalMaturing,
	boolean eatToFull
) {}
