from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import subprocess
import json

app = FastAPI()

class Query(BaseModel):
    question: str

@app.post("/query/")
async def query_model(query: Query):
    try:
        # Running the model with Ollama
        result = subprocess.run(
            ['ollama', 'run', 'mistrallite', query.question],
            capture_output=True,
            text=True
        )
        if result.returncode == 0:
            try:
                # Attempt to parse the JSON output
                response_data = json.loads(result.stdout)
                return response_data
            except json.JSONDecodeError:
                # Handle JSON errors specifically
                return {"error": "Invalid JSON returned", "stdout": result.stdout}
        else:
            return {"error": "Error executing model", "stderr": result.stderr}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
