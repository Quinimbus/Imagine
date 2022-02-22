package cloud.quinimbus.imagine.api;

public interface HSVColor {
    
    HSVColor withHue(double hue);
    
    RGBAColor toRGBA(int alpha);
}
