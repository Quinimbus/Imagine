package cloud.quinimbus.imagine.api;

import java.util.Optional;

@FunctionalInterface
public interface VarResolver {

    Optional<Object> resolveVar(String name);
}
