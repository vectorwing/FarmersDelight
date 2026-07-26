# Farmer's Delight 26.2 Port TODO

Current state observed on 2026-07-26:

- `./gradlew.bat compileJava` succeeds with the core source tree and JEI included.
- `./gradlew.bat build` succeeds with the core source tree, JEI compile API, and the current temporary integration excludes.
- `./gradlew.bat runClientData` succeeds through `ExistingGeneratedResources`, a scoped 26.2 `BlockStates` provider for canvas signs/hanging signs, the 26.2 `ItemModels` provider, and `SoundDefinitions`.
- `./gradlew.bat runServerData` succeeds after changing cooking-pot tag ingredients to serialize unresolved tag references and adding canvas wall-sign loot tables.
- `build.gradle` currently excludes only `vectorwing/farmersdelight/integration/emi/**` and `vectorwing/farmersdelight/integration/crafttweaker/**`, because the checked Maven coordinates do not provide usable 26.2-compatible APIs yet.

## Must Fix Before Release

- [ ] Remove the remaining temporary Java source excludes from `build.gradle` and make EMI/CraftTweaker compile once usable 26.2 APIs exist.
- [ ] Replace `ExistingGeneratedResources` with real datagen coverage; it currently preserves pre-existing generated files so client/server datagen do not delete each other's output.
- [ ] Re-port the rest of `BlockStates` to the 26.2/NeoForge client model datagen APIs.
- [x] Re-port canvas sign and hanging canvas sign blockstates/models to the 26.2 `ModelProvider` API.
- [x] Re-port `ItemModels` to the 26.2 item model/datagen APIs for non-skillet generated item definitions and item models.
- [ ] Re-port the skillet's 26.2 item definition/model behavior once its special item renderer and cooking predicate are restored.
- [x] Fix server datagen lookup inputs so `runServerData` can emit common tags such as `c:drinks/milk`.
- [x] Add generated loot tables for canvas wall signs and hanging canvas wall signs.
- [x] Regenerate data and assets with `runClientData` and `runServerData`.
- [ ] Review the remaining generated resource diff for stale 1.21 formats and bridge-preserved files.
- [ ] Re-port cooking pot recipe book support:
  - [ ] Replace the placeholder `RecipeCategories` implementation with the 26.2 recipe book registry model.
  - [ ] Replace the placeholder `CookingPotRecipeBookComponent`.
  - [ ] Stop falling back to `RecipeBookType.FURNACE` and `RecipeBookCategories.FURNACE_FOOD` where Farmer's Delight needs its own cooking pot categories.
  - [ ] Verify `META-INF/enumextensions.json` is still the right way to register `FARMERSDELIGHT_COOKING`, or replace it with the 26.2-native registry path.
- [ ] Re-port HUD overlays for Nourishment and Comfort in `HUDOverlays`.
- [ ] Re-port the skillet client item behavior in `ClientSetupEvents` and `SkilletItemRenderer`.
- [ ] Re-port canvas rug block-breaking texture suppression in `HideBlockBreakProgressMixin` and re-add it to `farmersdelight.mixins.json` only after it targets the 26.2 renderer pipeline correctly.
- [ ] Revisit mixins removed from `farmersdelight.mixins.json`:
  - [ ] Decide whether `datafix.V3818_3Mixin` is obsolete or needs a 26.2 equivalent.
  - [ ] Decide whether `KeepRichSoilGiantTreeMixin` is obsolete or needs a 26.2 equivalent.
- [ ] Implement block/entity item transfer support for 26.2:
  - [ ] Basket capability/transfer registration.
  - [ ] Cabinet capability/transfer registration.
  - [ ] Cooking Pot sided input/output transfer registration.
  - [ ] Cutting Board single-slot transfer registration.
  - [ ] Skillet and stove inventory transfer behavior audit.
  - [ ] Update utility APIs that still expose `IItemHandler` if NeoForge transfer is now the supported integration surface.
- [ ] Replace the old villager-trade config toggles with a data-driven 26.2-compatible mechanism, or intentionally remove the toggles and document the behavior change.
- [ ] Re-enable and port optional integrations:
  - [ ] JEI recipe population; the classes compile against JEI `30.14.0.93`, but custom recipe registration currently returns empty lists.
  - [ ] EMI; official `dev.emi:emi-neoforge` metadata still targets 1.21.1-era Minecraft versions.
  - [ ] AppleSkin runtime/dev setup.
  - [ ] CraftTweaker recipe managers, docs, and scripts; `CraftTweaker-neoforge-26.2` is not published on BlameJared Maven as of 2026-07-26.
- [ ] Update optional dependency coordinates in `gradle.properties`; JEI is now on a 26.2 API, but EMI, AppleSkin, and CraftTweaker still target 1.21-era artifacts.
- [ ] Update `neoforge.mods.toml` optional dependency ranges, especially CraftTweaker, to match the final 26.2-compatible versions.

## Functional QA

- [ ] Launch a 26.2 client with only Farmer's Delight installed.
- [ ] Launch a 26.2 dedicated server with only Farmer's Delight installed.
- [ ] Place, break, rotate, waterlog, and render every block family.
- [ ] Test all crops: planting, growth, bonemeal, harvesting, wild crop generation, rich soil behavior, and villager crop targeting.
- [ ] Test Cooking Pot input, cooking progress, output serving, shift-clicking, recipe unlocks, recipe book UI, XP, custom name, block-item meal storage, and comparator output.
- [ ] Test Cutting Board manual use, dispenser use, fortune outputs, invalid tool feedback, and item carving.
- [ ] Test Skillet held cooking, placed cooking, flipping, custom renderer/model, sounds, combat, and drops.
- [ ] Test Basket/Cabinet automation with vanilla hoppers and common item-transfer mods once integrations are available.
- [ ] Test all loot modifiers: structure loot, mob scavenging, grass/straw drops, and pastry slicing.
- [ ] Test datafix/migration paths with old worlds/items containing Farmer's Delight block entities and componentized item data.
- [ ] Test all network payloads on a real client-server connection.
- [ ] Test translations after the 26.2 item/block translation-key alias changes.

## Release Cleanup

- [ ] Remove or resolve all `TODO 26.2` placeholders.
- [ ] Remove temporary build exclusions and commented-out runtime dependencies.
- [ ] Confirm `./gradlew.bat clean build` succeeds.
- [ ] Confirm `./gradlew.bat runClientData` and `./gradlew.bat runServerData` both succeed from a clean checkout.
- [ ] Confirm the generated resource diff is intentional and reproducible.
- [ ] Update README, changelog, supported Minecraft/NeoForge version ranges, and release metadata.
- [ ] Build the release jar and smoke-test it outside the dev workspace.
