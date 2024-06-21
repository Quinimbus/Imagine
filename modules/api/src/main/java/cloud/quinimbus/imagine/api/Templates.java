package cloud.quinimbus.imagine.api;

import java.util.ServiceLoader;

public interface Templates {

    static final Templates instance = ServiceLoader.load(Templates.class)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                    "Cannot find the quinimbus core library, is it on the classpath / modulepath?"));

    static Templates get() {
        return instance;
    }

    ImageCreator newImageCreator(ImageTemplate template);
}
