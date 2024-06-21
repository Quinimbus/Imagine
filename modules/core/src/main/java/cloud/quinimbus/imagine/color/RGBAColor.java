package cloud.quinimbus.imagine.color;

import java.awt.Color;

public record RGBAColor(int red, int green, int blue, int alpha) implements cloud.quinimbus.imagine.api.RGBAColor {

    public static RGBAColor decode(int argbData) {
        return new RGBAColor(
                (argbData >>> 16) & 0xFF, (argbData >>> 8) & 0xFF, argbData & 0xFF, (argbData >>> 24) & 0xFF);
    }

    public Color toColor() {
        return new Color(this.red, this.green, this.blue, this.alpha);
    }

    public int toRGB() {
        return this.toColor().getRGB();
    }

    public HSVColor toHSV() {
        var r = this.red / 255.0;
        var g = this.green / 255.0;
        var b = this.blue / 255.0;
        var max = Math.max(Math.max(r, g), b);
        var min = Math.min(Math.min(r, g), b);
        var delta = max - min;
        var hue = 0.0;
        if (delta > 0) {
            if (max == r) {
                hue = (g - b) / delta % 6;
                if (hue < 0) {
                    hue += 6;
                }
            } else if (max == g) {
                hue = (b - r) / delta + 2;
            } else {
                hue = (r - g) / delta + 4;
            }
        }
        return new HSVColor(60 * hue, max == 0 ? 0 : delta / max, max);
    }
}
