package cloud.quinimbus.imagine.api;

public interface HSVColor {
    
    HSVColor withHue(double hue);
    
    HSVColor withSaturation(double saturation);
    
    HSVColor withValue(double value);
    
    RGBAColor toRGBA(int alpha);
    
    double hue();
    
    double saturation();
    
    double value();
}
