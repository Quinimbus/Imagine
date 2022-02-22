package cloud.quinimbus.imagine.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.function.Function;

public interface ImageCreator {
    
    void createImage(Map<String, Object> parameter, OutputStream os, Function<String, InputStream> resourceLoader) throws IOException, BinaryResolutionException;
}
