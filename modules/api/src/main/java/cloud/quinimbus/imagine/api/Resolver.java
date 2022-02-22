package cloud.quinimbus.imagine.api;

import java.util.Optional;

@FunctionalInterface
public interface Resolver {
    
    Optional<Object> resolveVar(String name);
}
