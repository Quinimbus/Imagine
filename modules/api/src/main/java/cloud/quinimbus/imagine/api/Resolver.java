package cloud.quinimbus.imagine.api;

import java.util.Optional;
import java.util.function.Function;

public interface Resolver extends VarResolver, FunctionResolver {
    
    public static Resolver of(VarResolver vr) {
        return of(vr, n -> Optional.empty());
    }
    
    public static Resolver of(VarResolver vr, FunctionResolver fr) {
        return new Resolver() {
            @Override
            public Optional<Object> resolveVar(String name) {
                return vr.resolveVar(name);
            }

            @Override
            public Optional<Function<String, String>> resolveFunction(String name) {
                return fr.resolveFunction(name);
            }
        };
    }
}
