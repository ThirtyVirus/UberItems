package thirtyvirus.uber.helpers;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.nio.charset.Charset;

public class StoredString implements PersistentDataType<byte[], String> {

    private Charset charset = Charset.defaultCharset();

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<String> getComplexType() {
        return String.class;
    }

    @Override
    public byte[] toPrimitive(String string, PersistentDataAdapterContext itemTagAdapterContext) {
        return string.getBytes(charset);
    }

    @Override
    public String fromPrimitive(byte[] bytes, PersistentDataAdapterContext itemTagAdapterContext) {
        return new String(bytes, charset);
    }
}
