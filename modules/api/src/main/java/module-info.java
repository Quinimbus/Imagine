module cloud.quinimbus.imagine.api {
    exports cloud.quinimbus.imagine.api;

    opens cloud.quinimbus.imagine.api to
            com.fasterxml.jackson.databind;

    requires java.desktop;
}
