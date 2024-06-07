package cloud.quinimbus.imagine.template;

import cloud.quinimbus.imagine.api.BinaryResolutionException;
import cloud.quinimbus.imagine.api.ImageCreator;
import cloud.quinimbus.imagine.api.ImageTemplate;
import cloud.quinimbus.imagine.api.TemplateStep;
import cloud.quinimbus.imagine.color.ColorsImpl;
import cloud.quinimbus.imagine.color.RGBAColor;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.imageio.ImageIO;

public class ImageCreatorImpl implements ImageCreator {
    
    private static final ColorsImpl COLORS = new ColorsImpl();
    
    private final ImageTemplate template;

    public ImageCreatorImpl(ImageTemplate template) {
        this.template = template;
    }
    
    @Override
    public void createImage(Map<String, Object> parameter, Map<String, Function<String, String>> functions, OutputStream os, Function<String, InputStream> resourceLoader) throws IOException, BinaryResolutionException {
        var base = this.template.base();
        var ctx = new CreationContext(parameter, functions, resourceLoader, this.template);
        var img = new BufferedImage(base.width(), base.height(), BufferedImage.TYPE_INT_ARGB);
        var g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(COLORS.decodeRGBA(base.background()).toColor());
        g.fillRect(0, 0, base.width(), base.height());
        for (var step : this.template.steps()) {
            switch (step.type()) {
                case image -> this.imageStep(g, step, ctx);
                case hue -> this.hueStep(img, step, ctx);
                case text -> this.textStep(g, step, ctx);
            }
        }
        ImageIO.write(img, "png", os);
    }

    private void imageStep(Graphics2D g, TemplateStep step, CreationContext ctx) throws BinaryResolutionException {
        var img = ctx.resolveImage(step.src().get(ctx));
        var position = step.position();
        g.drawImage(
                img,
                position.x().get(ctx),
                position.y().get(ctx),
                position.width().get(ctx),
                position.height().get(ctx),
                null);
    }

    private void hueStep(BufferedImage img, TemplateStep step, CreationContext ctx) throws BinaryResolutionException {
        BiFunction<Integer, Integer, Boolean> pixelPredicate = (x, y) -> true;
        if (step.mask() != null) {
            switch (step.mask().type()) {
                case image -> {
                    var base = ctx.template().base();
                    var maskImg = ctx.resolveImage(step.mask().src());
                    var scaledMaskImg = new BufferedImage(base.width(), base.height(), BufferedImage.TYPE_INT_ARGB);
                    scaledMaskImg.createGraphics().drawImage(maskImg, 0, 0, base.width(), base.height(), null);
                    pixelPredicate = (x, y) -> RGBAColor.decode(scaledMaskImg.getRGB(x, y)).red() > 127;
                }
            }
        }
        for(var x = 0; x < img.getWidth(); x++) {
            for (var y = 0; y < img.getHeight(); y++) {
                if (pixelPredicate.apply(x, y)) {
                    var rgbaColor = RGBAColor.decode(img.getRGB(x, y));
                    var hsvColor = rgbaColor.toHSV();
                    if (step.hue() != null && !step.hue().isEmpty(ctx)) {
                        hsvColor = hsvColor.withHue(step.hue().get(ctx));
                    }
                    if (step.saturationModifier()!= null && !step.saturationModifier().isEmpty(ctx)) {
                        hsvColor = hsvColor.withSaturation(hsvColor.saturation() * step.saturationModifier().get(ctx));
                    }
                    if (step.valueModifier()!= null && !step.valueModifier().isEmpty(ctx)) {
                        hsvColor = hsvColor.withValue(hsvColor.value()* step.valueModifier().get(ctx));
                    }
                    img.setRGB(x, y, hsvColor.toRGBA(rgbaColor.alpha()).toRGB());
                }
            }
        }
    }

    private void textStep(Graphics2D g, TemplateStep step, CreationContext ctx) throws BinaryResolutionException {
        var textarea = step.textarea();
        var position = step.position();
        var fontSize = textarea.fontSize().get(ctx);
        var font = ctx.resolveFont(textarea.font().get(ctx))
                .deriveFont(fontSize.floatValue());
        var fontMetrics = g.getFontMetrics(font);
        var text = textarea.text().get(ctx);
        var stringWidth = fontMetrics.stringWidth(text);
        var stringHeight = fontMetrics.getHeight();
        if (TemplateStep.ScaleMethod.adjustFontSize.equals(step.autoScale().method())) {
            boolean scaledDown = false;
            if (step.autoScale().scaleDown()) {
                if (step.autoScale().scaleOnWidth()) {
                    while (stringWidth > position.width().get(ctx) && fontSize > 1) {
                        fontSize--;
                        scaledDown = true;
                        font = font.deriveFont(fontSize.floatValue());
                        fontMetrics = g.getFontMetrics(font);
                        stringWidth = fontMetrics.stringWidth(text);
                    }
                }
                if (step.autoScale().scaleOnHeight()) {
                    while (stringHeight > position.height().get(ctx) && fontSize > 1) {
                        fontSize--;
                        scaledDown = true;
                        font = font.deriveFont(fontSize.floatValue());
                        fontMetrics = g.getFontMetrics(font);
                        stringHeight = fontMetrics.getHeight();
                    }
                }
            }
            if (!scaledDown && step.autoScale().scaleUp()) {
                if (step.autoScale().scaleOnWidth()) {
                    var scaledUp = false;
                    while (stringWidth < position.width().get(ctx) && fontSize < 1000) {
                        fontSize++;
                        scaledUp = true;
                        font = font.deriveFont(fontSize.floatValue());
                        fontMetrics = g.getFontMetrics(font);
                        stringWidth = fontMetrics.stringWidth(text);
                    }
                    if (scaledUp) {
                        fontSize--;
                    }
                }
                if (step.autoScale().scaleOnHeight()) {
                    var scaledUp = false;
                    while (stringHeight < position.height().get(ctx) && fontSize < 1000) {
                        fontSize++;
                        scaledUp = true;
                        font = font.deriveFont(fontSize.floatValue());
                        fontMetrics = g.getFontMetrics(font);
                        stringHeight = fontMetrics.getHeight();
                    }
                    if (scaledUp) {
                        fontSize--;
                    }
                }
            }
        }
        g.setColor(COLORS.decodeRGBA(textarea.color().get(ctx)).toColor());
        g.setFont(font);
        var x = switch(textarea.verticalAlign().get(ctx)) {
            case "left" -> position.x().get(ctx);
            case "center" -> position.x().get(ctx) + position.width().get(ctx) / 2 - stringWidth / 2;
            case "right" -> position.x().get(ctx) + position.width().get(ctx) - stringWidth;
            default -> throw new IllegalArgumentException("Invalid vertical align: " + textarea.verticalAlign().get(ctx));
        };
        var y = switch(textarea.horizontalAlign().get(ctx)) {
            case "top" -> position.y().get(ctx) + fontMetrics.getAscent();
            case "center" -> position.y().get(ctx) + position.height().get(ctx) / 2 + fontMetrics.getAscent() / 2;
            case "bottom" -> position.y().get(ctx) + position.height().get(ctx) - fontMetrics.getDescent();
            default -> throw new IllegalArgumentException("Invalid horizontal align: " + textarea.horizontalAlign().get(ctx));
        };
        //g.drawRect(position.x().get(ctx), position.y().get(ctx), position.width().get(ctx), position.height().get(ctx));
        g.drawString(text, x, y);
    }
}
