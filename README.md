# 💧 Water Dehydration 

**A survival realism plugin** for Minecraft Paper servers that adds dynamic hydration mechanics with dehydration effects and water poisoning system.

---

## 🌊 Features
- **Hydration System**: Water level decreases over time during gameplay
- **Poisoning Mechanics**: Drinking contaminated water causes:
  - Hunger effects
  - Poison effects
  - Accelerated dehydration
- **Disinfected Water** support via custom NBT tags
- **Visual Actionbar** with customizable water level display
- **Auto-Save** of player hydration between sessions
- **Multi-World Support** with permissions:
  - `wd.no-water` - complete immunity
  - `wd.reload` - config reload access
- **Full Configuration**:
  - Adjustable dehydration rates
  - Effect amplifiers/duration
  - Poison mechanics tuning
  - Custom messages/designs
 
## ⚙️ Configuration (`config.yml`)
### 🔹 Default Configuration
```yaml
max-water: 10
how-much-to-reduce-the-water-level: 1
how-much-time-to-reduce-the-water-level: 60 # need reload the server (players should re-join). in seconds

dehydrate:
  hunger-effect-amplifier: 1 # 0 - 1, 1 - 2...
  poison-effect-amplifier: 1

water-poisoning:
  hunger-effect-amplifier: 1
  poison-effect-amplifier: 0 # 0 - 1, 1 - 2...
  poison-effect-duration: 10 # in seconds
  hunger-effect-duration: 10
  water-poisoning-time: 60 # in seconds. 20 second - -1 water level in the default configuration
  how-much-infected-water-increase-water-level: 1 # 1 - 0
  water-poisoning-reduce-water-in-time: 1 # in default configuration, 20 seconds - -1 water level. 60 seconds - -3 water level.
  water-poisoning-reduce-water-time: 20 # in seconds. 20 seconds - -1 water level. 60 seconds - -3 water level.

message:
  water-actionbar:
    wa1: "&#345D90["
    emoji-water: "&#6A93FB♦"
    emoji-dehydration-water: "&#FFFFFF♦"
    wa2: "&#345D90]"
    dehydrate: "&#92C3FFYou are dehydrated!"
    water-poisoning: "&#99FF92♦"

  no-permission: "&#FF0000You don't have permission to use this command!"
  reload: "&#FFD700Configuration reloaded!"

disinfected-water-name: "&#A7D1F1Disinfected Water"
disinfected-custom-model-data: 500 # REQUIRED
how-much-disinfected-water-increase-water-level: 1

boiling-water:
  # Campfire and furnace
  campfire-cooking-time: 10 # in seconds
  furnace-cooking-time: 10 # in seconds
```

# 💧 Water Dehydration - Complete Configuration Guide

## 📋 Core Parameters

### `max-water: 10`
- **Description**: Maximum hydration level a player can have  
- **Values**: Any integer > 0  
- **Example**: `15` = player can store up to 15 "water drops"

### `how-much-to-reduce-the-water-level: 1`
- **Description**: Water units removed per interval  
- **Formula**: `X water removed every N seconds` (N = `how-much-time...` value)

### `how-much-time-to-reduce-the-water-level: 60`
- **Description**: Interval between water reduction (seconds)  
- **Note**: Requires server reload/player rejoin to apply changes

---

## ⚠️ Dehydration Effects

### `dehydrate:` Section
```yaml
hunger-effect-amplifier: 1   # Hunger effect level (0 = I, 1 = II)
poison-effect-amplifier: 1    # Slowness effect level
```

## ☠️ Water Poisoning Mechanics

### `water-poisoning:` Section
```yaml
hunger-effect-amplifier: 1          # Hunger effect strength
poison-effect-amplifier: 0          # Poison effect level
poison-effect-duration: 10          # Poison duration (seconds)
hunger-effect-duration: 10          # Hunger duration (seconds)
water-poisoning-time: 60            # Total poisoning duration
how-much-infected-water-increase-water-level: 1 # Water gained from dirty water
water-poisoning-reduce-water-in-time: 1  # Water lost per interval
water-poisoning-reduce-water-time: 20    # Water loss interval (seconds)
```

**Mechanics:**

- Drinking contaminated water:
- Adds +1 water (how-much-infected...)
- Applies effects for 10 seconds
- Removes 1 water every 20 seconds (water-poisoning-reduce-water-time)

## 🎨 Interface Customization

### `message.water-actionbar:` Section

```yaml
wa1: "&#345D90["           # Prefix HEX color
emoji-water: "&#6A93FB♦"   # Full water droplet symbol
emoji-dehydration-water: "&#FFFFFF♦" # Empty droplet symbol
wa2: "&#345D90]"           # Suffix HEX color
dehydrate: "&#92C3FFYou are dehydrated!" # Dehydration warning
water-poisoning: "&#99FF92♦" # Poisoned water color
```

## 🧪 Disinfected Water

`disinfected-custom-model-data: 500`
**Description:** Unique CustomModelData ID for disinfected water
**!! Critical:** Must match item's actual CustomModelData

`how-much-disinfected-water-increase-water-level: 1`
**Description:** Hydration restored by disinfected water

## 🔥 Water Boiling System

### `boiling-water:` Section

`campfire-cooking-time: 10`  # Campfire cooking duration (seconds)
`furnace-cooking-time: 10`   # Furnace smelting duration (seconds)

**Mechanics:**
-Regular water + campfire/furnace → Disinfected water

## 🔄 Configuration Reload
Use `/wd reload` to apply:
- Message changes
- Interface colors
- Boiling parameters
*Does NOT update:*
- Water reduction intervals (requires reboot)
- Active poison effects

## 📖 Commands & Permissions

### 🔸 Commands

| Command | Permission | Description |
|----------------|:---------:|----------------:|
| /wd reload | wd.reload | Reload the config. |

### 🔸 Permissions

| Permission | Description |
|----------------|:---------:|
| wd.no-water | Bypass all mechanics |

## 🗂️ Versions

| Plugin Version | Version | Download |
|----------------|:---------:|----------------:|
| 1.1 | 1.21.4 | [Download v1.1](https://github.com/OluniOfficial/WaterDehydration/releases/download/v1.1/WaterDehydration-1.1.jar) |

## ❗ Bug Reports & Support

Found an issue? Want to suggest improvement?

- 🐛 Create GitHub Issue
- 📮 Discord: oluni_official
