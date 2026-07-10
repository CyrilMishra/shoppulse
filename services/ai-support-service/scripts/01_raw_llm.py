"""STEP 01 — Raw LLM API. NO frameworks. (~45 min)

Goal: understand exactly what an LLM API call is, so LangChain never feels
like magic. It's just HTTP: a list of {role, content} messages in, a message out.

Tasks:
 1. Call Ollama's OpenAI-compatible endpoint (POST /v1/chat/completions) with
    httpx: messages=[{"role":"system","content":...},{"role":"user","content":...}]
    Print the FULL raw JSON response. Look at: choices[0].message, usage
    (prompt_tokens / completion_tokens — this is what you pay for with hosted APIs).
 2. Play with temperature (0 vs 1.5 — run the same prompt 3x each, observe
    determinism vs creativity) and max_tokens (watch it cut off mid-sentence
    -> finish_reason="length").
 3. STREAMING: pass "stream": true, iterate the SSE lines, print tokens as they
    arrive. This is how every chat UI works (and why they feel fast).
 4. Repeat ONE call against a hosted free tier (Groq or Gemini) to see that the
    API shape is ~identical across vendors.

Interview Qs you can now answer: what's in an LLM request? what are tokens and
why do they cost money? what does temperature actually do (softmax sampling)?
how does streaming work (SSE)? context window limits?
"""
import httpx, json, os
from dotenv import load_dotenv

load_dotenv()
OLLAMA = os.getenv("OLLAMA_BASE_URL", "http://localhost:11434")
MODEL = os.getenv("OLLAMA_MODEL", "llama3.1:8b")

# --- Task 1: your code here ---
resp = httpx.post(f"{OLLAMA}/v1/chat/completions", json={
    "model": MODEL,
    "messages": [
        {"role": "system", "content": "You are a concise e-commerce support agent."},
        {"role": "user", "content": "My order is late. What should I do?"},
    ],
    "temperature": 0.2,
}, timeout=120)
print(json.dumps(resp.json(), indent=2))

# --- Task 2 (temperature/max_tokens), Task 3 (stream=True), Task 4 (Groq/Gemini): TODO ---
