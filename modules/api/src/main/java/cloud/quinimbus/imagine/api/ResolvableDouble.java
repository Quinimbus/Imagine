package cloud.quinimbus.imagine.api;

public interface ResolvableDouble {

    Double get(Resolver resolver);

    boolean isEmpty(Resolver resolver);
}
