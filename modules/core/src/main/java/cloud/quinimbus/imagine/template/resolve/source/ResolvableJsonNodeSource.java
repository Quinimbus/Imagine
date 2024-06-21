package cloud.quinimbus.imagine.template.resolve.source;

import com.fasterxml.jackson.databind.JsonNode;

public class ResolvableJsonNodeSource implements ResolvableSource<JsonNode> {

    private final JsonNode node;

    public ResolvableJsonNodeSource(JsonNode node) {
        this.node = node;
    }

    @Override
    public boolean isNumber() {
        return this.node.isNumber();
    }

    @Override
    public boolean isTextual() {
        return this.node.isTextual();
    }

    @Override
    public String getText() {
        return this.node.asText();
    }

    @Override
    public Number getNumber() {
        return this.node.asInt();
    }
}
