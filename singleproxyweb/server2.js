const { parse } = require('url')
const next = require('next')

const dev = process.env.NODE_ENV !== 'production'
const app = next({dev})
const handle = app.getRequestHandler()

const port = 3000
const https = require('https')
const fs = require('fs');
const options = {
    key: fs.readFileSync('/etc/letsencrypt/live/alealogic.com/privkey.pem'),
    cert: fs.readFileSync('/etc/letsencrypt/live/alealogic.com/cert.pem'),
    ca: fs.readFileSync('/etc/letsencrypt/live/alealogic.com/chain.pem'),
};


app.prepare().then(() => {
    https.createServer(options, (req, res) => {
        const parsedUrl = parse(req.url, true)
        handle(req, res, parsedUrl)
    }).listen(port, (err) => {
        if (err) throw err
        console.log(`> Ready on http://localhost:${port}`)
    })
});
