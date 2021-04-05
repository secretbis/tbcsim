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
      remove: ['ESLintWebpackPlugin', 'StyleLintPlugin'],
    },
    configure: function(webpackConfig) {
      const yamlLoader = {
        test: /\.ya?ml$/,
        use: ['json-loader', 'yaml-loader']
      };
      addBeforeLoader(webpackConfig, loaderByName("file-loader"), yamlLoader);

      return webpackConfig;
    }
  },
};
