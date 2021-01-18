FROM swistak7171/repository:nginx
COPY build/distributions /usr/share/nginx/html
COPY initialize.sh /
RUN chmod +x initialize.sh
ENTRYPOINT ["/bin/bash", "./initialize.sh"]
EXPOSE 80