package pl.projektorion.serializer;

public interface Serdes<Msg> {
    Msg deserialize(final byte[] data) throws Exception;
    byte[] serialize(final Msg message) throws Exception;
}
