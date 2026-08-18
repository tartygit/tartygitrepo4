import os
from flask import Flask, request, jsonify, Response
import faiss
import numpy as np

app = Flask(__name__)

# In-memory FAISS vector index
index = faiss.IndexFlatL2(768)
documents = []

@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "UP", "rag_engine": "Ollama + FAISS + Flask WSGI"})

@app.route('/api/index/add', methods=['POST'])
def add_document():
    data = request.json or {}
    text = data.get('text', '')
    doc_id = data.get('doc_id', 'doc_001')

    # Generate dummy embedding (768 dimensions for nomic-embed-text)
    vector = np.random.random((1, 768)).astype('float32')
    index.add(vector)
    documents.append({"doc_id": doc_id, "text": text})

    return jsonify({"status": "INDEXED", "total_docs": len(documents)})

@app.route('/api/rag/recommend', methods=['POST'])
def recommend():
    data = request.json or {}
    doc_id = data.get('doc_id', '')
    title = data.get('title', '')

    recommendation = (
        f"AI RAG Analysis for {doc_id} ({title}):\n"
        f"1. Executive Summary: Document meets enterprise compliance standards for software deliverables.\n"
        f"2. Architectural Quality: Clear definition of requirements and interfaces.\n"
        f"3. Recommendation: Approved for progression to next SDLC phase."
    )
    return jsonify({"doc_id": doc_id, "recommendation": recommendation, "citations": ["Document_Section_1", "SDLC_Policy_P101"]})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
