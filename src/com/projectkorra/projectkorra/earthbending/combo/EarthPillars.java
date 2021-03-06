package com.projectkorra.projectkorra.earthbending.combo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.earthbending.RaiseEarth;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.ParticleEffect.BlockData;

public class EarthPillars extends EarthAbility implements ComboAbility {
	public double radius, damage, power, fallThreshold;
	public boolean damaging;
	public Map<RaiseEarth, LivingEntity> entities;

	public EarthPillars(final Player player, final boolean fall) {
		super(player);
		this.setFields(fall);

		if (!this.bPlayer.canBendIgnoreBinds(this) || !isEarthbendable(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType(), true, true, false)) {
			return;
		}

		if (fall) {
			if (player.getFallDistance() < this.fallThreshold) {
				return;
			}
		}

		for (final Entity e : GeneralMethods.getEntitiesAroundPoint(player.getLocation(), this.radius)) {
			if (e instanceof LivingEntity && e.getEntityId() != player.getEntityId() && isEarthbendable(e.getLocation().getBlock().getRelative(BlockFace.DOWN).getType(), true, true, false)) {
				ParticleEffect.BLOCK_DUST.display(new BlockData(e.getLocation().clone().subtract(0, 1, 0).getBlock().getType(), (byte) 0), 1f, 0.1f, 1f, 0, 6, e.getLocation(), 255);
				this.affect((LivingEntity) e);
			}
		}

		if (this.entities.isEmpty()) {
			return;
		}

		this.start();
	}

	private void setFields(final boolean fall) {
		this.radius = getConfig().getDouble("Abilities.Earth.EarthPillars.Radius");
		this.damage = getConfig().getDouble("Abilities.Earth.EarthPillars.Damage.Value");
		this.power = getConfig().getDouble("Abilities.Earth.EarthPillars.Power");
		this.damaging = getConfig().getBoolean("Abilities.Earth.EarthPillars.Damage.Enabled");
		this.entities = new HashMap<>();

		if (fall) {
			this.fallThreshold = getConfig().getDouble("Abilities.Earth.EarthPillars.FallThreshold");
			this.damaging = true;
			this.damage *= this.power;
			this.radius = this.fallThreshold;
			this.power += (this.player.getFallDistance() > this.fallThreshold ? this.player.getFallDistance() : this.fallThreshold) / 100;
		}
	}

	public void affect(final LivingEntity lent) {
		final RaiseEarth re = new RaiseEarth(this.player, lent.getLocation().clone().subtract(0, 1, 0), 3);
		this.entities.put(re, lent);
	}

	@Override
	public void progress() {
		final List<RaiseEarth> removal = new ArrayList<>();
		for (final RaiseEarth abil : this.entities.keySet()) {
			if (abil.isRemoved() && abil.isStarted()) {
				final LivingEntity lent = this.entities.get(abil);
				if (!lent.isDead()) {
					if (lent instanceof Player && !((Player) lent).isOnline()) {
						continue;
					}

					lent.setVelocity(new Vector(0, this.power, 0));
				}
				if (this.damaging) {
					DamageHandler.damageEntity(lent, this.damage, this);
				}

				removal.add(abil);
			}
		}

		for (final RaiseEarth remove : removal) {
			this.entities.remove(remove);
		}

		if (this.entities.isEmpty()) {
			this.bPlayer.addCooldown(this);
			this.remove();
			return;
		}
	}

	@Override
	public boolean isSneakAbility() {
		return true;
	}

	@Override
	public boolean isHarmlessAbility() {
		return false;
	}

	@Override
	public long getCooldown() {
		return getConfig().getLong("Abilities.Earth.EarthPillars.Cooldown");
	}

	@Override
	public String getName() {
		return "EarthPillars";
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public Object createNewComboInstance(final Player player) {
		return new EarthPillars(player, false);
	}

	@Override
	public ArrayList<AbilityInformation> getCombination() {
		final ArrayList<AbilityInformation> earthPillars = new ArrayList<>();
		earthPillars.add(new AbilityInformation("Shockwave", ClickType.SHIFT_DOWN));
		earthPillars.add(new AbilityInformation("Shockwave", ClickType.SHIFT_UP));
		earthPillars.add(new AbilityInformation("Shockwave", ClickType.SHIFT_DOWN));
		earthPillars.add(new AbilityInformation("Catapult", ClickType.SHIFT_UP));
		return earthPillars;
	}

	@Override
	public String getInstructions() {
		return "Shockwave (Tap sneak) > Shockwave (Hold sneak) > Catapult (Release sneak)";
	}
}
