# Instant Actions

**Adds toggles to reduce or simplify repetitive gameplay mechanics, making common interactions quicker and less repetitive.**

Available on [Modrinth](https://modrinth.com/mod/instant-actions) and [CurseForge](https://www.curseforge.com/minecraft/mc-mods/instant-actions).

## 📖 Features

### Farming

* **Double tilling:** Tilling a block also tills any adjacent blocks.
* **Double seeding:** Planting a crop also plants the same crop on any adjacent empty plantable blocks.
* **Double bone meal:** Bonemealing a crop also applies bone meal to adjacent crops of the same type.
* **Instant crop maturity:** Grow a crop to full maturity, consuming however many bone meal are required.
* **Chain harvest:** Breaking a fully-grown crop harvests all connected crops of the same type, within a 3-block radius.
* **Instant composting:** Instantly fill a composter, consuming as many of the item as needed.

### Animals

* **Instant taming:** Guarantee taming on the first try.
* **Instant animal maturing:** Feeding a baby animal instantly matures it to an adult.

### Food

* **Restore hunger instantly:** Consuming food uses as many of the same item as needed to fully restore hunger.

## 📋 Usage

All toggles are **off by default**, so the mod does nothing until you enable the features you want.

1. Open the config screen via **Mod Menu**, or by running `/instant-actions config` in chat.
2. Enable whatever toggles you want.
3. Save to apply changes

## 💡 Examples

[![crops](https://raw.githubusercontent.com/victorfaurschou/instant-actions/26.1/.github/examples/crops.gif)](https://raw.githubusercontent.com/victorfaurschou/instant-actions/26.1/.github/examples/crops.gif)

[![harvesting](https://raw.githubusercontent.com/victorfaurschou/instant-actions/26.1/.github/examples/harvesting.gif)](https://raw.githubusercontent.com/victorfaurschou/instant-actions/26.1/.github/examples/harvesting.gif)

[![composter](https://raw.githubusercontent.com/victorfaurschou/instant-actions/26.1/.github/examples/composter.gif)](https://raw.githubusercontent.com/victorfaurschou/instant-actions/26.1/.github/examples/composter.gif)

[![hunger](https://raw.githubusercontent.com/victorfaurschou/instant-actions/26.2/.github/examples/hunger.gif)](https://raw.githubusercontent.com/victorfaurschou/instant-actions/26.2/.github/examples/hunger.gif)

[![double seeding](https://raw.githubusercontent.com/victorfaurschou/instant-actions/26.2/.github/examples/double-seeding.gif)](https://raw.githubusercontent.com/victorfaurschou/instant-actions/26.2/.github/examples/double-seeding.gif)

[![double tilling](https://raw.githubusercontent.com/victorfaurschou/instant-actions/26.2/.github/examples/double-tilling.gif)](https://raw.githubusercontent.com/victorfaurschou/instant-actions/26.2/.github/examples/double-tilling.gif)

and more...

## 📌 Notes

This is a **client-side mod**. It works in singleplayer and with the integrated (Open to LAN) server.

Each player controls their own toggles independently. If you host a world and enable a toggle, it applies only to you. Another player who joins with the mod installed uses their own toggle settings. Another player who joins without the mod is unaffected.

Joining a world where the host doesn't have the mod, but you do, will make it have no effect and a warning will be shown.

This mod does not work on dedicated servers.

## 🔗 Dependencies

### Required

- [Fabric Loader](https://fabricmc.net/use/)
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Cloth Config](https://modrinth.com/mod/cloth-config)

### Optional

- [Mod Menu](https://modrinth.com/mod/modmenu)

## 🏷️ Tags

`minecraft, mod, fabric, quality of life, qol, instant action, quick action, quick use, instant use, taming, hunger, consume, bone meal, farming, crops, tilling, seeding, harvest, compost, breeding`