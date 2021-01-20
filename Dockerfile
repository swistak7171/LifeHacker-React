FROM swistak7171/repository:nginx
COPY build/distributions /usr/share/nginx/html
COPY initialize.sh /
EXPOSE 80
UN ["chmod", "+x", "initialize.sh"]
ENTRYPOINT ["/.initialize.sh"]