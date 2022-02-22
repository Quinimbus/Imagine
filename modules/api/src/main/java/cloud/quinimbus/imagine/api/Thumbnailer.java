package cloud.quinimbus.imagine.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.ServiceLoader;

public interface Thumbnailer {
    
    static final Thumbnailer instance = ServiceLoader.load(Thumbnailer.class).findFirst().orElseThrow(() -> new IllegalStateException("Cannot find the quinimbus core library, is it on the classpath / modulepath?"));

    static Thumbnailer get() {
        return instance;
    }
    
    byte[] createImageThumb(InputStream is) throws IOException, BinaryResolutionException;
    
    byte[] createFontThumb(InputStream is) throws IOException, BinaryResolutionException;
}
