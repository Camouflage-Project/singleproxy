singleproxy.com

to create torprivoxy docker image:
tor --hash-password password
add to torrc (if not already there)

cd into docker/torprivoxydocker

docker build --tag test:1.0 .

docker run -d --network host test:1.0 9050 9051 8118


start postgres:
docker run --name postgres -e POSTGRES_PASSWORD=somepassword -v /home/postgres/data:/var/lib/postgresql/data -d -p 5432:5432 postgres:latest


build and start with:
mvn package
java -jar compiledjar.jar -Dspring.profiles.active=prod


start zaproxy 

make request with:
curl -vx localhost:8082 --cacert my_certificate.cer -H "Singleproxy-API-key: 80d2e07e-eb75-410b-a832-bf6020cde212" https://icanhazip.com/

note: my_certificate.cer is located in project root. the above curl must be executed in the directory where my_certificate.cer is located

or

start martian

make request with:
curl -vx localhost:8080 -H "Singleproxy-API-key: c7ccad6d-f2ce-4597-9958-ded2a712a4d6" --cacert rootCACert.pem https://httpbin.org/get


to create privoxy docker image:
cd into docker/privoxys

docker build --tag onlyprivoxy:1.0 .

docker run -d --network host onlyprivoxy:1.0 8118

start and call zaproxy like mentioned above





start frontend
sudo NODE_ENV=production node server.js
