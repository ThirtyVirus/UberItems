package thirtyvirus.uber.helpers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemStorageUtilities {

    // save list of items into the lore of a given item, starting at startLoreIndex
    public static void saveItemsInLore(ItemStack item, ItemStack[] items, int startLoreIndex) {
        String dataString = itemsToString(items);

        ArrayList<String> loreChunks = new ArrayList<String>();

        for (int index = 0; index < startLoreIndex; index++) {
            loreChunks.add(item.getItemMeta().getLore().get(index));
        }

        while (dataString.length() > 0) {
            String chunk = "";

            if (dataString.length() >= 510) {
                chunk = dataString.substring(0, 510);
                dataString = dataString.substring(510);
            }
            else{
                chunk = dataString;
                dataString = "";
            }

            loreChunks.add(convertToInvisibleString(chunk));

        }
        Utilities.loreItem(item, loreChunks);
    }

    // load list of items from the lore of a given item, starting at startLoreIndex
    public static ItemStack[] getItemsFromLore(ItemStack item, int startLoreIndex) {

        String itemString = "";
        while (startLoreIndex < item.getItemMeta().getLore().size()) {
            itemString = itemString + item.getItemMeta().getLore().get(startLoreIndex);
            startLoreIndex++;
        }

        if (!itemString.equals("")){
            return stringToItems(convertToVisibleString(itemString));
        }
        else {
            return null;
        }
    }

    // convert list of items into string
    private static String itemsToString(ItemStack[] items) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(serializeItemStack(items));
            oos.flush();
            return DatatypeConverter.printBase64Binary(bos.toByteArray());
        }
        catch (Exception e) {
            //Logger.exception(e);
        }
        return "";
    }

    // convert string to list of items
    @SuppressWarnings("unchecked")
    private static ItemStack[] stringToItems(String s) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(
                    DatatypeConverter.parseBase64Binary(s));
            ObjectInputStream ois = new ObjectInputStream(bis);
            return deserializeItemStack(
                    (Map<String, Object>[]) ois.readObject());
        }
        catch (Exception e) {
            //Logger.exception(e);
        }
        return new ItemStack[] {
                new ItemStack(Material.AIR) };
    }

    // serialize list of items
    @SuppressWarnings("unchecked")
    private static Map<String, Object>[] serializeItemStack(ItemStack[] items) {

        Map<String, Object>[] result = new Map[items.length];

        for (int i = 0; i < items.length; i++) {
            ItemStack is = items[i];
            if (is == null) {
                result[i] = new HashMap<>();
            }
            else {
                result[i] = is.serialize();
                if (is.hasItemMeta()) {
                    result[i].put("meta", is.getItemMeta().serialize());
                }
            }
        }

        return result;
    }

    // deserialize list of items
    @SuppressWarnings("unchecked")
    private static ItemStack[] deserializeItemStack(Map<String, Object>[] map) {
        ItemStack[] items = new ItemStack[map.length];

        for (int i = 0; i < items.length; i++) {
            Map<String, Object> s = map[i];
            if (s.size() == 0) {
                items[i] = null;
            }
            else {
                try {
                    if (s.containsKey("meta")) {
                        Map<String, Object> im = new HashMap<>(
                                (Map<String, Object>) s.remove("meta"));
                        im.put("==", "ItemMeta");
                        ItemStack is = ItemStack.deserialize(s);
                        is.setItemMeta((ItemMeta) ConfigurationSerialization
                                .deserializeObject(im));
                        items[i] = is;
                    }
                    else {
                        items[i] = ItemStack.deserialize(s);
                    }
                }
                catch (Exception e) {
                    //Logger.exception(e);
                    items[i] = null;
                }
            }

        }

        return items;
    }

    // makes string invisible to player
    private static String convertToInvisibleString(String s) {
        String hidden = "";
        for (char c : s.toCharArray()) hidden += ChatColor.COLOR_CHAR+""+c;
        return hidden;
    }

    // makes invisible string visible to player
    private static String convertToVisibleString(String s){
        String c = "";
        c = c + ChatColor.COLOR_CHAR;
        return s.replaceAll(c, "");
    }
}
