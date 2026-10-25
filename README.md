# MamdaniMod

A Minecraft Forge mod featuring Mayor Mamdani and Big Yahu.

## Mobs

- **Mayor Mamdani** is a neutral mob. He ignores players unless attacked, then retaliates for a limited time. He is faster and stronger than Big Yahu and will attack him.
- **Big Yahu** is a hostile, player-sized mob that attacks nearby players and fights back against Mamdani. At half health, he retreats from Mamdani.

Both mobs have spawn eggs in the MamdaniMod creative tab and the vanilla Spawn Eggs tab. Big Yahu's spawn egg uses an Israeli flag design.

## Customize Big Yahu's texture

Replace `src/main/resources/assets/mamdanimod/textures/entity/big_yahu.png` with your own 64×64 Minecraft humanoid skin. Keep the same file name and path, then rebuild the mod.

## Requirements

- Minecraft 26.2
- Minecraft Forge 65.1.5
- Java 25

## Build

Run the Gradle build:

```powershell
.\gradlew.bat build
```

The built mod JAR is in `build/libs`. Copy it into the `mods` folder of a matching Forge installation.

To launch the development client:

```powershell
.\gradlew.bat runClient
```
