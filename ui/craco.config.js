const {
  addBeforeLoader,
  loaderByName,
} = require("@craco/craco");

module.exports = {
  eslint: {
    enable: false
  },
  webpack: {
    plugins: {
      remove: ['ESLintWebpackPlugin'],
    },
    configure: function(webpackConfig) {
      const fragLoader = {
        test: /\.ya?ml$/,
        use: ['json-loader', 'yaml-loader']
      };

      addBeforeLoader(webpackConfig, loaderByName("file-loader"), fragLoader );

      return webpackConfig;
    }
  },
};
