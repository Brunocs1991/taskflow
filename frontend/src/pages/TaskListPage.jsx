import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { taskApi } from "../api/taskApi";
import TaskList from "../components/TaskList";

const STATUSES = ["", "TODO", "DOING", "DONE"];

export default function TaskListPage() {
  const nav = useNavigate();
  const [tasks, setTasks] = useState([]);
  const [status, setStatus] = useState("");
  const [q, setQ] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function load() {
    setLoading(true);
    setError("");
    try {
      const data = await taskApi.list({
        page: 0,
        size: 20,
        status: status || undefined,
        q: q || undefined,
      });
      setTasks(data);
    } catch (e) {
      setError(e.message || "Falha ao carregar");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, [status]); // recarrega ao mudar status

  async function onDelete(id) {
    if (!confirm("Excluir esta task?")) return;
    try {
      await taskApi.remove(id);
      await load();
    } catch (e) {
      alert(e.message || "Erro ao excluir");
    }
  }

  return (
    <div style={styles.page}>
      <header style={styles.header}>
        <h1 style={{ margin: 0 }}>TaskFlow</h1>
        <button style={styles.btn} onClick={() => nav("/tasks/new")}>
          Nova Task
        </button>
      </header>

      <form
        style={styles.filters}
        onSubmit={(e) => {
          e.preventDefault();
          load();
        }}
      >
        <input
          value={q}
          onChange={(e) => setQ(e.target.value)}
          placeholder="Buscar por texto..."
          style={styles.input}
        />
        <button type="submit" style={styles.btn}>
          Buscar
        </button>

        <select
          value={status}
          onChange={(e) => setStatus(e.target.value)}
          style={styles.input}
        >
          {STATUSES.map((s) => (
            <option key={s} value={s}>
              {s ? s : "TODOS"}
            </option>
          ))}
        </select>
      </form>

      {loading ? <p>Carregando...</p> : null}
      {error ? <p style={{ color: "salmon" }}>{error}</p> : null}
      {!loading && !error ? (
        <TaskList
          tasks={tasks}
          onEdit={(id) => nav(`/tasks/${id}/edit`)}
          onDelete={onDelete}
        />
      ) : null}
    </div>
  );
}

const styles = {
  page: {
    maxWidth: 860,
    margin: "0 auto",
    padding: 16,
    display: "grid",
    gap: 14,
  },
  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    gap: 12,
    flexWrap: "wrap",
  },
  filters: { display: "flex", gap: 10, flexWrap: "wrap", alignItems: "center" },
  input: {
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
};
