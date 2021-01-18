FROM swistak7171/repository:ubuntu
RUN apt-get install git

FROM swistak7171/repository:openjdk
COPY create.sh /
RUN ./create.sh

FROM swistak7171/repository:nginx
COPY build/distributions /usr/share/nginx/html
EXPOSE 80