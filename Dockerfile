FROM swistak7171/repository:nginx
COPY build/distributions /usr/share/nginx/html
COPY initialize.sh /
EXPOSE 80
ENTRYPOINT ["chmod +x initialize.sh && ./initialize.sh && service nginx restart"]