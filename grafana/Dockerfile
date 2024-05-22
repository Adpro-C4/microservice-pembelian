# Use an ARG to allow specifying the Grafana version at build time
ARG VERSION=latest

# Use the specified version of the Grafana OSS image
FROM grafana/grafana-oss:${VERSION}

# Set environment variables to install Grafana plugins
ENV \
    GF_INSTALL_PLUGINS=grafana-piechart-panel,grafana-worldmap-panel,grafana-clock-panel,grafana-simple-json-datasource

# Expose port 3000
EXPOSE 3000

# Start Grafana
CMD ["grafana-server"]
