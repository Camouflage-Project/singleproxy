singleproxy.com

tor --hash-password password
add to torrc


docker build --tag test:1.0 .

docker run -d --network host test:1.0 9050 9051 8118


start zaproxy 
curl -vx localhost:8082 --cacert my_certificate.cer https://icanhazip.com/