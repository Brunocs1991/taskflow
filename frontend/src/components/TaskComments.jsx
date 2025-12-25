import { useEffect, useState } from "react";
import { taskApi } from "../api/taskApi";

export default function TaskComments({ taskId }) {
  const [comments, setComments] = useState([]);
  const [text, setText] = useState("");
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  async function load() {
    setLoading(true);
    setError("");
    try {
      const data = await taskApi.listComments(taskId);
      setComments(data);
    } catch (e) {
      setError(e.message || "Falha ao carregar comentários");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    if (!taskId) return;
    load();
  }, [taskId]);

  async function add(e) {
    e.preventDefault();
    if (!text.trim()) return;

    setSubmitting(true);
    setError("");
    try {
      await taskApi.addComment(taskId, { text: text.trim() });
      setText("");
      await load();
    } catch (e2) {
      setError(e2.message || "Falha ao adicionar comentário");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <section style={styles.card}>
      <h3 style={{ margin: 0 }}>Comentários</h3>

      <form onSubmit={add} style={styles.form}>
        <input
          value={text}
          onChange={(e) => setText(e.target.value)}
          placeholder="Adicionar comentário..."
          style={styles.input}
        />
        <button disabled={submitting} style={styles.btn}>
          {submitting ? "Enviando..." : "Adicionar"}
        </button>
      </form>

      {loading ? <p>Carregando comentários...</p> : null}
      {error ? <p style={{ color: "salmon" }}>{error}</p> : null}

      {!loading && !error ? (
        comments.length ? (
          <ul style={styles.list}>
            {comments.map((c) => (
              <li key={c.id} style={styles.item}>
                <div style={{ fontSize: 14 }}>{c.text}</div>
                {c.createdAt ? (
                  <div style={{ fontSize: 12, opacity: 0.7, marginTop: 4 }}>
                    {new Date(c.createdAt).toLocaleString()}
                  </div>
                ) : null}
              </li>
            ))}
          </ul>
        ) : (
          <p>Nenhum comentário ainda.</p>
        )
      ) : null}
    </section>
  );
}

const styles = {
  card: { border: "1px solid #2a2a2a", borderRadius: 12, padding: 14, display: "grid", gap: 12 },
  form: { display: "flex", gap: 10, flexWrap: "wrap" },
  input: {
    flex: "1 1 240px",
    padding: 10,
    borderRadius: 10,
    border: "1px solid #333",
    background: "transparent",
    color: "inherit",
  },
  btn: {
    padding: "10px 12px",
    borderRadius: 10,
    border: "1px solid #333",
    background: "transparent",
    color: "inherit",
    cursor: "pointer",
  },
  list: { listStyle: "none", padding: 0, margin: 0, display: "grid", gap: 10 },
  item: { border: "1px solid #2a2a2a", borderRadius: 12, padding: 12 },
};
