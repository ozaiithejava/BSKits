package org.ozaii.bskits.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Getter
@Setter
@DatabaseTable(tableName = "kit_items")
public class KitItem {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Kit kit;

    @DatabaseField(columnName = "item_serialized", canBeNull = false, width = 4096)
    private String itemSerialized;

    @DatabaseField(canBeNull = false)
    private int amount;

    // Default
    public KitItem() {}


    public KitItem(Kit kit, ItemStack item, int amount) {
        this.kit = kit;
        this.itemSerialized = serializeItem(item);
        this.amount = amount;
    }

    // Deserialize for item
    public ItemStack getItem() {
        return deserializeItem(itemSerialized);
    }

    // Serialize
    private String serializeItem(ItemStack item) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
            dataOutput.writeObject(item);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Item serialize edilemedi", e);
        }
    }

    // Deserialize
    private ItemStack deserializeItem(String base64) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
            return (ItemStack) dataInput.readObject();
        } catch (Exception e) {
            throw new RuntimeException("Item deserialize edilemedi", e);
        }
    }
}
