import { useState } from "react";
import { useNavigate } from "react-router-dom";
import TaskForm from "../components/TaskForm";
import { taskApi } from "../api/taskApi";

export default function TaskCreatePage() {
  const nav = useNavigate();
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  async function onSubmit(payload) {
    setSubmitting(true);
    setError("");
    try {
      await taskApi.create(payload);
      nav("/tasks");
    } catch (e) {
      setError(e.message || "Falha ao criar");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div style={{ maxWidth: 720, margin: "0 auto", padding: 16, display: "grid", gap: 12 }}>
      <h2 style={{ margin: 0 }}>Nova Task</h2>
      {error ? <p style={{ color: "salmon" }}>{error}</p> : null}
      <TaskForm submitting={submitting} submitLabel="Criar" onSubmit={onSubmit} />
      <button onClick={() => nav("/tasks")} style={{ padding: 10, borderRadius: 10, border: "1px solid #333", background: "transparent", color: "inherit" }}>
        Voltar
      </button>
    </div>
  );
}
