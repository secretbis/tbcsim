const {
  addBeforeLoader,
  loaderByName,
  when,
  whenDev,
  whenProd,
  whenTest,
  ESLINT_MODES,
  POSTCSS_MODES,
} = require("@craco/craco");

module.exports = {
  webpack: {
    configure: function(webpackConfig) {
      const fragLoader = {
        test: /\.ya?ml$/,
        use: ['json-loader', 'yaml-loader']
      };

      addBeforeLoader(webpackConfig, loaderByName("file-loader"), fragLoader );

      return webpackConfig;
    }
    // configure: {
    //   module: {
    //     rules: [
    //       {
    //         test: /\.ya?ml$/,
    //         use: "yaml-loader",
    //       },
    //     ],
    //   },
    // },
  },
};
