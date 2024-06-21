package cloud.quinimbus.imagine.api;

public record TemplateStep(
        Type type,
        ResolvableString src,
        ResolvableDouble hue,
        ResolvableDouble saturationModifier,
        ResolvableDouble valueModifier,
        ImageMask mask,
        Textarea textarea,
        Position position,
        AutoScale autoScale) {

    public static enum Type {
        image,
        hue,
        text
    }

    public static record ImageMask(MaskType type, String src) {}

    public static enum MaskType {
        image
    }

    public static record Textarea(
            ResolvableString text,
            ResolvableString font,
            ResolvableInteger fontSize,
            ResolvableString color,
            ResolvableString verticalAlign,
            ResolvableString horizontalAlign) {}

    public static record Position(
            ResolvableInteger x, ResolvableInteger y, ResolvableInteger width, ResolvableInteger height) {
        public Position(int x, int y, int width, int height) {
            this(
                    Resolvables.get().integer(x),
                    Resolvables.get().integer(y),
                    Resolvables.get().integer(width),
                    Resolvables.get().integer(height));
        }
    }

    public static record AutoScale(
            ScaleMethod method, Boolean scaleOnWidth, Boolean scaleOnHeight, Boolean scaleDown, Boolean scaleUp) {}

    public static enum ScaleMethod {
        none,
        adjustFontSize
    }
}
