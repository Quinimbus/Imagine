package cloud.quinimbus.imagine.api;

import java.awt.Color;

public interface RGBAColor {

    Color toColor();

    int toRGB();

    HSVColor toHSV();
}
