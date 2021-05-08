const next = require('next')
const express = require('express')

const dev = process.env.NODE_ENV !== 'production'
const app = next({dev})

const port = 3000
const http = require('http')


app.prepare().then(() => {
    const httpApp = express();
    http.createServer(httpApp).listen(port, err => {
        if (err) throw err;
        console.log(`> HTTP Ready on http://localhost:${port}`);
    });
});
