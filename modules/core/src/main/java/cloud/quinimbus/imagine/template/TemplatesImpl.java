package cloud.quinimbus.imagine.template;

import cloud.quinimbus.imagine.api.ImageCreator;
import cloud.quinimbus.imagine.api.ImageTemplate;
import cloud.quinimbus.imagine.api.Templates;

public class TemplatesImpl implements Templates {

    @Override
    public ImageCreator newImageCreator(ImageTemplate template) {
        return new ImageCreatorImpl(template);
    }
}
