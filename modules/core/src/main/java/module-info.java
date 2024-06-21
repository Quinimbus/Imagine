module cloud.quinimbus.imagine.core {
    provides cloud.quinimbus.imagine.api.Colors with
            cloud.quinimbus.imagine.color.ColorsImpl;
    provides cloud.quinimbus.imagine.api.Resolvables with
            cloud.quinimbus.imagine.template.resolve.ResolvablesImpl;
    provides cloud.quinimbus.imagine.api.Templates with
            cloud.quinimbus.imagine.template.TemplatesImpl;
    provides cloud.quinimbus.imagine.api.Thumbnailer with
            cloud.quinimbus.imagine.thumb.ThumbnailerImpl;

    requires cloud.quinimbus.imagine.api;
    requires cloud.quinimbus.tools;
    requires java.desktop;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires throwing.interfaces;
}
