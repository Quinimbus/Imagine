package cloud.quinimbus.imagine.thumb;

import cloud.quinimbus.imagine.api.ImageTemplate;
import cloud.quinimbus.imagine.api.ImageTemplate.Base;
import cloud.quinimbus.imagine.api.TemplateStep;
import cloud.quinimbus.imagine.api.TemplateStep.Position;
import cloud.quinimbus.imagine.api.BinaryResolutionException;
import cloud.quinimbus.imagine.api.TemplateStep.AutoScale;
import cloud.quinimbus.imagine.api.Thumbnailer;
import cloud.quinimbus.imagine.template.ImageCreatorImpl;
import cloud.quinimbus.imagine.template.resolve.ResolvableIntegerImpl;
import cloud.quinimbus.imagine.template.resolve.ResolvableStringImpl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class ThumbnailerImpl implements Thumbnailer {

    @Override
    public byte[] createImageThumb(InputStream is, ThumbParams params) throws IOException, BinaryResolutionException {
        var width = params.width();
        var height = params.height();
        InputStream imgIs;
        if (height == null || width == null) {
            var imgBytes = is.readAllBytes();
            var img = ImageIO.read(new ByteArrayInputStream(imgBytes));
            var aspectRatio = (float)img.getWidth() / img.getHeight();
            if (params.width() == null) {
                width = Math.round(height * aspectRatio);
            } else {
                height = Math.round(width / aspectRatio);
            }
            imgIs = new ByteArrayInputStream(imgBytes);
        } else {
            imgIs = is;
        }
        var template = new ImageTemplate(
                List.of(),
                List.of(),
                new Base(width, height, "#FFFFFF"),
                List.of(
                        new TemplateStep(
                                TemplateStep.Type.image,
                                new ResolvableStringImpl("img"),
                                null,
                                null,
                                null,
                                null,
                                null,
                                new Position(0, 0, width, height),
                                null)
                ));
        var creator = new ImageCreatorImpl(template);
        var bos = new ByteArrayOutputStream();
        creator.createImage(Map.of(), Map.of(), bos, id -> imgIs);
        return bos.toByteArray();
    }

    @Override
    public byte[] createFontThumb(InputStream is, ThumbParams params) throws IOException, BinaryResolutionException {
        var template = new ImageTemplate(
                List.of(),
                List.of(),
                new Base(params.width(), params.height(), "#FFFFFF"),
                List.of(
                        new TemplateStep(
                                TemplateStep.Type.text,
                                null,
                                null,
                                null,
                                null,
                                null,
                                new cloud.quinimbus.imagine.api.TemplateStep.Textarea(
                                        new ResolvableStringImpl("ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
                                        new ResolvableStringImpl("font"),
                                        new ResolvableIntegerImpl(Math.round(params.height() / 3f)),
                                        new ResolvableStringImpl("#000000"),
                                        new ResolvableStringImpl("left"),
                                        new ResolvableStringImpl("top")),
                                new Position(0, 0, params.width(), params.height()),
                                new AutoScale(TemplateStep.ScaleMethod.none, true, true, true, true)),
                        new TemplateStep(
                                TemplateStep.Type.text,
                                null,
                                null,
                                null,
                                null,
                                null,
                                new cloud.quinimbus.imagine.api.TemplateStep.Textarea(
                                        new ResolvableStringImpl("abcdefghijklmnopqrstuvwxyz"),
                                        new ResolvableStringImpl("font"),
                                        new ResolvableIntegerImpl(Math.round(params.height() / 3f)),
                                        new ResolvableStringImpl("#000000"),
                                        new ResolvableStringImpl("left"),
                                        new ResolvableStringImpl("bottom")),
                                new Position(0, 0, params.width(), params.height()),
                                new AutoScale(TemplateStep.ScaleMethod.none, true, true, true, true))
                ));
        var creator = new ImageCreatorImpl(template);
        var bos = new ByteArrayOutputStream();
        creator.createImage(Map.of(), Map.of(), bos, id -> is);
        return bos.toByteArray();
    }
}
