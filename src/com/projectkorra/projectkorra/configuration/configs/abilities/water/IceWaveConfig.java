package com.projectkorra.projectkorra.configuration.configs.abilities.water;

import com.projectkorra.projectkorra.configuration.configs.abilities.AbilityConfig;

public class IceWaveConfig extends AbilityConfig {

	public final long Cooldown = 5000;
	
	public final double ThawRadius = 30;
	public final double Damage = 2;
	public final boolean RevertSphere = true;
	public final long RevertSphereTime = 5000;
	
	public final double AvatarState_Damage = 5;
	
	public IceWaveConfig() {
		super(true, "PhaseChange your WaterWave into an IceWave that freezes and damages enemies.", "Create a WaterSpout Wave > PhaseChange (Left Click)");
	}

	@Override
	public String getName() {
		return "IceWave";
	}

	@Override
	public String[] getParents() {
		return new String[] { "Abilities", "Water", "Combos" };
	}

}