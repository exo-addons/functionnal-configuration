const path = require('path');
const merge = require('webpack-merge');
const common = require('./webpack.common.js');
const webpack = require('webpack');

module.exports = merge(common, {

    devtool: 'source-map',

    context: path.resolve(__dirname, 'src/main/frontend/apps'),

    entry: {
    	functionalConfiguration: "./functionalConfiguration/functionalConfiguration.js"
    },
    output: {
    	path: path.resolve(__dirname, 'src/main/webapp/js/'),
    	filename: '[name].bundle.js',
    	libraryTarget: 'amd'
    },
    plugins: [

        //new UglifyJSPlugin({
        //    sourceMap: true
        //}),
        new webpack.DefinePlugin({
            'process.env.NODE_ENV': JSON.stringify('production')
        })
    ]
});