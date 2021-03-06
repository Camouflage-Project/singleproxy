const { parse } = require('url')
const next = require('next')
const express = require('express')

const dev = process.env.NODE_ENV !== 'production'
const app = next({dev})
const handle = app.getRequestHandler()

const port = dev ? 3000 : 443
const https = require('https')
const http = require('http')
const fs = require('fs');
const options = {
    key: fs.readFileSync('/etc/letsencrypt/live/alealogic.com/privkey.pem'),
    cert: fs.readFileSync('/etc/letsencrypt/live/alealogic.com/cert.pem'),
    ca: fs.readFileSync('/etc/letsencrypt/live/alealogic.com/chain.pem'),
};


app.prepare().then(() => {
    const httpApp = express();
    httpApp.get("*", (req, res) => {
        res.writeHead(302, {
            Location: "https://" + req.headers.host + req.url
        });
        res.end();
    });

    http.createServer(httpApp).listen(80, err => {
        if (err) throw err;
        console.log(`> HTTP Ready on http://localhost:${80}`);
    });

    https.createServer(options, (req, res) => {
        const parsedUrl = parse(req.url, true)
        handle(req, res, parsedUrl)
    }).listen(port, (err) => {
        if (err) throw err
        console.log(`> Ready on http://localhost:${port}`)
    })
});
