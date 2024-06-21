package cloud.quinimbus.imagine.api;

import java.util.ServiceLoader;

public interface Resolvables {

    static final Resolvables instance = ServiceLoader.load(Resolvables.class)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                    "Cannot find the quinimbus core library, is it on the classpath / modulepath?"));

    static Resolvables get() {
        return instance;
    }

    ResolvableInteger integer(int value);

    ResolvableInteger integer(String value);

    ResolvableDouble asDouble(double value);

    ResolvableDouble asDouble(String value);

    ResolvableString string(String value);
}
