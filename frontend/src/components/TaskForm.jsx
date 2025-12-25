import { useState } from "react";

const STATUSES = ["TODO", "DOING", "DONE"];

export default function TaskForm({ initial, onSubmit, submitting, submitLabel }) {
  const [title, setTitle] = useState(initial?.title ?? "");
  const [description, setDescription] = useState(initial?.description ?? "");
  const [status, setStatus] = useState(initial?.status ?? "TODO");

  function handleSubmit(e) {
    e.preventDefault();
    onSubmit({ title, description, status });
  }

  return (
    <form onSubmit={handleSubmit} style={styles.card}>
      <label style={styles.label}>
        Título *
        <input
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
          style={styles.input}
          placeholder="Ex: Estudar Micronaut"
        />
      </label>

      <label style={styles.label}>
        Descrição
        <textarea
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          style={{ ...styles.input, minHeight: 90 }}
          placeholder="Detalhes (opcional)"
        />
      </label>

      <label style={styles.label}>
        Status *
        <select value={status} onChange={(e) => setStatus(e.target.value)} style={styles.input}>
          {STATUSES.map((s) => (
            <option key={s} value={s}>
              {s}
            </option>
          ))}
        </select>
      </label>

      <button disabled={submitting} style={styles.button}>
        {submitting ? "Salvando..." : submitLabel}
      </button>
    </form>
  );
}

const styles = {
  card: {
    display: "grid",
    gap: 12,
    padding: 16,
    border: "1px solid #2a2a2a",
    borderRadius: 12,
  },
  label: { display: "grid", gap: 6, fontSize: 14 },
  input: {
    padding: 10,
    borderRadius: 10,
    border: "1px solid #333",
    background: "transparent",
    color: "inherit",
  },
  button: {
    padding: 12,
    borderRadius: 10,
    border: "1px solid #333",
    cursor: "pointer",
    background: "transparent",
    color: "inherit",
  },
};
