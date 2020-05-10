if (process.env.NODE_ENV === "production") {
    const opt = require("./elmizo-opt.js");
    opt.main();
    module.exports = opt;
} else {
    var exports = window;
    exports.require = require("./elmizo-fastopt-entrypoint.js").require;
    window.global = window;

    const fastOpt = require("./elmizo-fastopt.js");
    fastOpt.main()
    module.exports = fastOpt;

    if (module.hot) {
        module.hot.accept();
    }
}
