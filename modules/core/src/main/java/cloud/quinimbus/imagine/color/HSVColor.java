package cloud.quinimbus.imagine.color;

public record HSVColor(double hue, double saturation, double value) implements cloud.quinimbus.imagine.api.HSVColor {

    public HSVColor withHue(double hue) {
        return new HSVColor(hue, this.saturation, this.value);
    }

    public HSVColor withSaturation(double saturation) {
        return new HSVColor(this.hue, saturation, this.value);
    }

    public HSVColor withValue(double value) {
        return new HSVColor(this.hue, this.saturation, value);
    }

    public RGBAColor toRGBA(int alpha) {
        var c = this.saturation * this.value;
        var x = c * (1 - Math.abs((this.hue / 60) % 2 - 1));
        var m = this.value - c;
        double r;
        double g;
        double b;
        if (this.hue < 60) {
            r = c;
            g = x;
            b = 0;
        } else if (this.hue < 120) {
            r = x;
            g = c;
            b = 0;
        } else if (this.hue < 180) {
            r = 0;
            g = c;
            b = x;
        } else if (this.hue < 240) {
            r = 0;
            g = x;
            b = c;
        } else if (this.hue < 300) {
            r = x;
            g = 0;
            b = c;
        } else if (this.hue < 360) {
            r = c;
            g = 0;
            b = x;
        } else {
            throw new IllegalStateException("hue has to be between 0 and 360");
        }
        return new RGBAColor(
                (int) Math.round((r + m) * 255),
                (int) Math.round((g + m) * 255),
                (int) Math.round((b + m) * 255),
                alpha);
    }
}
