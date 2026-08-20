# Farmer's Delight - Unofficial NeoForge 26.2 Port

> **Unofficial community port.** This project is based on
> [Farmer's Delight](https://github.com/vectorwing/FarmersDelight), originally
> created by **vectorwing**. This project is not affiliated with, maintained by,
> partnered with, or endorsed by vectorwing or the official Farmer's Delight
> project.

This repository adapts Farmer's Delight 1.3.2 to Minecraft 26.2 and
NeoForge 26.2. To preserve world-save and add-on compatibility, this port
keeps the original `farmersdelight` mod id.


## Attribution

- Original author: **vectorwing**
- Original project: <https://github.com/vectorwing/FarmersDelight>
- Original license: [MIT License](LICENSE)
- Port role: **unofficial port maintainer**
- Port maintainer: **LexonBlackzz**
- Port source: <https://github.com/LexonBlackzz/FarmersDelight>

This project preserves the original copyright notice and MIT License. Original
project contributors remain credited in the mod metadata. Maintaining this port
does not transfer authorship of the original mod.

## Installation

1. Install Minecraft 26.2
2. Install NeoForge 26.2. The current build uses NeoForge `26.2.0.59`.
3. Download this port's jar, for example
   `FarmersDelight-26.2-1.3.2-port.40.jar`.
4. Put the jar into the `mods` folder of the target instance.
5. Start the game and confirm that the mod list shows
   `Farmer's Delight - Unofficial NeoForge 26.2 Port`.

This port does not require extra dependency mods by itself. Optional
integrations such as EMI, AppleSkin, and CraftTweaker are not yet ported
to 26.2, so they are not required dependencies.

## Current Limitations

- Optional integration source for EMI, AppleSkin, and CraftTweaker is not
  enabled yet.
- Data-generator source is not fully migrated yet; the current release package
  ships the already usable generated data resources directly.
- Some villager events, older rendering hooks, and other non-core systems may
  still need follow-up porting.
- Multiplayer servers, complex modpacks, add-on compatibility, and old-world
  upgrades still need broader testing.

## Redistribution and Support

- Do not describe this project as an official new version, official update, or
  official release.
- Report port-specific issues to this port repository, not to the upstream
  Farmer's Delight issue tracker.
- Only add third-party assets or dependencies when their licenses and
  redistribution terms are known and preserved.
