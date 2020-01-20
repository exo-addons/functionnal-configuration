const path = require('path');
const merge = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {

    context: path.resolve(__dirname, 'src/main/frontend/apps'),

    entry: {
    	functionalConfiguration: "./functionalConfiguration/functionalConfiguration.js",
        highlightSpacesView: "./hightLightSpaces/view/HighlightSpacesView.js",
        highlightSpacesEdit: "./hightLightSpaces/edit/HighlightSpacesEdit.js"
    },
    output: {
    	path: path.resolve('/project/exoplatform/platform/5.3.0/webapps/functional-configuration-portlets/js'),
    	filename: '[name].bundle.js',
    	libraryTarget: 'amd'
    },

    devtool: 'inline-source-map',
});