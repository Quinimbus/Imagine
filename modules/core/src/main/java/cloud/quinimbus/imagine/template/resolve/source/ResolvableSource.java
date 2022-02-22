package cloud.quinimbus.imagine.template.resolve.source;

public interface ResolvableSource<T> {
    
    boolean isNumber();
    boolean isTextual();
    Number getNumber();
    String getText();
}
