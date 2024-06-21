package cloud.quinimbus.imagine.api;

public interface ResolvableString {

    String get(Resolver resolver);

    boolean isEmpty(Resolver resolver);
}
