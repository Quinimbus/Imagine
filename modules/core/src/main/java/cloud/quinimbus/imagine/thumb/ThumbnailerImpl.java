package cloud.quinimbus.imagine.thumb;

import cloud.quinimbus.imagine.api.ImageTemplate;
import cloud.quinimbus.imagine.api.ImageTemplate.Base;
import cloud.quinimbus.imagine.api.TemplateStep;
import cloud.quinimbus.imagine.api.TemplateStep.Position;
import cloud.quinimbus.imagine.api.BinaryResolutionException;
import cloud.quinimbus.imagine.api.Thumbnailer;
import cloud.quinimbus.imagine.template.ImageCreatorImpl;
import cloud.quinimbus.imagine.template.resolve.ResolvableIntegerImpl;
import cloud.quinimbus.imagine.template.resolve.ResolvableStringImpl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ThumbnailerImpl implements Thumbnailer {

    @Override
    public byte[] createImageThumb(InputStream is) throws IOException, BinaryResolutionException {
        var template = new ImageTemplate(
                List.of(),
                List.of(),
                new Base(200, 200, "#FFFFFF"),
                List.of(
                        new TemplateStep(
                                TemplateStep.Type.image,
                                "img",
                                null,
                                null,
                                null,
                                new Position(0, 0, 200, 200))
                ));
        var creator = new ImageCreatorImpl(template);
        var bos = new ByteArrayOutputStream();
        creator.createImage(Map.of(), bos, id -> is);
        return bos.toByteArray();
    }

    @Override
    public byte[] createFontThumb(InputStream is) throws IOException, BinaryResolutionException {
        var template = new ImageTemplate(
                List.of(),
                List.of(),
                new Base(100, 100, "#FFFFFF"),
                List.of(new TemplateStep(
                                TemplateStep.Type.text,
                                null,
                                null,
                                null,
                                new cloud.quinimbus.imagine.api.TemplateStep.Textarea(
                                        new ResolvableStringImpl("ABCDEF"),
                                        new ResolvableStringImpl("font"),
                                        new ResolvableIntegerImpl(32),
                                        new ResolvableStringImpl("#000000")),
                                new Position(15, 30, 500, 500)),
                        new TemplateStep(
                                TemplateStep.Type.text,
                                null,
                                null,
                                null,
                                new cloud.quinimbus.imagine.api.TemplateStep.Textarea(
                                        new ResolvableStringImpl("abcdef"),
                                        new ResolvableStringImpl("font"),
                                        new ResolvableIntegerImpl(32),
                                        new ResolvableStringImpl("#000000")),
                                new Position(15, 60, 500, 500))
                ));
        var creator = new ImageCreatorImpl(template);
        var bos = new ByteArrayOutputStream();
        creator.createImage(Map.of(), bos, id -> is);
        return bos.toByteArray();
    }
}
