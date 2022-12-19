package cloud.quinimbus.imagine.template.resolve;

import cloud.quinimbus.imagine.api.ResolvableDouble;
import cloud.quinimbus.imagine.template.resolve.source.ResolvableSource;
import com.fasterxml.jackson.databind.JsonNode;

public final class ResolvableDoubleImpl extends Resolvable<Double> implements ResolvableDouble {
    
    public ResolvableDoubleImpl(JsonNode source) {
        super(source);
    }

    public ResolvableDoubleImpl(String source) {
        super(source);
    }

    public ResolvableDoubleImpl(Double source) {
        super(source);
    }
    
    @Override
    protected Double convert(ResolvableSource source) {
        if (source.isNumber()) {
            return source.getNumber().doubleValue();
        } else if (source.isTextual()) {
            if (source.getText().matches("-?\\d*\\.?\\d+")) {
                return Double.parseDouble(source.getText());
            }
        }
        throw new IllegalArgumentException("Cannot read source %s as double".formatted(source));
    }

    @Override
    protected Double convert(Object source) {
        if (source instanceof Double d) {
            return d;
        } else if (source instanceof Number n) {
            return n.doubleValue();
        } else if (source instanceof String s) {
            if (s.matches("-?\\d*\\.?\\d+")) {
                return Double.parseDouble(s);
            }
        }
        throw new IllegalArgumentException("Cannot convert object %s of type %s as double".formatted(source.toString(), source.getClass()));
    }
}
