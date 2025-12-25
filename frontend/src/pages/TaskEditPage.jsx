import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import TaskForm from "../components/TaskForm";
import { taskApi } from "../api/taskApi";
import TaskComments from "../components/TaskComments";

export default function TaskEditPage() {
  const { id } = useParams();
  const nav = useNavigate();
  const [task, setTask] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    (async () => {
      setLoading(true);
      setError("");
      try {
        const data = await taskApi.get(id);
        setTask(data);
      } catch (e) {
        setError(e.message || "Falha ao carregar");
      } finally {
        setLoading(false);
      }
    })();
  }, [id]);

  async function onSubmit(payload) {
    setSubmitting(true);
    setError("");
    try {
      await taskApi.update(id, payload);
      nav("/tasks");
    } catch (e) {
      setError(e.message || "Falha ao salvar");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div
      style={{
        maxWidth: 720,
        margin: "0 auto",
        padding: 16,
        display: "grid",
        gap: 12,
      }}
    >
      <h2 style={{ margin: 0 }}>Editar Task</h2>

      {loading ? <p>Carregando...</p> : null}
      {error ? <p style={{ color: "salmon" }}>{error}</p> : null}

      {!loading && task ? (
        <>
          <TaskForm
            initial={task}
            submitting={submitting}
            submitLabel="Salvar"
            onSubmit={onSubmit}
          />
          <TaskComments taskId={id} />
        </>
      ) : null}

      <button
        onClick={() => nav("/tasks")}
        style={{
          padding: 10,
          borderRadius: 10,
          border: "1px solid #333",
          background: "transparent",
          color: "inherit",
        }}
      >
        Voltar
      </button>
    </div>
  );
}
