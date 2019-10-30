package com.projectkorra.projectkorra.configuration.configs.abilities.fire;

import com.projectkorra.projectkorra.configuration.configs.abilities.AbilityConfig;

public class FireKickConfig extends AbilityConfig {

	public final long Cooldown = 0;
	public final double Damage = 0;
	public final double Range = 0;
	public final double Speed = 0;
	
	public final double AvatarState_Damage = 0;
	public final double AvatarState_Range = 0;
	
	public FireKickConfig() {
		super(true, "A short ranged arc of fire launches from the player's feet dealing moderate damage to enemies.", "FireBlast > FireBlast > (Hold sneak) > FireBlast");
	}

	@Override
	public String getName() {
		return "FireKick";
	}

	@Override
	public String[] getParents() {
		return new String[] { "Abilities", "Fire", "Combos" };
	}

}