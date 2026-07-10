"""STEP 05 — Rebuild step 04 in LangChain, then read the source. (~1 hr)

Now that you've built RAG by hand, LangChain's abstractions map 1:1:
  your chunker      -> RecursiveCharacterTextSplitter
  your embed calls  -> OllamaEmbeddings
  your qdrant code  -> QdrantVectorStore.as_retriever(k=3)
  your prompt       -> ChatPromptTemplate
  your glue         -> LCEL:  chain = ({"context": retriever | format_docs,
                                        "question": RunnablePassthrough()}
                                       | prompt | llm | StrOutputParser())

Tasks:
 1. Rebuild the RAG pipeline with the mapping above; verify same answers as 04.
 2. LANGCHAIN INTERNALS — read actual source (this is what makes you stand out):
    - Everything is a Runnable with .invoke/.batch/.stream/.astream
    - The | operator is Runnable.__or__ returning RunnableSequence
    - Open in your venv: langchain_core/runnables/base.py — find
      RunnableSequence.invoke(): it's just a for-loop calling each step,
      wrapped in callbacks. Set a breakpoint and step through your chain once.
    - A dict literal in LCEL becomes RunnableParallel (runs branches concurrently).
 3. Swap the retriever to add a score_threshold — one line. THAT is why
    frameworks exist (swappability), and you can now say so from experience.

Interview Qs: what is a Runnable? what does | actually do? LCEL benefits
(streaming/async/batch/tracing for free)? when would you NOT use LangChain
(simple pipelines — the abstraction tax isn't worth it; you can defend this
because you built both versions)?
"""
# TODO: implement
