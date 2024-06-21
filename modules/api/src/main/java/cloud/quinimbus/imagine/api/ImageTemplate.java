package cloud.quinimbus.imagine.api;

import java.util.List;

public record ImageTemplate(
        List<TemplateParameter> parameters, List<TemplateVariable> variables, Base base, List<TemplateStep> steps) {

    public static record Base(int width, int height, String background) {}
}
