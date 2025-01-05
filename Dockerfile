# Use an ARM-compatible Ubuntu base image (for Apple Silicon)
# FROM arm64v8/ubuntu:22.04
FROM ubuntu:22.04
# Set environment variables for non-interactive installation
FROM maven:3.6.3-jdk-11

# Install Java, Spark, MongoDB and other tools
RUN apt-get update && apt-get install -y \
    bash \
    wget \
    curl \
    gnupg \
    && rm -rf /var/lib/apt/lists/*

# Set Java 11 as the default
# RUN update-alternatives --config java

# Set environment variables for Spark
# ENV SPARK_VERSION=3.4.0
# ENV HADOOP_VERSION=3
# ENV SPARK_HOME=/opt/spark
# ENV PATH=$SPARK_HOME/bin:$PATH
#
# # Download and install Apache Spark
# RUN wget https://downloads.apache.org/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz -O /tmp/spark.tgz && \
#     mkdir -p /opt && \
#     tar -xzf /tmp/spark.tgz -C /opt && \
#     mv /opt/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION} /opt/spark && \
#     rm /tmp/spark.tgz

# Expose necessary ports
EXPOSE 8080 27017

# Set working directory
WORKDIR /app

# Copy application files (assuming a fat JAR for Spring Boot app)
COPY . /app/

# Command to run MongoDB, Spark and Spring Boot
CMD ["/bin/bash"]