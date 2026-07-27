# Farmer's Delight 26.2 Port TODO

Current state observed on 2026-07-27:

- `./gradlew.bat compileJava` succeeds with the core source tree and JEI included.
- `./gradlew.bat build` succeeds with the core source tree, JEI compile API, AppleSkin compile API, and the current temporary integration excludes.
- `./gradlew.bat runClientData` succeeds through scoped generated `data` preservation, the fully re-ported 26.2 `BlockStates` provider, the 26.2 `ItemModels` provider, and `SoundDefinitions`.
- `./gradlew.bat runServerData` succeeds through scoped generated `assets` preservation after changing cooking-pot tag ingredients to serialize unresolved tag references and adding canvas wall-sign loot tables.
- `./gradlew.bat runGameTestServer` succeeds in the dev environment.
- `./gradlew.bat runServer` reaches the dedicated server `Done` state in the dev environment after removing a common-load dependency on client-only `RecipeUtils` from `RegistryAliases`.
- `./gradlew.bat runClient` reaches client resource startup with only Farmer's Delight installed; the latest smoke reaches OpenAL and block-atlas creation with no Farmer's Delight item-model parse errors or Farmer's Delight missing-texture warnings.
- `./gradlew.bat runServer -Pfd.serverWorld=fd_only_smoke` creates a fresh dedicated server world with only Farmer's Delight installed and reaches the server `Done` state.
- The built jar is `build/libs/FarmersDelight-26.2-1.3.2.jar`; jar inspection confirms generated resource cache files are excluded.
- CurseForge `Test 26.2` world-load failure from `latest.log` was traced to `FoodServingRecipe` failing NeoForge recipe-content sync; fixed the stateless custom recipe stream codecs for `FoodServingRecipe` and `DoughRecipe`, rebuilt, and copied the jar into the test instance for retesting.
- Canvas sign block models now use 26.2-style square block textures instead of the old 64x32 sign entity sheets, fixing misplaced/stretched placed-sign textures.
- `build.gradle` currently excludes only `vectorwing/farmersdelight/integration/emi/**` and `vectorwing/farmersdelight/integration/crafttweaker/**`, because the checked Maven coordinates do not provide usable 26.2-compatible APIs yet.
- AppleSkin was updated to `squeek.appleskin:appleskin-neoforge:mc26.2-3.0.10`; its API resolves for compilation, and its dev runtime is opt-in with `-Pfd.includeAppleSkinRuntime=true` so default smoke runs stay Farmer's Delight-only.
- Old EMI/CraftTweaker coordinates are opt-in with `-Pfd.includeUnavailableIntegrations=true` for future port work; default builds no longer resolve 1.21-era optional integration APIs.

## Must Fix Before Release

- [ ] Remove the remaining temporary Java source excludes from `build.gradle` and make EMI/CraftTweaker compile once usable 26.2 APIs exist.
- [x] Replace `ExistingGeneratedResources` with real datagen coverage; client/server datagen now only preserve the opposite generated pack while sharing `src/generated/resources`.
- [x] Re-port the rest of `BlockStates` to the 26.2/NeoForge client model datagen APIs.
  - [x] Re-port simple opaque block families with byte-stable generated output: rice bag, rice bale, crop crates, straw bale, rich soil, rich soil farmland, and organic compost.
  - [x] Re-port existing-model and horizontal/orientable block families with byte-stable generated output: safety net, canvas rug, baskets, cutting board, half tatami mat, cabinets, cooking pot, skillet, and stove.
  - [x] Re-port pie and feast blockstates/models with byte-stable generated output.
  - [x] Re-port crop, mushroom colony, wild crop, and double-plant blockstates/models with byte-stable generated output.
  - [x] Re-port rope, rope fence, rope fence gate, tatami, and full tatami mat blockstates/models with byte-stable generated output.
- [x] Re-port canvas sign and hanging canvas sign blockstates/models to the 26.2 `ModelProvider` API.
  - [x] Add a `minecraft:blocks` atlas contribution for Farmer's Delight canvas sign entity textures so generated sign block models resolve cleanly.
- [x] Re-port `ItemModels` to the 26.2 item model/datagen APIs for non-skillet generated item definitions and item models.
- [x] Re-port the skillet's 26.2 item definition/model behavior once its special item renderer and cooking predicate are restored.
- [x] Fix server datagen lookup inputs so `runServerData` can emit common tags such as `c:drinks/milk`.
- [x] Add generated loot tables for canvas wall signs and hanging canvas wall signs.
- [x] Regenerate data and assets with `runClientData` and `runServerData`.
- [x] Review the remaining generated resource diff for stale 1.21 formats and bridge-preserved files.
  - The current generated-resource diff removes stale block-item indirection models now covered by 26.2 item definitions, drops obsolete tag/global-loot manifest paths, and rewrites configured-feature JSON through the 26.2 registry provider.
