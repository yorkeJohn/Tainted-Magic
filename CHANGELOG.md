# Tainted Magic Changelog

## Unreleased

### Changed

- Removed the update checker and the `NOTIFY_UPDATE` config option.
- Focus upgrade IDs can now be set to any value from 20 upwards.
- Migrated the build to RetroFuturaGradle and the Gradle Kotlin DSL.
- Added palantir-java-format via Spotless (`./gradlew spotlessApply`).

### Fixed

- Fixed focus upgrade IDs not using the values set in the config, which could cause the wrong upgrade to be applied to a focus (#83, #88). The game now stops at startup with a clear error if a configured focus upgrade ID is already used by another mod.
- Fixed unlocalized text on the last page of the Thaumic Disassembler research entry (#85).
- Fixed research entries with infusion recipes not requiring the Infusion research.
- Fixed the Thaumic Disassembler working at full speed with a negative entropy charge, e.g. after vein mining (#80).
- Fixed Fortress Blade sayas and the Flyte Charm magic circle not rendering for other players on servers (#35). These effects are now also hidden on invisible players.
- Fixed Flyte Charm flight state not being cleared when a player logs out.
- Fixed a crash when releasing a charged Fortress Blade strike while not looking at anything.
- Fixed charged Fortress Blade strikes being calculated by the client. Strikes are now handled by the server, which uses normal melee reach and respects the server's PvP setting.
- Fixed Fortress Blade charge being shared between players.
- Fixed typos and outdated research text in the English, Russian and Chinese localizations.

## 8.1.1

### Added

- Added Chinese (zh_CN) and Russian (ru_RU) localization.

## 8.1.0

### Changed

- Changed the way Salis Aevum and Salis Tempestas work, and added visual effects.
- Changed the way Wand Focus: Lumos and Ring of Lumos work - no more laggy dynamic lighting.
- Improved the effects when completing the Shard of Creation infusion.
- Refined localization (fixed grammar, wording, typos).

## 8.0.4

### Changed

- Removed logo from mcmod.info - reduces archive size.

### Fixed

- Fixed a bug where breaking any block with shears would drop Atropa Belladonna.

## 8.0.3

### Changed

- Tweaked inscription recipes.

### Fixed

- Fixed a bug where inscription recipes were incorrect in the Thaumonomicon.

## 8.0.2

### Changed

- Changed versioning scheme: [major version].[minor version].[patch].
- Decreased the Magic Funguar's food and saturation values.

### Fixed

- Fixed a bug where the Flyte Charm would disable all other flight.

## r8.1

### Fixed

- Fixed a bug where the game would crash when using the Dark Matter focus.

## r8.0

### Added

- Re-added the old particles when walking with the Boots of the Voidwalker. Not as invasive as the original.
- Added Atropa Belladonna plants which grow at the base of Warpwood Trees, dropping Nightshade Berries when broken.
- Added Nightshade Berries.
- Added Warping Fertilizer.
- Added the Fragment of Creation. Subunit of the Shard of Creation. 1 shard can be broken down into 9 fragments. Used for crafting recipes that require some creation-y goodness but where an entire shard would be too expensive.
- Added Salis Tempestas and Salis Aevum. When thrown they change the weather or time respectively, the same way that the Meteorology and Time foci did in previous versions. Single use.
- Added the Shockwave Focus. Works the same as the Tainted Shockwave used to, but attacks them with lightning instead.
- Added the Taint Swarm focus. The focus summons a tamed Taint Swarm to fight for the player. The player must be looking at a valid target to summon a swarm. The swarm will pathfind to this target and will only attack this target. The swarm dies when the target dies. The focus can be used on cooldown to summon multiple swarms.
- Added a way to craft Vishrooms.
- Added a config option to define how powerful the Mage's Mace focus is.
- Added a way to display info to the player temporarily on the HUD instead of via chat messages. Used in various places.
- Added the Celestial Gate Key. Bind it to a location to return to that location from anywhere within the same dimension. One location per key, bound location is permanent.
- Added the Vengeful Spirit Inscription, can be added to Fortress Blades. Casts a mini shockwave focus.
- Added the Benevolent Goddess Inscription, can be added to Fortress Blades. Heals the user while inflicting Wither on nearby entities.
- Added hoe function to the Thaumic Disassembler.

### Changed

- Shadowmetal -> Shadow Metal.
- Changed the system for obtaining materials to make Crimson Cult equipment.
- Replaced the Crystal Dagger with the Hollow Dagger.
- Replaced Phial of Crystal Blood with Phial of Crimson Blood.
- To obtain Phials of Crimson Blood the player must hit entities with a Hollow Dagger and must have empty Glass Phials in their inventory.
- Removed the "Bloodlust" attribute from weapons with which Crystal Blood was previously obtainable. Phials of Crimson Blood now may only be obtained with a Hollow Dagger.
- Replaced the Tainted Shockwave focus with the Shockwave Focus, see Added for details. The crafting recipe for this item was changed significantly as well.
- Replaced the Tainted Storm focus with the Taint Swarm focus. Complete overhaul, see Added.
- Removed the Glowpet and Lumos Maxima upgrades to the Lumos focus. To replace the functionality of the Glowpet upgrade, the Ring of Lumos has been added. See Added for details.
- Lumos light sources now have a hitbox.
- Removed the Meteorology and Time foci, replaced with Salis Tempestas and Salis Aevum respectively. See Added for details.
- Warpwood Saplings can no longer be crafted via infusion. Warping Fertilizer must be used on a Silverwood Sapling to create a Warpwood Sapling.
- Changed how Warpwood trees grow, as well as the leaf and log textures. They now are shaped like Silverwood trees.
- Similar to the node inside of Silverwood trees, there is a chance that a knot will generate inside of Warpwood trees. The knot drops 1-5 void seeds.
- Changed the formula with which the Warpwood Wand/Staff's vis replenishment rate is calculated.
- Slightly changed the visual effects for Lumos light sources.
- Voidmetal Goggles of Revealing now provide 1 point of armour and a vis discount of 12%.
- Voidsent Blood -> Void-infused Blood. Can now be applied to any armour.
- The player now must first research the diffusion upgrade before they can apply it to the Dark Matter Focus.
- Removed the Tainted Fortress Blade Inscription.
- Removed the Undead Fortress Blade Inscription.
- Volcanic Inscription -> Raging Demon Inscription.
- Significantly changed recipes for Crimson Cult Equipment.
- Changed recipes to make them more balanced.
- Improved textures.
- Significantly changed Thaumonomicon entries and research.
- Removed the Flyte Charm's burst function.
- Removed the Flyte Charm's speed boost.
- Improved the Flyte Charm's glide feature.
- Changed vis costs for foci.
- Removed Block of Shadow Metal.
- Vis discounts for the Shadow Metal Caps, Cloth Caps, Crimson-stained Caps, and Shadow-imbued Caps were changed to 35%, 3%, 7%, and 7% respectively.
- Removed Shadow Metal Ore.
- Reduced the speed boost associated with the Boots of the Voidwalker.
- Reworked the Thaumic Disassembler to be charged using entropy vis.
- Edge of the Primordials -> Primal Blade.

### Fixed

- Fixed a bug in which foci could not be upgraded when playing on a server.
- Fixed a bug where a Katana with the Volcanic Inscription would deal damage within an ungodly radius.
- Fixed a bug where the Lumos focus would replace nodes.
- Fixed a bug where the Fortress Blade bar would not position properly at different GUI scales.

## r7.82

### Changed

- Thaumonomicon layout improvements.

### Fixed

- Bug fixes.

## r7.81

### Fixed

- Bug fixes.

## r7.8

### Added

- Flyte Charm.
- New materials (stolen from Thaumcraft 6).

### Changed

- Improved some particle effects.
- Texture improvements.
- Made adjustments to recipes.
- Made adjustments to research and prerequisites.

### Fixed

- Fixed bugs where servers would crash under certain conditions.
- Other bug fixes.

## r7.7

### Added

- Lumos focus.
- Diffusion upgrade for the Dark Matter focus.
- Shadowmetal nuggets.
- Particles when using the Time and Meteorology foci.

### Changed

- Adjusted damage values for the Fortress Blades and certain foci.
- New Thaumonomicon layout.
- Reworked Thaumonomicon prerequisites.
- Texture changes.
- New formula to calculate the recharge rate of the Warpwood Wand Rod / Staff Core.
- Some Thaumonomicon entries were rewritten.
- Recipe adjustments.
- Reworked focus upgrades.
- Reworked the Tainted Storm focus with a new renderer and movement physics.

### Fixed

- Bug fixes and code cleanup.
- Fixed various rendering problems... finally.

## r7.6

### Added

- Added the Undead Inscription.
- Added the Vis Shard focus from TC5.

### Changed

- Removed the Stormbound Inscription and replaced it with the Undead Inscription.
- Changed the Volcanic Inscription to emit a single explosive fireball.
- Balanced katana projectile damage.
- Retextured some stuff.
- Made the Shard of Creation research secondary.

### Fixed

- Fixed some OreDict stuff.
- Code cleanup.

## r7.5

### Fixed

- Bug fixes.

## r7.4

### Changed

- HUD changes.
- Changes to the Stormbound Inscription.

### Fixed

- Fixed some Thaumonomicon entries.
- Bug fixes.

## r7.3

### Added

- Voidmetal Goggles of Revealing.

### Fixed

- Fixed some Thaumonomicon entries.
- Fixed some values.

## r7.2

### Added

- Upgrades to the Fortress Blades as inscriptions.
- Hold down right click to charge a Fortress Blade, releasing a powered strike.

### Changed

- Tweaked the rendering, animation, and SFX for the Tainted Storm wand focus.
- Tweaked some rendering for the Fortress Blades.

### Fixed

- Various bug fixes and texture improvements.
- Fixed the Thaumic Disassembler text.
- Fixed the Thaumonomicon entry for Shadowmetal Fortress armor, and Thaumium Fortress Blades.
- Fixed a few recipes.

## r7.1

### Fixed

- Bug fixes, fixed crashes.

## r7.0

Initial release of Release 7. Significant changes from Release 6. Release 6 worlds are not compatible with this update.
