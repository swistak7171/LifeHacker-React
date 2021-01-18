FROM swistak7171/repository:ubuntu AS git_stage
RUN apt-get update && \
        apt-get install -y git && \
        git clone https://github.com/swistak7171/LifeHacker-React.git

FROM swistak7171/repository:openjdk
COPY --from=git_stage LifeHacker-React LifeHacker-React
RUN cd LifeHacker-React && \
    chmod +x gradlew && \
    ./gradlew build

FROM swistak7171/repository:nginx
COPY build/distributions /usr/share/nginx/html
COPY initialize.sh /
RUN chmod +x initialize.sh
ENTRYPOINT ["./initialize.sh"]
EXPOSE 80