# Simple Burnout Torch
[![Modrinth](https://badges.moddingx.org/modrinth/downloads/MqJnLmSS)](https://modrinth.com/mod/simple-burnout-torch)
[![CurseForge](https://badges.moddingx.org/curseforge/downloads/1353923)](https://www.curseforge.com/minecraft/mc-mods/simple-burnout-torch)

**Simple Burnout Torch** makes torches feel alive by adding configurable burnout and relighting.

---

## Features

* **Configurable burnout** — Torches can expire after a set time (ticks), with optional random variance.
* **Relight support** — Re-ignite a spent torch using allowed relighter items (defined by tags/config).
* **Wall & standing torches** — Placement logic supports both variants consistently.
* **Server-friendly** — All behavior driven by config/tags for pack makers and servers.
---

## Installation

1. Install **NeoForge** for Minecraft **1.21.1**.
2. Drop the mod JAR into your `mods/` folder.
3. Launch the game once to generate the config.

---

## Configuration

After first launch, edit:

```
config/simple_burnout_torch.toml
```

Common options you’ll find (names may differ slightly depending on your current version):

```toml
#A list of dimensions where Burnout Torch is forced. Example: dimension_expansion:deep_beneath, theupsidedown:upside_down
dimensions = ["dimension_expansion:deep_beneath", "theupsidedown:upside_down"]
#Per-tick chance that a Burnable Torch will extinguish. Range: 0.0 (never) to 1.0 (always). Default: 0.1 (10%).
# Default: 0.1
# Range: 0.0 ~ 1.0
extinguishChance = 0.1
#Light level for Burnable Torch (1–15). Default: 8
# Default: 8
# Range: 1 ~ 15
torchLightLevel = 8
#Light level for Extinguished Torch (0–15). Default: 0
# Default: 0
# Range: 0 ~ 15
extinguishedTorchLightLevel = 0
#Block IDs of light sources that can be placed alongside Burnable Torch.
allowedBlocks = ["minecraft:torch", "minecraft:wall_torch", "minecraft:soul_torch", "minecraft:soul_wall_torch"]
#Block IDs that are explicitly denied (cannot be placed as light sources).
deniedBlocks = ["minecraft:lantern", "minecraft:soul_lantern", "minecraft:sea_lantern", "minecraft:glowstone_block"]

```

> Tip: For servers, ship a tuned `defaultconfigs/simple_burnout_torch.toml` in your modpack.

---

## Datapack & Tag Integration (Optional)

You can control which items **count as relighters** (e.g., flint and steel, custom lighters) via item tags in a datapack:

```
data/<your_namespace>/tags/items/relighters.json
```

```json
{
  "replace": false,
  "values": [
    "minecraft:flint_and_steel"
    // add other items or your custom lighter here
  ]
}
```

Pack authors can also define custom behaviors or whitelists/blacklists using tags the mod exposes (if present in your version). Check your logs on first run for generated tag files, or browse `data/<modid>/tags/` inside the JAR for available hooks.

---

## Gameplay Notes

* Placing a torch sets its **lit** state according to config.
* A torch **burns down** over time and then becomes **unlit** (spent).
* Use an **approved relighter item** to bring it back.

This is designed to be **modpack-friendly**: you decide whether this adds light survival spice or becomes a core progression mechanic.

---

## Compatibility

* **Minecraft:** 1.21.1
* **Loader:** NeoForge
* **Worlds:** Works on existing saves.
* **Other light sources:** Non-torch light blocks remain unaffected (unless your pack scripts change tags/behavior intentionally).

If you hit a conflict, open an issue with logs + a minimal reproduction.

---

## Changelog

See the [Releases](./releases) page or your distribution platform (CurseForge/Modrinth) for version-specific notes.
When contributing, follow **Keep a Changelog** style with clear bullets for Added/Changed/Fixed/Removed.

---

## Reporting Issues

Please include:

* Game + loader versions (MC 1.21.1, NeoForge build)
* Mod version (e.g., `1.0.x`)
* Full `latest.log` (gist/paste)
* Steps to reproduce
* Pack/mod list if relevant

---

## License

This mod is licensed under an **MIT License**.

### 📅 Attribution Notice

Torch textures used in this mod are adapted from [Realistic Torches by MattCzyr](https://github.com/MattCzyr/RealisticTorches),
licensed under the [Attribution-NonCommercial-ShareAlike 4.0 International License](https://creativecommons.org/licenses/by-nc-sa/4.0/).

Credit to MattCzyr for the original texture work that inspired the visual design of **Simple Burnout Torch**.

---

## Credits

* **DarkLotus / LithiumCraft** — concept, code, & maintenance
* **MattCzyr** — original torch textures (Realistic Torches)
* Community testers & pack authors providing feedback and balancing ideas

---

## Links

* **Issues: https://github.com/darklotus781/Simple-Burnout-Torch/issues**

---

If this mod improved your pack, a star on the repo helps a ton!
