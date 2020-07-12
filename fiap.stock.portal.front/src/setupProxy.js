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
        '/api/portal',
        createProxyMiddleware(
            {
                target: 'http://localhost:8383',
                pathRewrite: {
                    '^/api/portal': '/portal'
                },
                changeOrigin: true,
                secure: false
            }
        )
    )
}