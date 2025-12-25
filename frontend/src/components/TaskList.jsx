export default function TaskList({ tasks, onEdit, onDelete }) {
  if (!tasks.length) return <p>Nenhuma task encontrada.</p>;

  return (
    <div style={{ display: "grid", gap: 10 }}>
      {tasks.map((t) => (
        <div key={t.id} style={styles.item}>
          <div style={{ display: "flex", justifyContent: "space-between", gap: 12 }}>
            <strong>{t.title}</strong>
            <span style={styles.badge}>{t.status}</span>
          </div>
          {t.description ? <p style={{ margin: "6px 0 0", opacity: 0.9 }}>{t.description}</p> : null}

          <div style={styles.actions}>
            <button style={styles.btn} onClick={() => onEdit(t.id)}>Editar</button>
            <button style={styles.btnDanger} onClick={() => onDelete(t.id)}>Excluir</button>
          </div>
        </div>
      ))}
    </div>
  );
}

const styles = {
  item: {
    border: "1px solid #2a2a2a",
    borderRadius: 12,
    padding: 14,
  },
  badge: {
    border: "1px solid #333",
    borderRadius: 999,
    padding: "2px 10px",
    fontSize: 12,
    opacity: 0.9,
    whiteSpace: "nowrap",
  },
  actions: { display: "flex", gap: 8, marginTop: 12 },
  btn: {
    padding: "8px 10px",
    borderRadius: 10,
    border: "1px solid #333",
    background: "transparent",
    color: "inherit",
    cursor: "pointer",
  },
  btnDanger: {
    padding: "8px 10px",
    borderRadius: 10,
    border: "1px solid #5a2a2a",
    background: "transparent",
    color: "inherit",
    cursor: "pointer",
  },
};
