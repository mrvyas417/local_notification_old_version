const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 8080 });

wss.on('connection', function connection(ws) {
  console.log('A new client connected');

  // Send a message to the client every 10 seconds (for testing)
  setInterval(() => {
    ws.send(JSON.stringify({ message: "Hello from WebSocket Server", timestamp: new Date().toLocaleString() }));
  }, 10000);

  
  ws.on('message', function incoming(message) {
    console.log('Received from client:', message);
  });

  // Handle client disconnects
  ws.on('close', function close() {
    console.log('Client disconnected');
  });
});
console.log('WebSocket server is running on ws://localhost:8080 :)');
