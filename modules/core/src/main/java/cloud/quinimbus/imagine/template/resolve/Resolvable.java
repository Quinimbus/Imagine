package cloud.quinimbus.imagine.template.resolve;

import cloud.quinimbus.imagine.api.FunctionResolver;
import cloud.quinimbus.imagine.api.Resolver;
import cloud.quinimbus.imagine.api.VarResolver;
import cloud.quinimbus.imagine.template.resolve.source.ResolvableJsonNodeSource;
import cloud.quinimbus.imagine.template.resolve.source.ResolvableNumberSource;
import cloud.quinimbus.imagine.template.resolve.source.ResolvableSource;
import cloud.quinimbus.imagine.template.resolve.source.ResolvableStringSource;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public abstract sealed class Resolvable<T> permits ResolvableStringImpl, ResolvableIntegerImpl, ResolvableDoubleImpl {

    private static final Pattern SIMPLE_VAR_REGEX = Pattern.compile("\\$(\\w+)");
    private static final Pattern EXTENDED_VAR_REGEX = Pattern.compile("\\$\\{([\\w;:\\->]+)\\}");
    private static final Pattern EXTENDED_SIMPLE_VAR_REGEX = Pattern.compile("^(\\w+)$");
    private static final Pattern EXTENDED_SWITCH_VAR_REGEX = Pattern.compile("(\\w+):((\\w+->[^;]*(;|$))*)");
    private static final Pattern EXTENDED_SWITCH_VAR_RESOLVER_REGEX = Pattern.compile("(\\w+)->([^;]*)");
    private static final Pattern FUNCTION_CALL = Pattern.compile("#(\\w+)\\(([^\\)]+)\\)");

    private final ResolvableSource source;

    public Resolvable(JsonNode source) {
        this.source = new ResolvableJsonNodeSource(source);
    }

    public Resolvable(String source) {
        this.source = new ResolvableStringSource(source);
    }

    public Resolvable(Number source) {
        this.source = new ResolvableNumberSource(source);
    }

    protected abstract T convert(ResolvableSource source);

    protected abstract T convert(Object source);

    public T get(Resolver resolver) {
        if (this.source.isTextual()) {
            return this.convert(
                    resolveFunctionCalls(
                            resolveExtendedVars(
                                    resolveSimpleVars(
                                            source.getText(),
                                            resolver),
                                    resolver),
                            resolver));
        }
        return this.convert(this.source);
    }

    public boolean isEmpty(Resolver resolver) {
        if (this.source.isTextual()) {
            return source.getText() == null || source.getText().isEmpty();
        }
        return false;
    }

    private static String resolveSimpleVars(String sourceStr, VarResolver resolver) {
        var resultBuilder = new StringBuilder();
        var lastIndex = 0;
        var simpleVarMatcher = SIMPLE_VAR_REGEX.matcher(sourceStr);
        while (simpleVarMatcher.find()) {
            resultBuilder.append(sourceStr.substring(lastIndex, simpleVarMatcher.start()));
            var varName = simpleVarMatcher.group(1);
            var resolved = resolver.resolveVar(varName);
            if (resolved.isPresent()) {
                resultBuilder.append(resolved.get());
            } else {
                resultBuilder.append(sourceStr.substring(simpleVarMatcher.start(), simpleVarMatcher.end()));
            }
            lastIndex = simpleVarMatcher.end();
        }
        resultBuilder.append(sourceStr.substring(lastIndex));
        return resultBuilder.toString();
    }

    private static String resolveExtendedVars(String sourceStr, VarResolver resolver) {
        var resultBuilder = new StringBuilder();
        var lastIndex = 0;
        var extendedVarMatcher = EXTENDED_VAR_REGEX.matcher(sourceStr);
        while (extendedVarMatcher.find()) {
            resultBuilder.append(sourceStr.substring(lastIndex, extendedVarMatcher.start()));
            var extendedVar = extendedVarMatcher.group(1);
            var extendedSimpleVar = EXTENDED_SIMPLE_VAR_REGEX.matcher(extendedVar);
            if (extendedSimpleVar.matches()) {
                var varName = extendedSimpleVar.group(1);
                var resolved = resolver.resolveVar(varName);
                if (resolved.isPresent()) {
                    resultBuilder.append(resolved.get());
                } else {
                    resultBuilder.append(sourceStr.substring(extendedVarMatcher.start(), extendedVarMatcher.end()));
                }
            } else {
                var extendedSwitchVar = EXTENDED_SWITCH_VAR_REGEX.matcher(extendedVar);
                if (extendedSwitchVar.matches()) {
                    var varName = extendedSwitchVar.group(1);
                    var resolved = resolver.resolveVar(varName);
                    if (resolved.isPresent()) {
                        var extendedSwitchResolverVar = EXTENDED_SWITCH_VAR_RESOLVER_REGEX.matcher(extendedSwitchVar.group(2));
                        var switchMap = new LinkedHashMap<String, String>();
                        while (extendedSwitchResolverVar.find()) {
                            switchMap.put(extendedSwitchResolverVar.group(1), extendedSwitchResolverVar.group(2));
                        }
                        if (switchMap.containsKey(resolved.get())) {
                            resultBuilder.append(switchMap.get(resolved.get()));
                        }
                    } else {
                        resultBuilder.append(sourceStr.substring(extendedVarMatcher.start(), extendedVarMatcher.end()));
                    }
                }
            }
            lastIndex = extendedVarMatcher.end();
        }
        resultBuilder.append(sourceStr.substring(lastIndex));
        return resultBuilder.toString();
    }
    
    private static String resolveFunctionCalls(String sourceStr, FunctionResolver resolver) {
        var resultBuilder = new StringBuilder();
        var lastIndex = 0;
        var functionCallMatcher = FUNCTION_CALL.matcher(sourceStr);
        while (functionCallMatcher.find()) {
            resultBuilder.append(sourceStr.substring(lastIndex, functionCallMatcher.start()));
            var functionName = functionCallMatcher.group(1);
            var resolved = resolver.resolveFunction(functionName)
                    .map(func -> func.apply(functionCallMatcher.group(2)))
                    .orElseGet(() -> "missing function: %s".formatted(functionName));
            resultBuilder.append(resolved);
            lastIndex = functionCallMatcher.end();
        }
        resultBuilder.append(sourceStr.substring(lastIndex));
        return resultBuilder.toString();
    }
}
