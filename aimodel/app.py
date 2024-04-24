from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from transformers import pipeline

app = FastAPI()

# Initialize the pipeline
pipe = pipeline("text2text-generation", model="google/flan-t5-large")

class GenerationRequest(BaseModel):
    text: str
    max_length: int = 50  # Default max length, you can adjust this
    num_return_sequences: int = 1  # Default number of sequences

@app.post("/generate")
def generate_post(request: GenerationRequest):
    try:
        output = pipe(request.text, max_length=request.max_length, num_return_sequences=request.num_return_sequences)
        return {"output": [result["generated_text"] for result in output]}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
