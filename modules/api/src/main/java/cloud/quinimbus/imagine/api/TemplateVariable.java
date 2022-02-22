package cloud.quinimbus.imagine.api;

import java.util.Map;

public record TemplateVariable(String name, Choice choice) {
    
    public static record Choice(String sourceVariable, Map<String, Object> cases) {
        
    }
}
