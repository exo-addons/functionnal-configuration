const path = require('path');
const merge = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {

    context: path.resolve(__dirname, 'src/main/frontend/apps'),

    entry: {
    	functionalConfiguration: "./functionalConfiguration/functionalConfiguration.js"
    },
    output: {
    	path: path.resolve(__dirname, '../webapp/js/'),
    	filename: '[name].bundle.js',
    	libraryTarget: 'amd'
    },

    devtool: 'inline-source-map',
});