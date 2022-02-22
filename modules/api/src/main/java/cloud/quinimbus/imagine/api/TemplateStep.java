package cloud.quinimbus.imagine.api;

public record TemplateStep(Type type, String src, ResolvableInteger hue, ImageMask mask, Textarea textarea, Position position) {
    
    public static enum Type {
        image, hue, text
    }
    
    public static record ImageMask(MaskType type, String src) {
        
    }
    
    public static enum MaskType {
        image
    }
    
    public static record Textarea(ResolvableString text, ResolvableString font, ResolvableInteger fontSize, ResolvableString color) {
        
    }
    
    public static record Position(ResolvableInteger x, ResolvableInteger y, ResolvableInteger width, ResolvableInteger height) {
        public Position(int x, int y, int width, int height) {
            this(
                    Resolvables.get().integer(x),
                    Resolvables.get().integer(y),
                    Resolvables.get().integer(width),
                    Resolvables.get().integer(height));
        }
    }
}
