const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {

    app.use(
        '/api/login',
        createProxyMiddleware(
            {
                target: 'http://localhost:8181',
                pathRewrite: {
                    '^/api/login': '/login'
                },
                changeOrigin: true,
                secure: false
            }
        )
    )

    app.use(
        '/api/stock/mgnt',
        createProxyMiddleware(
            {
                target: 'http://localhost:8282',
                pathRewrite: {
                    '^/api/stock/mgnt': '/stock/mgnt'
                },
                changeOrigin: true,
                secure: false
            }
        )
    )
}