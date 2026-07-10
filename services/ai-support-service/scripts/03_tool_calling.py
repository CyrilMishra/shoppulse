"""STEP 03 — Tool calling, RAW (no LangChain). (~1 hr)  ** cornerstone step **

The model NEVER executes anything. It emits a JSON "I want to call tool X with
args Y"; YOUR code executes it and sends the result back. Once you've written
this loop by hand, agents/LangGraph/MCP are all obviously just this loop.

Tasks:
 1. Define a tool schema (OpenAI format works with Ollama):
    {"type":"function","function":{"name":"get_order_status",
      "description":"Get the status of a customer order by id",
      "parameters":{"type":"object",
        "properties":{"order_id":{"type":"string"}},
        "required":["order_id"]}}}
 2. Send: messages=[user: "where is my order o-42?"], tools=[...above...]
    Inspect the response: message.tool_calls[0].function.{name,arguments}
    — the model CHOSE the tool and produced args. Print it. Stare at it.
 3. Execute it for real: httpx.get(f"{GATEWAY_URL}/api/orders/o-42") against
    YOUR order-service (or stub it if Block 2 isn't done yet).
 4. Append to messages: the assistant msg with tool_calls, then
    {"role":"tool","tool_call_id":...,"content":json.dumps(result)} and call
    the LLM again -> it writes a natural-language answer from the data.
 5. Wrap 2-4 in a while loop until there are no more tool_calls. Congratulations,
    you have written an AGENT (the ReAct loop) in ~40 lines.

Interview Qs: walk me through the tool-calling message flow (this exact 4-step
dance). Who executes tools? How does the model know when to stop? What if the
tool errors (feed the error back as the tool message — the model can recover)?
"""
# TODO: implement tasks 1-5
