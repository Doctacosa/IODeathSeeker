# IODeathSeeker

![Logo](https://www.interordi.com/images/plugins/iodeathseeker-96.png)

Death Seeker minigame in Minecraft, as featured on the [Creeper's Lab](https://www.creeperslab.net/).

The goal of the game is simple, yet can be challenging: the players must collect as many unique death messages as possible! Each unique death provides one point. The death messages used are those in English, as provided by the game server.

Two play modes are available: one that ignores mob names and one that includes them. The former forces players to focus on seeking out purely unique messages, while the latter lets them score more points by finding different mobs.

This is a sample of the messages logged when counting individual mob names, along with the amount of times that each one was found:

```
was blown up by creeper: 4
died: 1
fell from a high place: 3
was slain by enderman: 1
hit the ground too hard: 1
was slain by wolf: 1
```

If the `ignore-mobs` flag was set to true, this would have been saved instead. Note how both "slain by" messages were counted as a single source of death:

```
was blown up by: 4
died: 1
fell from a high place: 3
was slain by: 2
hit the ground too hard: 1
```

This event is designed to run for multiples consecutive days. Using a few weeks, up to full month before a reset, is the suggested duration.


## How to play

To understand how this is played from a player's perspective, [see this guide on our wiki](https://wiki.creeperslab.net/worlds/kenorland/death-seeker). Note that Merit Points are exclusive to the Creeper's Lab, you're free to implement your own rewards as none are built-in. The list of deaths obtained by each player are saved in the file `IODeathSeeker/stats.yml`.


## Setup guide

1. Download the plugin and place it in the `plugins/` directory of the server.
2. Start and stop the server to create the configuration files.
3. Edit `plugins/IOGrindatron/config.yml` to set your settings, described below.


## Configuration

`ignore-mobs`: true/false, if the individual mob names should be ignored in tracking  


## Commands

None


## Permissions

None
