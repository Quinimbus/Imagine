package cloud.quinimbus.imagine.template.resolve;

import cloud.quinimbus.imagine.api.ResolvableInteger;
import cloud.quinimbus.imagine.template.resolve.source.ResolvableSource;
import com.fasterxml.jackson.databind.JsonNode;

public final class ResolvableIntegerImpl extends Resolvable<Integer> implements ResolvableInteger {

    public ResolvableIntegerImpl(JsonNode source) {
        super(source);
    }

    public ResolvableIntegerImpl(String source) {
        super(source);
    }

    public ResolvableIntegerImpl(Integer source) {
        super(source);
    }
    
    @Override
    protected Integer convert(ResolvableSource source) {
        if (source.isNumber()) {
            return source.getNumber().intValue();
        } else if (source.isTextual()) {
            if (source.getText().matches("-?\\d+")) {
                return Integer.parseInt(source.getText());
            }
        }
        throw new IllegalArgumentException("Cannot read source %s as int".formatted(source));
    }

    @Override
    protected Integer convert(Object source) {
        if (source instanceof Integer i) {
            return i;
        } else if (source instanceof Number n) {
            return n.intValue();
        } else if (source instanceof String s) {
            if (s.matches("-?\\d+")) {
                return Integer.parseInt(s);
            }
        }
        throw new IllegalArgumentException("Cannot convert object %s of type %s as int".formatted(source.toString(), source.getClass()));
    }
}
