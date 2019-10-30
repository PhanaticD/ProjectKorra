package com.projectkorra.projectkorra.ability;

import com.projectkorra.projectkorra.ability.info.PassiveAbilityInfo;
import com.projectkorra.projectkorra.element.Element;
import com.projectkorra.projectkorra.element.SubElement;
import com.projectkorra.projectkorra.module.Module;
import com.projectkorra.projectkorra.module.ModuleManager;
import com.projectkorra.projectkorra.player.BendingPlayer;
import com.projectkorra.projectkorra.player.BendingPlayerManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassiveAbilityManager extends Module {

	private final BendingPlayerManager bendingPlayerManager;
	private final AbilityManager abilityManager;

	private final Map<Class<? extends Ability>, PassiveAbilityInfo> abilities = new HashMap<>();

	private PassiveAbilityManager() {
		super("Passive Ability");

		this.bendingPlayerManager = ModuleManager.getModule(BendingPlayerManager.class);
		this.abilityManager = ModuleManager.getModule(AbilityManager.class);
	}

	public void registerAbility(Class<? extends Ability> abilityClass, PassiveAbilityInfo passiveAbilityInfo) {
		this.abilities.put(abilityClass, passiveAbilityInfo);
	}

	public void registerPassives(Player player) {
		this.abilities.forEach((abilityClass, passiveAbilityInfo) -> {
			if (!canUsePassive(player, abilityClass)) {
				return;
			}

			if (this.abilityManager.hasAbility(player, abilityClass)) {
				return;
			}

			if (!passiveAbilityInfo.isInstantiable()) {
				return;
			}

			Ability ability = this.abilityManager.createAbility(player, abilityClass);
			ability.start();
		});
	}

	public boolean canUsePassive(Player player, Class<? extends Ability> abilityClass) {
		BendingPlayer bendingPlayer = this.bendingPlayerManager.getBendingPlayer(player);
		PassiveAbilityInfo passiveAbilityInfo = this.abilities.get(abilityClass);

		if (passiveAbilityInfo == null) {
			return false;
		}

		Element element = passiveAbilityInfo.getElement();

		if (element instanceof SubElement) {
			element = ((SubElement) element).getParent();
		}

		//		if (!bendingPlayer.canBendPassive(abilityClass)) {
		//			return false;
		//		}

		if (!bendingPlayer.isToggled()) {
			return false;
		}

		if (!bendingPlayer.isElementToggled(element)) {
			return false;
		}

		return true;
	}

	public PassiveAbilityInfo getPassiveAbility(Class<? extends Ability> abilityClass) {
		return this.abilities.get(abilityClass);
	}

	public List<PassiveAbilityInfo> getPassives(Element element) {
		List<PassiveAbilityInfo> abilities = new ArrayList<>();

		this.abilities.values().forEach(passiveAbilityInfo -> {

			Element passiveElement = passiveAbilityInfo.getElement();

			if (passiveElement instanceof SubElement) {
				passiveElement = ((SubElement) passiveElement).getParent();
			}

			if (passiveElement.equals(element)) {
				abilities.add(passiveAbilityInfo);
			}
		});

		return abilities;
	}
}