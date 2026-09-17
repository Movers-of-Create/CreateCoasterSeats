# Create Coaster Seats

Create Coaster Seats is a NeoForge addon for [Create](https://github.com/Creators-of-Create/Create) that adds lockable seats controlled with Create Redstone Link frequencies.

## Features

- Securable coaster seats in all 16 Minecraft dye colors
- Two frequency slots on the bottom of every seat
- Wireless locking and unlocking through Create's Redstone Link system
- Animated restraint that closes when the configured signal is active
- Prevents players from entering or leaving while the seat is locked
- Seats can be recolored with dyes
- English and German translations

## Usage

1. Place a Securable Seat on a Create contraption.
2. Look at the two frequency slots on the bottom of the seat.
3. Use items on the slots to configure the desired Redstone Link frequency.
4. Transmit a signal on the matching frequency to close and lock the restraint.
5. Turn off the signal to unlock the seat again.

Hold **Shift** while hovering over a Securable Seat in the inventory to display its tooltip description.

## Requirements

| Dependency | Version |
| --- | --- |
| Minecraft | 1.21.1 |
| NeoForge | 21.1 or newer |
| Create | 6.0.10 or newer |

## Installation

1. Install NeoForge for Minecraft 1.21.1.
2. Install Create with its required dependencies.
3. Download Create Coaster Seats and place the JAR in the Minecraft `mods` directory.

Make sure all installed versions target Minecraft 1.21.1.

## Building from Source

Java 21 is required.

On Windows:

```powershell
.\gradlew.bat build
```

On Linux or macOS:

```bash
./gradlew build
```

The compiled JAR will be created in `build/libs`.

For a development client, run:

```powershell
.\gradlew.bat runClient
```

## License

Create Coaster Seats is licensed under the [GNU General Public License v3.0](https://github.com/Movers-of-Create/CreateCoasterSeats?tab=GPL-3.0-1-ov-file).

Create is a separate project owned by its respective authors.
