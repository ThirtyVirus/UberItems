package thirtyvirus.uber.helpers;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class StoredInt implements PersistentDataType<byte[], Integer> {

    private Charset charset = Charset.defaultCharset();

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<Integer> getComplexType() {
        return Integer.class;
    }

    @Override
    public byte[] toPrimitive(Integer i, PersistentDataAdapterContext itemTagAdapterContext) {
        return ByteBuffer.allocate(4).putInt(i).array();
    }

    @Override
    public Integer fromPrimitive(byte[] bytes, PersistentDataAdapterContext itemTagAdapterContext) {
        ByteBuffer wrapped = ByteBuffer.wrap(bytes);
        return wrapped.getInt();
    }
}
