package cloud.quinimbus.imagine.template;

import cloud.quinimbus.imagine.api.ImageTemplate;
import cloud.quinimbus.imagine.api.BinaryResolutionException;
import cloud.quinimbus.imagine.api.Resolver;
import cloud.quinimbus.tools.throwing.ThrowingMap;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.imageio.ImageIO;

public class CreationContext implements Resolver {
    
    private final Map<String, Object> parameter;
    
    private final Map<String, Function<String, String>> functions;
    
    private final Function<String, InputStream> resourceLoader;
    
    private final ImageTemplate template;
    
    private final ThrowingMap<String, BufferedImage, BinaryResolutionException> loadedImages = ThrowingMap.empty(BinaryResolutionException.class);
    
    private final ThrowingMap<String, Font, BinaryResolutionException> loadedFonts = ThrowingMap.empty(BinaryResolutionException.class);

    public CreationContext(Map<String, Object> parameter, Map<String, Function<String, String>> functions, Function<String, InputStream> resourceLoader, ImageTemplate template) {
        this.parameter = parameter;
        this.functions = functions;
        this.resourceLoader = resourceLoader;
        this.template = template;
    }
    
    public InputStream resolveBinary(String path) throws BinaryResolutionException {
        var res = this.resourceLoader.apply(path);
        if (res == null) {
            throw new BinaryResolutionException("Cannot find a binary resource at path " + path);
        }
        return res;
    }
    
    public BufferedImage resolveImage(String path) throws BinaryResolutionException {
        return this.loadedImages.computeIfAbsent(path, p -> {
            try ( var is = this.resolveBinary(path)) {
                return ImageIO.read(is);
            } catch (IOException ex) {
                throw new BinaryResolutionException("Cannot read the binary resource at path " + path);
            }
        });
    }
    
    public Font resolveFont(String path) throws BinaryResolutionException {
        return this.loadedFonts.computeIfAbsent(path, p -> {
            try ( var is = this.resolveBinary(path)) {
                return Font.createFont(Font.TRUETYPE_FONT, is);
            } catch (IOException ex) {
                throw new BinaryResolutionException("Cannot read the binary resource at path " + path);
            } catch (FontFormatException ex) {
                throw new BinaryResolutionException("Cannot read the binary resource at path " + path + " as font");
            }
        });
    }

    public ImageTemplate template() {
        return template;
    }

    @Override
    public Optional<Object> resolveVar(String name) {
        if (this.parameter.containsKey(name)) {
            return Optional.ofNullable(this.parameter.get(name));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Function<String, String>> resolveFunction(String name) {
        if (this.functions.containsKey(name)) {
            return Optional.ofNullable(this.functions.get(name));
        }
        return Optional.empty();
    }
}
