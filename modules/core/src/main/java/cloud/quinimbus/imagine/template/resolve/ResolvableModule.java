package cloud.quinimbus.imagine.template.resolve;

import cloud.quinimbus.imagine.api.ResolvableInteger;
import cloud.quinimbus.imagine.api.ResolvableString;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.util.function.Function;

public class ResolvableModule extends SimpleModule {
    
    private class SimpleJsonNodeSerializer<T> extends JsonDeserializer<T> {
        
        private final Function<JsonNode, T> converter;

        public SimpleJsonNodeSerializer(Function<JsonNode, T> converter) {
            this.converter = converter;
        }

        @Override
        public T deserialize(JsonParser p, DeserializationContext c) throws IOException, JsonProcessingException {
            return this.converter.apply(c.readTree(p));
        }
    }

    public ResolvableModule() {
        this.addDeserializer(ResolvableString.class, new SimpleJsonNodeSerializer<>(ResolvableStringImpl::new));
        this.addDeserializer(ResolvableInteger.class, new SimpleJsonNodeSerializer<>(ResolvableIntegerImpl::new));
    }
}
