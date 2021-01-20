FROM swistak7171/repository:nginx
COPY build/distributions /usr/share/nginx/html
COPY initialize.sh /
EXPOSE 80
RUN ["chmod", "+x", "initialize.sh"]
CMD ["nginx", "-g", "daemon off;"]
ENTRYPOINT ["/.initialize.sh"]