package cloud.quinimbus.imagine.color;

import cloud.quinimbus.imagine.api.Colors;
import java.util.Optional;
import java.util.regex.Pattern;

public class ColorsImpl implements Colors {

    private static final Pattern HEX_RGB_SHORT = Pattern.compile("#([0-9a-f])([0-9a-f])([0-9a-f])");
    private static final Pattern HEX_RGB = Pattern.compile("#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})");
    private static final Pattern HEX_RGBA_SHORT = Pattern.compile("#([0-9a-f])([0-9a-f])([0-9a-f])([0-9a-f])");
    private static final Pattern HEX_RGBA = Pattern.compile("#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})");

    @Override
    public RGBAColor decodeRGBA(String src) {
        return tryDecode(src.toLowerCase(), HEX_RGB_SHORT)
                .or(() -> tryDecode(src, HEX_RGB))
                .or(() -> tryDecode(src, HEX_RGBA_SHORT))
                .or(() -> tryDecode(src, HEX_RGBA))
                .orElseThrow(() -> new IllegalArgumentException("Cannot decode %s as RGBA".formatted(src)));
    }

    private Optional<RGBAColor> tryDecode(String src, Pattern pattern) {
        var matcher = pattern.matcher(src.toLowerCase());
        if (matcher.matches()) {
            return Optional.of(new RGBAColor(
                    parseHexValue(matcher.group(1)),
                    parseHexValue(matcher.group(2)),
                    parseHexValue(matcher.group(3)),
                    matcher.groupCount() == 3 ? 255 : parseHexValue(matcher.group(4))));
        }
        return Optional.empty();
    }

    private int parseHexValue(String hex) {
        return switch (hex.length()) {
            case 1 -> Integer.parseInt("%s%s".formatted(hex, hex), 16);
            case 2 -> Integer.parseInt(hex, 16);
            default -> throw new IllegalArgumentException();
        };
    }
}
