"""STEP 08 — Evaluation (RAGAS) + Observability (Langfuse). (~1.5 hr)

"How do you know your RAG/agent is any good, and how do you debug it in prod?"
— the question that separates people who've shipped LLM features from tutorial-land.

A) LANGFUSE (observability) — docker compose --profile ai up -d, open
   http://localhost:3002, sign up, create project, put keys in .env.
 1. from langfuse.callback import CallbackHandler
    chain.invoke(q, config={"callbacks": [CallbackHandler()]})
 2. Open the UI: full trace tree — every retrieval, every LLM call, latency,
    token counts per span. Now trace one AGENT run from step 06 and look at the
    multi-step trace (agent loop iterations visible!).
 3. Vocabulary to take away: trace / span / generation; cost tracking; scores
    (attach user feedback to traces); prompt management (versioned prompts).

B) RAGAS (evaluation)
 1. Build a golden dataset: 10 questions about YOUR docs with hand-written
    ground-truth answers (do this properly — garbage golden set = garbage eval).
 2. Run your step-05 chain over them, collect (question, answer,
    retrieved contexts, ground_truth).
 3. ragas.evaluate() with metrics:
      faithfulness        — is the answer supported by retrieved context? (anti-hallucination)
      answer_relevancy    — does it address the question?
      context_precision   — is the good chunk ranked high?
      context_recall      — did retrieval find the needed info at all?
    NOTE: metrics are computed BY an LLM (LLM-as-judge) — use the Gemini/Groq
    key here; a 8B local model judges poorly.
 4. Break retrieval on purpose (top_k=1, or chunk_size=2000) and re-run —
    watch context_recall drop. NOW you know which knob maps to which metric.

Interview Qs: how do you evaluate RAG (separate RETRIEVAL metrics from
GENERATION metrics)? what is LLM-as-judge and its risks (bias, cost, judge
drift)? offline eval (golden set, CI regression) vs online (user feedback,
A/B)? what would you monitor in prod (latency p95, token cost/day, faithfulness
sampled, thumbs-down rate)?
"""
# TODO: implement A then B
