package cloud.quinimbus.imagine.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.function.Function;

public interface ImageCreator {

    void createImage(
            Map<String, Object> parameter,
            Map<String, Function<String, String>> functions,
            OutputStream os,
            Function<String, InputStream> resourceLoader)
            throws IOException, BinaryResolutionException;
}
