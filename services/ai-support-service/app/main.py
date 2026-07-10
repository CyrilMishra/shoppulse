"""ShopPulse AI Support Service (port 8000).

The final product of Block 3: a FastAPI app exposing the LangGraph support agent.
Work through scripts/01..08 FIRST — each layer of this service is built there,
then assembled here.

Run:  uvicorn app.main:app --reload --port 8000
"""
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI(title="ShopPulse AI Support")


class ChatRequest(BaseModel):
    session_id: str
    message: str


@app.get("/health")
def health():
    return {"status": "UP"}


@app.post("/api/support/chat")
def chat(req: ChatRequest):
    # TODO (Block 3, step 06): invoke the LangGraph agent with a checkpointer
    # keyed by req.session_id so the conversation has memory:
    #   result = agent.invoke({"messages": [("user", req.message)]},
    #                         config={"configurable": {"thread_id": req.session_id}})
    # TODO (step 07 of Block 3): wrap with Langfuse callback handler for tracing.
    return {"reply": "TODO: wire the LangGraph agent from scripts/06_langgraph_agent.py"}
