"""STEP 06 — LangGraph support agent with tools + memory. (~1.5 hr)

LangGraph = explicit state machine for agents (vs the implicit while-loop you
wrote in step 03). State flows through NODES; EDGES (some conditional) decide
what's next; a CHECKPOINTER persists state per thread = conversation memory.

Tasks:
 1. Define tools with @tool:
      lookup_docs(question)      -> your step-05 retriever (RAG as a tool!)
      get_order_status(order_id) -> GET {GATEWAY_URL}/api/orders/{id}
      search_products(query)     -> GET {GATEWAY_URL}/api/products/search?q=
 2. Easy mode first: langgraph.prebuilt.create_react_agent(llm, tools,
    checkpointer=MemorySaver()). Chat with it:
      config = {"configurable": {"thread_id": "user-1"}}
    Ask "do you have running shoes?" then "what was my first question?" —
    memory works because the checkpointer replays the thread's state.
 3. Then build the SAME graph manually so you understand the prebuilt:
      graph = StateGraph(MessagesState)
      graph.add_node("agent", call_llm_with_tools)
      graph.add_node("tools", ToolNode(tools))
      graph.add_conditional_edges("agent", tools_condition)  # tool_calls? -> tools : END
      graph.add_edge("tools", "agent")
    Print the loop: agent -> tools -> agent -> END. It IS your step-03 while-loop,
    reified as a graph.
 4. Human-in-the-loop: interrupt_before=["tools"] for a "cancel_order" tool —
    the graph PAUSES before executing; you inspect state and resume. This is
    how production agents gate destructive actions.
 5. Wire the finished agent into app/main.py behind POST /api/support/chat.

Interview Qs: why a graph over a loop (control, persistence, replay, HITL,
parallel branches)? what does the checkpointer store (full state per super-step,
keyed by thread_id)? how do conditional edges work? multi-agent = subgraphs/
supervisor pattern. MEMORY: short-term = checkpointed messages; long-term =
vector store lookup — you built both.
"""
# TODO: implement
