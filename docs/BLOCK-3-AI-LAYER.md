# Block 3 — AI layer: LLM → RAG → Agent → MCP → Eval (Sat, ~8h)

Goal: a LangGraph support agent that answers from YOUR docs (RAG) and calls
YOUR microservices (tools), fully traced in Langfuse, scored by RAGAS.

The curriculum lives in `services/ai-support-service/scripts/01..08` — each
file's docstring is the lesson plan, tasks, and interview questions. Do them
IN ORDER; each builds on the last. The core learning trick of this block:
**build raw first (01, 03, 04), framework second (05, 06)** — that's what makes
"LangChain internals" and "RAG internals" honest bullet points instead of
buzzwords.

| Step | File | Time | You come out knowing |
|---|---|---|---|
| 01 | 01_raw_llm.py | 45m | messages/roles, tokens, temperature, streaming/SSE |
| 02 | 02_prompt_engineering.py | 45m | zero/few-shot, CoT, structured output, injection |
| 03 | 03_tool_calling.py | 1h | the tool-call loop = every agent's engine |
| 04 | 04_raw_rag.py | 1.5h | chunking, embeddings, Qdrant/HNSW, grounding |
| 05 | 05_langchain_rag.py | 1h | Runnables, LCEL `\|`, reading LangChain source |
| 06 | 06_langgraph_agent.py | 1.5h | StateGraph, checkpointer memory, HITL interrupt |
| 07 | 07_mcp_server.py | 45m | MCP protocol, tools/resources/prompts, stdio |
| 08 | 08_eval_and_observability.py | 1.5h | Langfuse traces, RAGAS metrics, LLM-as-judge |

Prereqs: `ollama list` shows llama3.1:8b + nomic-embed-text;
`docker compose --profile ai up -d` (Qdrant :6333, Langfuse :3002);
grab a free Groq or Gemini key into `.env` (needed properly for step 08's judge).

Finish by wiring the agent into `app/main.py` (POST /api/support/chat) and
adding the gateway route — then one curl hits Java microservices AND the agent:
```powershell
curl -X POST localhost:8080/api/support/chat -H "Authorization: Bearer <token>" -H "Content-Type: application/json" -d '{"session_id":"s1","message":"where is order o-1 and what is your refund policy?"}'
```
That reply required: JWT validation → routing → LangGraph → RAG retrieval →
tool call back through your gateway → generation. Screenshot the Langfuse trace
of it — that's your portfolio shot.

## Big-picture questions to be able to answer after this block
1. Explain RAG end-to-end and 3 failure modes with fixes (bad retrieval → chunking/top_k/hybrid search; hallucination → grounding prompt + faithfulness eval; stale index → CDC-style reingestion).
2. Agent vs chain? When do you NOT want an agent (deterministic pipelines: cheaper, testable, predictable)?
3. What does LangGraph's checkpointer give you that a chat history list doesn't? (resume/replay, HITL, per-thread persistence, time-travel debugging)
4. MCP vs tool calling — one sentence each, and how they compose (MCP delivers the tools; tool calling is how the model uses them).
5. How would you evaluate + monitor this in production? (golden set in CI, RAGAS metrics, Langfuse traces + cost, user feedback scores, sampled human review)
6. Embeddings: what are they, why cosine similarity, what's approximate about HNSW?
7. Why did raw-then-framework teach you more than framework-only? (have a real answer — interviewers love this meta-question)

## Resources
- Karpathy "Intro to LLMs" if not watched yet — non-negotiable
- DeepLearning.AI short courses (free): "LangChain for LLM App Development", "AI Agents in LangGraph"
- modelcontextprotocol.io — read "Architecture" + do the Python quickstart
- docs.anthropic.com "Tool use" page — clearest tool-calling explanation anywhere
- Qdrant docs: "What is a vector database?" · RAGAS docs: "Metrics" · Langfuse docs: "Tracing"
- Hamel Husain: "Your AI Product Needs Evals" (blog)
