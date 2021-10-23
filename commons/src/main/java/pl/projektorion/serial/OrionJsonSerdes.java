package pl.projektorion.serial;

import pl.projektorion.serializer.JsonSerdes;

import java.util.Arrays;

public class OrionJsonSerdes<Msg> extends JsonSerdes<Msg> {
    public OrionJsonSerdes(Class<Msg> clazz) {
        super(clazz);
    }

    @Override
    public byte[] serialize(Msg message) throws Exception {
        final byte[] serializedMessage = super.serialize(message);
        byte[] withEndline = Arrays.copyOf(serializedMessage, serializedMessage.length + 2);
        withEndline[withEndline.length-1] = (byte) '\n'; // \r\n
        withEndline[withEndline.length-2] = (byte) '\r';
        return withEndline;
    }
}
