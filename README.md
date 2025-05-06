# 📦 BSKits

## 🗓️ Created On
`06/05/2025`

## 🎯 Purpose
This plugin was developed as part of a **developer recruitment test** for **Magnesify**.  
Its goal is to provide a simple yet functional **kit creation, saving, and usage system** within the Minecraft environment.

## ⚙️ Target Server Platform
- [X] Spigot  
- [X] Paper  
- [X] Leaf  

## 🔢 Target Minecraft Version
`1.20.4`

## 🧰 Technologies & Dependencies Used

| Technology        | Description                                                |
|-------------------|------------------------------------------------------------|
| **Paper API**     | The plugin uses Paper as the server platform for Minecraft. It provides additional performance and feature enhancements over Spigot. |
| **ORMLite**       | Used for object-relational mapping (ORM) to handle SQLite database interactions. |
| **SQLite JDBC**   | SQLite database used for persistent kit data storage. |
| **Lombok**        | Simplifies Java code by generating boilerplate code like getters, setters, and constructors at compile-time. |

## 🧬 Commands

Here are the available commands in the plugin:

### `/kit add <kit_name>`
- **Purpose**: Creates a new kit with the items from the player's inventory and armor.
- **Permissions**: Admins (OPs) only
- **Usage**: `/kit add archer`
  - Creates a kit named "archer" with the items from the player's inventory and armor.

### `/kit remove <kit_name>`
- **Purpose**: Removes an existing kit by its name.
- **Permissions**: Admins (OPs) only
- **Usage**: `/kit remove archer`
  - Removes the kit named "archer".

### `/kit update <old_kit_name> <new_kit_name>`
- **Purpose**: Updates the name of an existing kit.
- **Permissions**: Admins (OPs) only
- **Usage**: `/kit update archer new_archer`
  - Updates the "archer" kit to "new_archer".

### `/kit list`
- **Purpose**: Lists all available kits on the server.
- **Permissions**: Admins (OPs) only
- **Usage**: `/kit list`
  - Displays a list of all kits available.

### `/kit give <kit_name>`
- **Purpose**: Gives a kit to the player.
- **Permissions**: Admins (OPs) only
- **Usage**: `/kit give archer`
  - Gives the "archer" kit to the player.

