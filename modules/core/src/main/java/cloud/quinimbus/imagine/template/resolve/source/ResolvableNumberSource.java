package cloud.quinimbus.imagine.template.resolve.source;

public class ResolvableNumberSource implements ResolvableSource<Number> {

    private final Number number;

    public ResolvableNumberSource(Number number) {
        this.number = number;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public boolean isTextual() {
        return false;
    }

    @Override
    public Number getNumber() {
        return this.number;
    }

    @Override
    public String getText() {
        throw new IllegalStateException("You cannot call getText() on a number source");
    }
}
