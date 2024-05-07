from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import subprocess
import os

app = FastAPI()

class Query(BaseModel):
    question: str

def run_ollama(question):
    """Run the Ollama model command with adjusted subprocess settings for Windows."""
    if os.name == 'nt':  # Check if the operating system is Windows
        # Disable the console window on Windows to prevent console mode errors
        creationflags = subprocess.CREATE_NO_WINDOW
    else:
        creationflags = 0  # No special flags needed for non-Windows systems

    result = subprocess.run(
        ['ollama', 'run', 'mistrallite', question],
        capture_output=True,
        text=True,
        creationflags=creationflags
    )
    return result

def parse_travel_recommendations(text):
    """Parse the output text to extract useful travel information and ignore errors."""
    # Filter out error messages specific to console mode on Windows
    lines = text.split('\n')
    useful_lines = [line for line in lines if line and not line.startswith("failed to get console mode")]

    # Simple extraction of introductory and detailed text
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
    result = run_ollama(query.question)
    if result.returncode == 0:
        # Parse the output to create a structured JSON response
        response_data = parse_travel_recommendations(result.stdout)
        return response_data
    else:
        # Handle errors from the subprocess, including non-zero exit codes
        return {"error": "Error executing model", "stderr": result.stderr}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
