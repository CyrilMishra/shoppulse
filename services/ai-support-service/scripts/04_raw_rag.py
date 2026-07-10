"""STEP 04 — RAG from scratch, ~80 lines, no LangChain. (~1.5 hr)

RAG = stuff relevant retrieved text into the prompt so the model answers from
YOUR data instead of hallucinating. Build every stage by hand once:

 1. CORPUS: write 5-6 markdown files into data/docs/ — ShopPulse FAQ, refund
    policy, shipping policy, product care guides. (Have an LLM draft them.)
 2. CHUNKING: split by paragraphs, ~500 chars with 50-char overlap. Why chunk?
    (embedding quality degrades on long text + you only want relevant pieces in
    the prompt). Why overlap? (don't cut a sentence's meaning at the boundary).
 3. EMBEDDINGS: POST {OLLAMA}/api/embed {"model":"nomic-embed-text","input":[chunks]}
    -> vector per chunk (768 floats). Print two similar sentences' cosine
    similarity vs two unrelated ones. FEEL what an embedding is.
 4. VECTOR DB: qdrant_client — create_collection(size=768, distance=Cosine),
    upsert(points with vector + payload={"text":..., "source":...}).
    Open the Qdrant dashboard: http://localhost:6333/dashboard
 5. RETRIEVE: embed the QUESTION, client.search(top_k=3). Print scores.
 6. GENERATE: prompt = "Answer ONLY from this context:\n{chunks}\n\nQ: {q}.
    If the context doesn't contain the answer, say you don't know."
 7. Ask something NOT in the docs -> confirm it says "I don't know" (grounding).

Interview Qs: full RAG pipeline stages? chunk size trade-off (precision vs
context)? cosine vs dot product (normalize embeddings and they're equivalent)?
what is HNSW (approximate nearest neighbor graph — O(log n) search, small recall
trade-off)? when RAG vs fine-tuning (knowledge vs behavior)? top-k trade-off?
"""
# TODO: implement stages 1-7
