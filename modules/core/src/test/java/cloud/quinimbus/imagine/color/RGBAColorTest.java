package cloud.quinimbus.imagine.color;

import java.awt.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RGBAColorTest {
    
    private static final ColorsImpl COLORS = new ColorsImpl();
    
    @Test
    public void testDecodeString() {
        assertEquals(
                new RGBAColor(255, 255, 255, 255),
                COLORS.decodeRGBA("#fff"));
        assertEquals(
                new RGBAColor(255, 255, 255, 255),
                COLORS.decodeRGBA("#ffffff"));
        assertEquals(
                new RGBAColor(255, 255, 255, 136),
                COLORS.decodeRGBA("#fff8"));
        assertEquals(
                new RGBAColor(255, 255, 255, 128),
                COLORS.decodeRGBA("#ffffff80"));
    }
    
    @Test
    public void testDecodeInt() {
        assertEquals(
                new RGBAColor(255, 255, 255, 255),
                RGBAColor.decode(new Color(255, 255, 255, 255).getRGB()));
        assertEquals(
                new RGBAColor(255, 127, 0, 200),
                RGBAColor.decode(new Color(255, 127, 0, 200).getRGB()));
    }
    
    @Test
    public void testToHSV() {
        assertEquals(
                new HSVColor(0, 0, 0),
                new RGBAColor(0, 0, 0, 255).toHSV());
        assertEquals(
                new HSVColor(0, 0, 1),
                new RGBAColor(255, 255, 255, 255).toHSV());
        assertEquals(
                new HSVColor(0, 1, 1),
                new RGBAColor(255, 0, 0, 255).toHSV());
        assertEquals(
                new HSVColor(120, 1, 1),
                new RGBAColor(0, 255, 0, 255).toHSV());
        assertEquals(
                new HSVColor(240, 1, 1),
                new RGBAColor(0, 0, 255, 255).toHSV());
        assertEquals(
                new HSVColor(300, 1, 128.0/255),
                new RGBAColor(128, 0, 128, 255).toHSV());
    }
}