- [x] Re-port cooking pot recipe book support:
  - [x] Replace the placeholder `RecipeCategories` implementation with the 26.2 recipe book registry model.
  - [x] Replace the placeholder `CookingPotRecipeBookComponent`.
  - [x] Stop falling back to `RecipeBookType.FURNACE` and `RecipeBookCategories.FURNACE_FOOD` where Farmer's Delight needs its own cooking pot categories.
  - [x] Verify `META-INF/enumextensions.json` is still the right way to register `FARMERSDELIGHT_COOKING`, or replace it with the 26.2-native registry path.
  - Cooking pot recipes now publish recipe displays, route into registered `farmersdelight:cooking_*` recipe-book categories, use the `FARMERSDELIGHT_COOKING` recipe book type, and register a NeoForge search category for the client recipe book.
- [x] Re-port HUD overlays for Nourishment and Comfort in `HUDOverlays`.
- [x] Re-port the skillet client item behavior in `ClientSetupEvents` and `SkilletItemRenderer`.
- [x] Re-port canvas rug block-breaking texture suppression in `HideBlockBreakProgressMixin` and re-add it to `farmersdelight.mixins.json` only after it targets the 26.2 renderer pipeline correctly.
- [x] Revisit mixins removed from `farmersdelight.mixins.json`:
  - [x] Decide whether `datafix.V3818_3Mixin` is obsolete or needs a 26.2 equivalent.
  - [x] Decide whether `KeepRichSoilGiantTreeMixin` is obsolete or needs a 26.2 equivalent.
- [x] Implement block/entity item transfer support for 26.2:
  - [x] Basket capability/transfer registration.
  - [x] Cabinet capability/transfer registration.
  - [x] Cooking Pot sided input/output transfer registration.
  - [x] Cutting Board single-slot transfer registration.
  - [x] Skillet and stove inventory transfer behavior audit.
  - [x] Update utility APIs that still expose `IItemHandler` if NeoForge transfer is now the supported integration surface.
- [x] Replace the old villager-trade config toggles with a data-driven 26.2-compatible mechanism, or intentionally remove the toggles and document the behavior change.
  - Farmer's Delight villager and wandering trader offers now live in `data/farmersdelight/villager_trade`, and vanilla trade tags are extended under `data/minecraft/tags/villager_trade`. The old runtime config toggles were removed because 26.2 villager trades are datapack-driven.
- [ ] Re-enable and port optional integrations:
  - [x] JEI recipe population; built-in Farmer's Delight cooking/cutting recipes are decoded from packaged recipe resources for JEI registration, with the dough-from-water synthetic recipe restored.
  - [ ] EMI; official `dev.emi:emi-neoforge` metadata still targets 1.21.1-era Minecraft versions.
  - [x] AppleSkin runtime/dev setup; compile API uses `squeek.appleskin:appleskin-neoforge:mc26.2-3.0.10`, and dev runtime can be enabled with `-Pfd.includeAppleSkinRuntime=true`.
  - [ ] CraftTweaker recipe managers, docs, and scripts; `CraftTweaker-neoforge-26.2` is not published on BlameJared Maven as of 2026-07-27.
  - Checked Maven metadata on 2026-07-27: official EMI still has no 26.2 artifact, `CraftTweaker-neoforge-26.2` returns 404, and the latest available CraftTweaker NeoForge artifact is still `CraftTweaker-neoforge-1.21.1:21.0.38`.
- [x] Update optional dependency handling in `build.gradle`; JEI and AppleSkin use 26.2 artifacts by default, while old EMI/CraftTweaker coordinates are gated behind `-Pfd.includeUnavailableIntegrations=true` until 26.2 APIs exist.
- [x] Update `neoforge.mods.toml` optional dependency ranges; the stale CraftTweaker optional dependency block is removed until a 26.2-compatible CraftTweaker artifact exists.

## Functional QA

- [x] Launch a 26.2 client with only Farmer's Delight installed.
- [x] Launch a 26.2 dedicated server with only Farmer's Delight installed.
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

- [x] Remove or resolve all source/build `TODO 26.2` placeholders.
- [ ] Remove temporary build exclusions once EMI/CraftTweaker publish 26.2-compatible APIs; commented-out runtime dependency placeholders have been removed.
- [x] Confirm `./gradlew.bat clean build` succeeds.
- [x] Confirm `./gradlew.bat runClient` reaches client resource startup with only Farmer's Delight installed and without Farmer's Delight item-model parse errors or missing-texture warnings.
- [ ] Confirm `./gradlew.bat runClientData` and `./gradlew.bat runServerData` both succeed from a clean checkout.
- [x] Confirm the generated resource diff is intentional and reproducible.
  - Re-ran `runServerData` followed by `runClientData` on 2026-07-27 and confirmed the generated-resource diff name list stayed stable.
- [x] Update README, changelog, supported Minecraft/NeoForge version ranges, and release metadata.
- [ ] Build the release jar and smoke-test it outside the dev workspace.
  - Built and installed `FarmersDelight-26.2-1.3.2.jar` into the CurseForge `Test 26.2` instance after the recipe-sync fix; pending manual world-load retest in that instance.
