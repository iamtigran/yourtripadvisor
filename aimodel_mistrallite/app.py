
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import subprocess
import os
import logging

app = FastAPI()

# Setup logging
logging.basicConfig(level=logging.INFO)

class Query(BaseModel):
    question: str

def run_ollama(question):
    """Run the Ollama model command with adjusted subprocess settings."""
    # Retrieve the model name from environment variables or default to 'phi'
    model_name = os.getenv('MODEL_NAME', 'phi3')  # Default to 'phi' if not specified
    if os.name == 'nt':
        creationflags = subprocess.CREATE_NO_WINDOW
    else:
        creationflags = 0

    result = subprocess.run(
        ['ollama', 'run', model_name, question],  # 'phi' will be used unless another model is specified in the environment variable
        capture_output=True,
        text=True,
        creationflags=creationflags
    )
    logging.info(f"Model run completed with return code {result.returncode}")
    return result


def parse_travel_recommendations(text):
    """Parse the output text to extract useful travel information."""
    lines = text.split('\n')
    useful_lines = [line for line in lines if line and not line.startswith("failed to get console mode")]
    if useful_lines:
        structured_info = {
            "intro": useful_lines[0],
            "details": " ".join(useful_lines[1:])
        }
        return structured_info
    else:
        return {"error": "No useful output received"}

@app.post("/query/")
async def query_model(query: Query):
    logging.info(f"Received query: {query.question}")
    result = run_ollama(query.question)
    if result.returncode == 0:
        response_data = parse_travel_recommendations(result.stdout)
        logging.info(f"Sending response: {response_data}")
        return response_data
    else:
        error_message = f"Error executing model: {result.stderr}"
        logging.error(error_message)
        return {"error": error_message}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
