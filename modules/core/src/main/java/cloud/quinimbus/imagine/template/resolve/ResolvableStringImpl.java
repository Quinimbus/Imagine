package cloud.quinimbus.imagine.template.resolve;

import cloud.quinimbus.imagine.api.ResolvableString;
import cloud.quinimbus.imagine.template.resolve.source.ResolvableSource;
import com.fasterxml.jackson.databind.JsonNode;

public final class ResolvableStringImpl extends Resolvable<String> implements ResolvableString {

    public ResolvableStringImpl(JsonNode source) {
        super(source);
    }

    public ResolvableStringImpl(String source) {
        super(source);
    }

    @Override
    protected String convert(ResolvableSource source) {
        if (source.isTextual()) {
            return source.getText();
        }
        throw new IllegalArgumentException("Cannot read source %s as text".formatted(source));
    }

    @Override
    protected String convert(Object source) {
        return source.toString();
    }
}
