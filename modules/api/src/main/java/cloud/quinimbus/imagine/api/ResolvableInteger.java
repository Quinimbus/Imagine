package cloud.quinimbus.imagine.api;

public interface ResolvableInteger {

    Integer get(Resolver resolver);

    boolean isEmpty(Resolver resolver);
}
