# Your Trip Advisor

To use this model
1. download ollama:  https://ollama.com/download
2. open command line in this folder and use : ollama create mistrallite -f Modelfile
3. ask questions using: ollama run mistrallite "Your question?" (example ollama run mistrallite "Where will you recommend to travel in summer?")
4. To use with api install pip install fastapi uvicorn, then uvicorn app:app --reload
5. to use the model with swagger go to http://127.0.0.1:8000/docs
#