FROM swistak7171/repository:ubuntu
RUN apt-get update && apt-get install -y git

FROM swistak7171/repository:openjdk
COPY create.sh /
RUN chmod +x create.sh && ./create.sh

FROM swistak7171/repository:nginx
COPY build/distributions /usr/share/nginx/html
EXPOSE 80