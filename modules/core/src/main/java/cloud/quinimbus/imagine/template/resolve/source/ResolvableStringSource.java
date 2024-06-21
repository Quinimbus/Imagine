package cloud.quinimbus.imagine.template.resolve.source;

public class ResolvableStringSource implements ResolvableSource<String> {

    private final String str;

    public ResolvableStringSource(String str) {
        this.str = str;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isTextual() {
        return true;
    }

    @Override
    public Number getNumber() {
        throw new IllegalStateException("You cannot call getNumber() on a string source");
    }

    @Override
    public String getText() {
        return this.str;
    }
}
