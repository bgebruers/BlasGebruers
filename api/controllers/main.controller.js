var fs = require('fs');

exports.auth = (req, res) => {
    fs.readFile('./vistas/index.html', function(err, data) {
        if(err) console.log(err);
        res.writeHead(200, {'Content-Type': 'text/html'});
        res.write(data);
        return res.end();
    })
}
exports.home = (req, res) => {
    fs.readFile('./vistas/home.html', function(err, data) {
        if(err) console.log(err);
        res.writeHead(200, {'Content-Type': 'text/html'});
        res.write(data);
        return res.end();
    })
}