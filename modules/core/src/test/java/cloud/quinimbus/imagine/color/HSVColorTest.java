package cloud.quinimbus.imagine.color;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HSVColorTest {
    
    @Test
    public void testToRGBA() {
        assertEquals(
                new RGBAColor(0, 0, 0, 255),
                new HSVColor(0, 0, 0).toRGBA(255));
        assertEquals(
                new RGBAColor(255, 255, 255, 255),
                new HSVColor(0, 0, 1).toRGBA(255));
        assertEquals(
                new RGBAColor(255, 0, 0, 255),
                new HSVColor(0, 1, 1).toRGBA(255));
        assertEquals(
                new RGBAColor(0, 255, 0, 255),
                new HSVColor(120, 1, 1).toRGBA(255));
        assertEquals(
                new RGBAColor(0, 0, 255, 255),
                new HSVColor(240, 1, 1).toRGBA(255));
        assertEquals(
                new RGBAColor(128, 0, 128, 255),
                new HSVColor(300, 1, 128.0/255).toRGBA(255));
    }
}
