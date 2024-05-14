# Use an official Python runtime as a parent image
FROM python:3.10-slim

# Set the working directory in the container
WORKDIR /app

# Install curl to download Ollama
RUN apt-get update && apt-get install -y curl

# Install Ollama
RUN curl -fsSL https://ollama.com/install.sh | sh

# Pull the desired Ollama model
RUN ollama pull phi3

# Copy the local directory contents into the container at /app
COPY ../../../Desktop/aimodel_mistrallite /app

# Install any needed packages specified in requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Make port 8000 available to the world outside this container
EXPOSE 8000

# Define environment variable
ENV MODULE_NAME="app"
ENV VARIABLE_NAME="app"

# Command to run the Ollama model and start the FastAPI application
CMD uvicorn app:app --host 0.0.0.0 --port 8000
