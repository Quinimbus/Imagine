package cloud.quinimbus.imagine.template.resolve;

import cloud.quinimbus.imagine.api.ResolvableDouble;
import cloud.quinimbus.imagine.api.ResolvableInteger;
import cloud.quinimbus.imagine.api.ResolvableString;
import cloud.quinimbus.imagine.api.Resolvables;

public class ResolvablesImpl implements Resolvables {

    @Override
    public ResolvableInteger integer(int value) {
        return new ResolvableIntegerImpl(value);
    }

    @Override
    public ResolvableInteger integer(String value) {
        return new ResolvableIntegerImpl(value);
    }

    @Override
    public ResolvableString string(String value) {
        return new ResolvableStringImpl(value);
    }

    @Override
    public ResolvableDouble asDouble(double value) {
        return new ResolvableDoubleImpl(value);
    }

    @Override
    public ResolvableDouble asDouble(String value) {
        return new ResolvableDoubleImpl(value);
    }
    
}
