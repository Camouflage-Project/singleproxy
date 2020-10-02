singleproxy.com

to create torprivoxy docker image:
tor --hash-password password
add to torrc (if not already there)

cd into docker/torprivoxydocker

docker build --tag test:1.0 .

docker run -d --network host test:1.0 9050 9051 8118

start zaproxy 
curl -vx localhost:8082 --cacert my_certificate.cer -H "Singleproxy-API-key: 80d2e07e-eb75-410b-a832-bf6020cde212" https://icanhazip.com/

note: my_certificate.cer is located in project root. the above curl must be executed in the directory where my_certificate.cer is located



to create privoxy docker image:
cd into docker/privoxy

docker build --tag onlyprivoxy:1.0 .

docker run -d --network host onlyprivoxy:1.0 8118

start and call zaproxy like mentioned above