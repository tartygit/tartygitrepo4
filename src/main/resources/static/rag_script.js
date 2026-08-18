    function generateAiRecommendation() {
        let docId = document.getElementById("ragDocSelect").value;
        if (!docId) { alert("Please select a document!"); return; }
        document.getElementById("ragResultArea").innerHTML = "<em>Analyzing document with AI LLM RAG engine...</em>";

        const formData = new FormData();
        formData.append("docId", docId);

        fetch("/api/rag/recommend", { method: "POST", body: formData })
            .then(r => r.json())
            .then(data => {
                if (data.recommendation) {
                    document.getElementById("ragResultArea").innerHTML = `<strong>AI LLM RAG Recommendation for ${docId}:</strong><br>` +
                        data.recommendation.replace(/\n/g, '<br>');
                } else {
                    document.getElementById("ragResultArea").innerHTML = "Error generating AI recommendation.";
                }
            })
            .catch(e => {
                document.getElementById("ragResultArea").innerHTML = "Failed to communicate with RAG backend service.";
            });
    }
