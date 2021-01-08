FROM ubuntu
RUN apt-get install git

FROM openjdk
COPY create.sh /
RUN ./create.sh

FROM nginx
COPY build/distributions /usr/share/nginx/html
EXPOSE 80