![Ascension](https://github.com/kenofnz/TowerConquest/raw/master/TowerConquest/resources/sprites/ui/menu/title.png)

# Description
An multiplayer action 2D sidescrolling brawler coded in pure Java.

# Version 0.18 Update 1
## Gameplay Changes
* Passives Removed: Tactical Execution
* Shield Skill Removed: Shield Toss
* New Shield Skill - Magnetize - Pull nearby enemies towards you.
* New Passive - Tough Skin - Boost your natural resilience to damage.
* New Passive - Vigor - Gain additional damage base on Max HP.
* New Passive - Static Charge - Chance to shock a nearby enemy when dealing damage.
* Rapid Fire damage increased from 75 + 2/Lvl% to 80 + 2/Lvl%.
* Resistance Passive reworked - When taking damage over 25% of your HP in 2 seconds, block all damage for 2 seconds.
* Phantom Reaper damage decreased from 110 + 3/Lvl% to 75 + 2/Lvl%
* Vortex Bolts damage increased from 75 + 3/Lvl% to 85% + 3/Lvl%
* Aggression damage increased from 600 + 20/Lvl% to 800 + 20/Lvl%

## Client Changes
* Fixed never timing out when trying to login to a live server with an invalid room.
* New Soundtrack!
* Text changes in Connect menu
* Item drop notifications now also describe the Item Tier.
* Various visual changes.
* Volley Renamed to Vortex Bolts
* New Skill Icons - Power of Will, Flurry
* Improved standards for Equipment sprite file structure.
* Fixed a rare bug where Charge not showing visuals.

## Server Changes
* Added batched packet sending. Disabled by default.
* Fixed some items not dropping.
* Moved getItemType from Player to Item class.
* Moved sendParticle, sendSfx from Player to PacketSender class.
* Fixed Dual Sword not being considered a passive.
* Phantom delay between strikes increased from 0.08s to 0.1s
* Update bow attack animation values.
* Fixed some skills rarely not activating and would go on cooldown.
* Fixed rare bug where player would take damage after respawning if there was a damage source on their death location at the time of respawn.
* Fixed not casting active skills when a passive skill hotkey is being held down.
* More consistent workflow for disconnecting player.
* Only player connection originated action commands are accepted by the server - Player hijacking prevention.

## Credits
### Developer/Game Designer/Visual Designs
[KenOfNZ](https://github.com/kenofnz)

### Character Base
[TradnuxGames](http://tradnux.com/)

### Music
Title & Menu Tracks

* Hero - [geluf](https://soundcloud.com/geluf)
* Through the Forest in Midwinter - [geluf](https://soundcloud.com/geluf)

Arena Tracks

```
Music from the �JRPG Essentials� Series by Dibur
Copyright (c) 2016 Dibur
http://dibur.moe
```

* Redemption (Orchestral Style) - [Dibur](http://dibur.moe)
* Graceful Resistance - [Dibur](http://dibur.moe)
* Dibur - Inevitable Bloodshed (Rock Style) - [Dibur](http://dibur.moe)