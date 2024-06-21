package cloud.quinimbus.imagine.api;

import java.util.ServiceLoader;

public interface Colors {

    static final Colors instance = ServiceLoader.load(Colors.class)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                    "Cannot find the quinimbus core library, is it on the classpath / modulepath?"));

    static Colors get() {
        return instance;
    }

    RGBAColor decodeRGBA(String src);
}
