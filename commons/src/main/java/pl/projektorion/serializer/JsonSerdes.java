package pl.projektorion.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class JsonSerdes<Msg> implements Serdes<Msg> {
    private static final Logger log = LoggerFactory.getLogger(JsonSerdes.class);

    private final Class<Msg> clazz;
    private final ObjectMapper mapper;

    public JsonSerdes(Class<Msg> clazz) {
        this.clazz = clazz;
        this.mapper = new ObjectMapper();
    }

    @Override
    public Msg deserialize(byte[] data) throws Exception {
//        log.info("Got raw msg to deserialize: {}", new String(data));
        return mapper.readValue(data, clazz);
    }

    @Override
    public byte[] serialize(Msg message) throws Exception {
        return mapper.writeValueAsBytes(message);
    }
}
