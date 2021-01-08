FROM openjdk
COPY create.sh /
CMD ["./create.sh"]

FROM nginx
COPY build/distributions /usr/share/nginx/html