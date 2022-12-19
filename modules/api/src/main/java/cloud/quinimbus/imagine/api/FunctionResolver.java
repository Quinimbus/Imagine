package cloud.quinimbus.imagine.api;

import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface FunctionResolver {
    
    Optional<Function<String, String>> resolveFunction(String name);
}
