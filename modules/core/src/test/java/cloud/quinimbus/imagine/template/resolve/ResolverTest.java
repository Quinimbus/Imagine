package cloud.quinimbus.imagine.template.resolve;

import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResolverTest {
    
    @Test
    public void testResolveString() {
        assertResolveString("$var", Map.of("var", "Test"), "Test");
        assertResolveString("Hello $name", Map.of("name", "World"), "Hello World");
        assertResolveString("Hello $name from ${name}town", Map.of("name", "World"), "Hello World from Worldtown");
        assertResolveString("Hello $name", Map.of(), "Hello $name");
        assertResolveString("${var:1->one;2->two}", Map.of("var", "1"), "one");
        assertResolveString("${var:1->one;2->two}", Map.of("var", "2"), "two");
    }
    
    @Test
    public void testResolveInteger() {
        assertResolveInteger("$n", Map.of("n", 1), 1);
    }
    
    public void assertResolveString(String str, Map<String, Object> params, String expected) {
        var res = new ResolvableStringImpl(str);
        Assertions.assertEquals(expected, res.get(v -> Optional.ofNullable(params.get(v))));
    }
    
    public void assertResolveInteger(String str, Map<String, Object> params, Integer expected) {
        var res = new ResolvableIntegerImpl(str);
        Assertions.assertEquals(expected, res.get(v -> Optional.ofNullable(params.get(v))));
    }
}
