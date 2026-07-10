"""STEP 07 — MCP server exposing ShopPulse tools. (~45 min)

MCP (Model Context Protocol) standardizes how AI apps discover and call tools.
Your step-03 tool schema was bespoke; MCP makes it a protocol (JSON-RPC over
stdio or HTTP) so ANY client (Claude Code, Claude Desktop, custom hosts) can
use your tools without custom integration. Think "USB-C for AI tools".

Tasks:
 1. FastMCP server with two tools (docstrings become tool descriptions!):

      from mcp.server.fastmcp import FastMCP
      import httpx, os
      mcp = FastMCP("shoppulse")
      GATEWAY = os.getenv("GATEWAY_URL", "http://localhost:8080")

      @mcp.tool()
      def get_order_status(order_id: str) -> str:
          \"\"\"Get the current status of a ShopPulse order by its id.\"\"\"
          return httpx.get(f"{GATEWAY}/api/orders/{order_id}").text

      @mcp.tool()
      def search_products(query: str) -> str:
          \"\"\"Search the ShopPulse product catalog.\"\"\"
          return httpx.get(f"{GATEWAY}/api/products/search", params={"q": query}).text

      if __name__ == "__main__":
          mcp.run()   # stdio transport

 2. Register with Claude Code — .mcp.json in the shoppulse repo root:
      {"mcpServers": {"shoppulse": {"command": "python",
        "args": ["services/ai-support-service/scripts/07_mcp_server.py"]}}}
    Restart Claude Code in that folder, then ask it "what's the status of
    order o-1?" and watch it call YOUR server against YOUR microservices.

Interview Qs: MCP architecture (host -> client -1:1-> server)? the three
primitives (tools = model-controlled actions, resources = app-controlled data,
prompts = user-controlled templates)? transports (stdio local, streamable HTTP
remote)? MCP vs plain tool calling (tool calling = model<->one app convention;
MCP = interoperable protocol between apps and tool providers)?
"""
# TODO: implement (the code above is ~90% of it)
