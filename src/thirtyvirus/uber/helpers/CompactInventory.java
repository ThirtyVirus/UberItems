package thirtyvirus.uber.helpers;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class CompactInventory implements PersistentDataType<byte[], ItemStack[]> {

    private Charset charset = Charset.defaultCharset();

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<ItemStack[]> getComplexType() {
        return ItemStack[].class;
    }

    @Override
    public byte[] toPrimitive(ItemStack[] items, PersistentDataAdapterContext itemTagAdapterContext) {
        return itemsToString(items).getBytes(charset);
    }

    @Override
    public ItemStack[] fromPrimitive(byte[] bytes, PersistentDataAdapterContext itemTagAdapterContext) {
        ItemStack[] items = stringToItems(new String(bytes, charset));
        return items;
    }

    // convert list of items into string
    private String itemsToString(ItemStack[] items) {
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
    private ItemStack[] stringToItems(String s) {
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
    private Map<String, Object>[] serializeItemStack(ItemStack[] items) {

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
    private ItemStack[] deserializeItemStack(Map<String, Object>[] map) {
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

}